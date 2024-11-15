package com.frauddetector.backend;


import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import com.frauddetector.frontend.models.Transaction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
public class AverageProcessFunction extends ProcessWindowFunction<Transaction,AggregatedResult, String, TimeWindow> {
    @Override
    public void process(String key, Context context, Iterable<Transaction> elements, Collector<AggregatedResult> out) {
        double sum = 0;
        long count = 0;
        for (Transaction t : elements) {
            sum += t.getAmount();
            count++;
        }
        double average = count == 0 ? 0 : sum / count;
        out.collect(new AggregatedResult(key, average));
    }
}