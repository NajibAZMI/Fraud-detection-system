package com.frauddetector.backend;

import com.frauddetector.frontend.controllers.AlertViewController;
import com.frauddetector.frontend.models.Alert;
import com.frauddetector.frontend.models.Rule;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class FraudDetectionJob {
    public static void main(String[] args) throws Exception {
        // Set up the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        DataStream<Transaction> transactionStream = env.addSource(new TransactionSource()).assignTimestampsAndWatermarks(new TransactionTimestampExtractor());
        // Apply rules
        List<Rule> activeRules = RuleProcessor.getActiveRules();
        for (Rule rule : activeRules) {
            applyRule(transactionStream, rule);
        }
        // Execute the job
        env.execute("Real-Time Fraud Detection Job");
    }
    private static void applyRule(DataStream<Transaction> stream, Rule rule) {
        KeyedStream<Transaction, String> keyedStream = stream.keyBy(transaction -> {
            switch (rule.getGroupingKey()) {
                case "Payer ID":
                    return transaction.getPayerId();
                case "Beneficiary ID":
                    return transaction.getBeneficiaryId();
                case "Transaction Type":
                    return transaction.getTransactionType();
                default:
                    return transaction.getPayerId();
            }
        });
        long windowSize = parseTimeWindow(rule.getTimeWindow());
        boolean isSliding = rule.getWindowType().equalsIgnoreCase("SLIDING");
        long slide = isSliding ? windowSize / 2 : windowSize; /
        WindowedStream<Transaction, String, org.apache.flink.streaming.api.windowing.windows.TimeWindow> windowedStream;
        if (isSliding) {
            windowedStream =
                    keyedStream.timeWindow(org.apache.flink.streaming.api.windowing.time.Time.milliseconds(window org.apache.flink.streaming.api.windowing.time.Time.milliseconds(slide));
        } else {
            windowedStream =
                    keyedStream.timeWindow(org.apache.flink.streaming.api.windowing.time.Time.milliseconds(window
        }
        DataStream<AggregatedResult> aggregatedStream;
        switch (rule.getAggregationFunction()) {
            case "SUM":
                aggregatedStream = windowedStream.sum("amount")
                        .map(transaction -> new
                                AggregatedResult(getGroupingKey(transaction, rule), transaction.getAmount()));
                break;
            case "COUNT":
                aggregatedStream = windowedStream
                        .process(new CountProcessFunction())
                        .map(count -> new AggregatedResult(count.getKey(), count.getValue()));
                break;
            case "AVERAGE":
                aggregatedStream = windowedStream
                        .process(new AverageProcessFunction())
                        .map(avg -> new AggregatedResult(avg.getKey(), avg.getValue()));
                break;
            default:
                aggregatedStream = windowedStream.sum("amount")
                        .map(transaction -> new
                                AggregatedResult(getGroupingKey(transaction, rule), transaction.getAmount()));
        }
        // Apply threshold condition
        DataStream<AggregatedResult> flaggedStream = aggregatedStream.filter(new FilterFunction<AggregatedResult>() {
            @Override
            public boolean filter(AggregatedResult value) {
                switch (rule.getOperator()) {
                        case "GREATER":
                             return value.getValue() > rule.getThreshold();
                        case "LESS":
                             return value.getValue() < rule.getThreshold();
                        case "GREATER_EQUAL":
                             return value.getValue() >= rule.getThreshold();
                        case "LESS_EQUAL":
                             return value.getValue() <= rule.getThreshold();
                        default:
                              return false;
                }
            }
        });
        // Handle flagged transactions by sending alerts to the frontend
        flaggedStream.map(result -> {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
                    Date());
            String details = String.format("Rule '%s' triggered for key '%s' with value %.2f at %s",
                    rule.getAggregationFunction(), result.getKey(), result.getValue(), timestamp);
            return new Alert(rule.getAggregationFunction(), details,
                    System.currentTimeMillis());
        }).addSink(alert -> {
            Platform.runLater(() -> {
               AlertViewControllerSingleton.getInstance().addAlert(alert);
            });
        });
    }
    private static String getGroupingKey(Transaction transaction, Rule rule) {
        switch (rule.getGroupingKey()) {
            case "Payer ID":
                return transaction.getPayerId();
            case "Beneficiary ID":
                return transaction.getBeneficiaryId();
            case "Transaction Type":
                return transaction.getTransactionType();
            default:
                return transaction.getPayerId();
        }
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