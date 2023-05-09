package org.sixLoadTester.testers;

import org.sixLoadTester.exceptions.NoDataAvailableException;
import org.sixLoadTester.utils.RequestData;
import org.sixLoadTester.utils.ResponseData;
import org.sixLoadTester.utils.StatisticsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EndpointStressTester extends EndpointTester {

    private static final int MAX_THREADS = 10000;
    private static final int MAX_ERRORS = 10;
    private static final int NUM_UPDATE_FREQUENCY_IN_MS = 100;

    private int increaseInRequestsPerSecond;
    private ResponseData responseData;

    public EndpointStressTester(RequestData requestData) {
        super(requestData);
    }

    @Override
    public void execute() throws InterruptedException {
        var executorService = Executors.newScheduledThreadPool(MAX_THREADS);
        System.setErr(null);

        AtomicInteger overallCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        List<Long> responseTimes = new ArrayList<>();
        float delayBetweenRequestsInMs = 1000.f / increaseInRequestsPerSecond;
        for (int i = 0; i < MAX_THREADS; i++) {
            long initialDelay = (long) (i * delayBetweenRequestsInMs);
            executorService.scheduleAtFixedRate(new StressTesterRunner(requestData, errorCount, overallCount, responseTimes), initialDelay,
                    1000, TimeUnit.MILLISECONDS);
        }

        while (errorCount.get() < MAX_ERRORS) {
            Thread.sleep(NUM_UPDATE_FREQUENCY_IN_MS);
            System.out.print("\rNumber of requests per second: " + overallCount.get());
        }

        responseData = new ResponseData();
        responseData.errorCount = errorCount;
        responseData.overallCount = overallCount;
        responseData.responseTimes = responseTimes;

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Override
    public void produceStatistics() throws NoDataAvailableException {
        System.out.println("\nOverall count: " + responseData.overallCount);

        StatisticsUtils.createChart(responseData.responseTimes);
    }

    public void setIncreaseInRequestsPerSecond(int increaseInRequestsPerSecond) {
        this.increaseInRequestsPerSecond = increaseInRequestsPerSecond;
    }
}
