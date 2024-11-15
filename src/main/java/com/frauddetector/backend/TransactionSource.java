package com.frauddetector.backend;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import java.util.Random;
public class TransactionSource implements SourceFunction<Transaction> {
    private volatile boolean running = true;
    private Random random = new Random();
    @Override
    public void run(SourceContext<Transaction> ctx) throws Exception {
        while (running) {
            String payerId = "PAYER-" + random.nextInt(1000);
            double amount = 50 + (10000 - 50) * random.nextDouble();
            String beneficiaryId = "BENEFICIARY-" + random.nextInt(1000);
            String[] types = {"WIRE", "CREDIT", "DEBIT"};
            String transactionType = types[random.nextInt(types.length)];
            Transaction transaction = new Transaction(payerId, amount, beneficiaryId,transactionType);
            ctx.collect(transaction);
            Thread.sleep(100); // Emit transactions every 100 ms
        }
    }
    @Override
    public void cancel() {
        running = false;
    }
}