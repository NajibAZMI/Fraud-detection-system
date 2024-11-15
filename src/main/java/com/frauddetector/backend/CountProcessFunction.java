package com.frauddetector.backend;
import com.frauddetector.frontend.models.Transaction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
public class CountProcessFunction extends ProcessWindowFunction<Transaction, AggregatedResult, String, TimeWindow> {
    @Override
    public void process(String key, Context context, Iterable<Transaction> elements, Collector<AggregatedResult> out) {
        long count = 0;
        for (Transaction t : elements) {
            count++;
        }
        out.collect(new AggregatedResult(key, count));

    }
}