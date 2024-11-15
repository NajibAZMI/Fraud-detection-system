package com.frauddetector.backend;

import com.frauddetector.frontend.controllers.AlertViewController;
import com.frauddetector.frontend.models.Alert;
import com.frauddetector.frontend.models.Rule;
import javafx.application.Platform;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import com.frauddetector.frontend.models.Transaction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction.Context;
public class FraudDetectionJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        DataStream<Transaction> transactionStream = env
                .addSource(new TransactionSource())
                .assignTimestampsAndWatermarks(new TransactionTimestampExtractor());

        List<Rule> activeRules = RuleProcessor.getActiveRules();
        for (Rule rule : activeRules) {
            applyRule(transactionStream, rule);
        }

        env.execute("Real-Time Fraud Detection Job");
    }

    private static void applyRule(DataStream<Transaction> stream, Rule rule) {
        KeyedStream<Transaction, String> keyedStream = stream.keyBy(transaction -> {
            switch (rule.getGroupingKey()) {
                case "Payer ID":
                    return transaction.getPayerId();
                case "Beneficiary ID":
                    return transaction.getBeneficiaryId();
                default:
                    return transaction.getTransactionType();
            }
        });

        long windowSize = parseTimeWindow(rule.getTimeWindow());
        WindowedStream<Transaction, String, TimeWindow> windowedStream = keyedStream.timeWindow(Time.milliseconds(windowSize));

        DataStream<Transaction> aggregatedStream = windowedStream
                .reduce((t1, t2) -> new Transaction(
                        t1.getPayerId(),                      // Payer ID (String)
                        t1.getAmount() + t2.getAmount(),        // Summing the amounts (double)
                        t1.getBeneficiaryId(),                 // Beneficiary ID (String)
                        t1.getTransactionType()
                ));
        DataStream<Transaction> flaggedStream = aggregatedStream.filter(transaction -> {
            double threshold = rule.getThreshold(); // Convert threshold to double

            switch (rule.getOperator()) {
                case "GREATER":
                    return transaction.getAmount() > threshold;
                case "LESS":
                    return transaction.getAmount() < threshold;
                default:
                    return transaction.getAmount() > threshold;
            }
        });

        flaggedStream.map(transaction -> {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String details = String.format("Rule '%s' triggered for transaction '%s' with amount %.2f at %s",
                    rule.getAggregationFunction(), transaction.getPayerId(), transaction.getAmount(), timestamp);
            return new Alert(rule.getAggregationFunction(), details, System.currentTimeMillis());
        }).addSink(new SinkFunction<Alert>() {
            @Override
            public void invoke(Alert alert, Context context) {
                Platform.runLater(() -> {
                    AlertViewControllerSingleton.getInstance().addAlert(alert);
                });
            }
        });
    }


    private static long parseTimeWindow(String timeWindow) {
        // Simple parser for time windows like "30 minutes", "24 hours"
        String[] parts = timeWindow.split(" ");
        long value = Long.parseLong(parts[0]);
        String unit = parts[1].toLowerCase();
        switch (unit) {
            case "seconds":
            case "second":
                return value * 1000;
            case "minutes":
            case "minute":
                return value * 60 * 1000;
            case "hours":
            case "hour":
                return value * 60 * 60 * 1000;
            default:
                return value * 60 * 1000; // default to minutes
        }
    }
}
