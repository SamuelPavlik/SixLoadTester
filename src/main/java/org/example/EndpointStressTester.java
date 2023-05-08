package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EndpointStressTester extends EndpointTester {

    private static final int MAX_THREADS = 10000;

    private int increaseInRequestsPerSecond;

    public EndpointStressTester(HttpMethod method, String endpoint) {
        super(endpoint, method);
    }

    public EndpointStressTester(String endpoint, HttpMethod method, String jsonData) {
        super(endpoint, method, jsonData);
    }

    @Override
    public void execute() throws InterruptedException {
        var executorService = Executors.newScheduledThreadPool(MAX_THREADS);
//        System.setErr(null);

        AtomicInteger overallCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        float delayBetweenRequestsInMs = 1000.f / increaseInRequestsPerSecond;
        for (int i = 0; i < MAX_THREADS; i++) {
            long initialDelay = (long) (i * delayBetweenRequestsInMs);
            executorService.scheduleAtFixedRate(new MyRunnable(errorCount, overallCount), initialDelay,
                    1000, TimeUnit.MILLISECONDS);
        }

        while (errorCount.get() < 10) {
            Thread.sleep(100);
        }

        System.out.println("Overall count: " + overallCount);
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Override
    public void produceStatistics() throws NoDataAvailableException {

    }

    public void setIncreaseInRequestsPerSecond(int increaseInRequestsPerSecond) {
        this.increaseInRequestsPerSecond = increaseInRequestsPerSecond;
    }
}
