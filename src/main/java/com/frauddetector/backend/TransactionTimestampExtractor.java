package com.frauddetector.backend;
import com.frauddetector.frontend.models.Transaction;
import org.apache.flink.api.common.eventtime.*;
import java.time.Duration;
public class TransactionTimestampExtractor implements WatermarkStrategy<Transaction> {
    @Override
    public TimestampAssigner<Transaction>
    createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return new SerializableTimestampAssigner<Transaction>() {
            @Override
            public long extractTimestamp(Transaction element, long recordTimestamp) {
                return System.currentTimeMillis();
            }
        };
    }
    @Override
    public WatermarkGenerator<Transaction>
    createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new WatermarkGenerator<Transaction>() {
            private final long maxOutOfOrderness = 10000; // 10 seconds
            private long currentMaxTimestamp = Long.MIN_VALUE;
            @Override
            public void onEvent(Transaction event, long eventTimestamp, WatermarkOutput output) {
                currentMaxTimestamp = Math.max(currentMaxTimestamp, eventTimestamp);
            }
            @Override
            public void onPeriodicEmit(WatermarkOutput output) {
                output.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderness - 1));
            }
        };
    }
}
