package org.sixLoadTester.testers;

import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.data.ResponseData;
import org.sixLoadTester.utils.StatisticsUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EndpointStressTester extends EndpointTester {

    private static final int MAX_THREADS = 10000;
    private static final int MAX_ERRORS = 10;
    private static final int NUM_UPDATE_FREQUENCY_IN_MS = 100;

    private int increaseInRequestsPerSecond;

    public EndpointStressTester(RequestData requestData) {
        super(requestData);
    }

    @Override
    public void execute() throws InterruptedException {
        var executorService = Executors.newScheduledThreadPool(MAX_THREADS);
        System.setErr(null);

        ResponseData responseData = new ResponseData();
        float delayBetweenRequestsInMs = 1000.f / increaseInRequestsPerSecond;

        for (int i = 0; i < MAX_THREADS; i++) {
            long initialDelay = (long) (i * delayBetweenRequestsInMs);
            TesterRunner runner = new TesterRunner(requestData, responseData);
            executorService.scheduleAtFixedRate(runner, initialDelay, 1000, TimeUnit.MILLISECONDS);
        }

        while (responseData.errorCount.get() < MAX_ERRORS) {
            Thread.sleep(NUM_UPDATE_FREQUENCY_IN_MS);
            System.out.print("\rNumber of requests per second: " + responseData.maxRequestsPerSecond.get());
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        produceStatistics(responseData);
    }

    protected void produceStatistics(ResponseData responseData) {
        //TODO remove totalDuration param
        StatisticsUtils.calculateStatistics(responseData, 0);
        StatisticsUtils.createChart(responseData.responseTimes);
    }

    public void setIncreaseInRequestsPerSecond(int increaseInRequestsPerSecond) {
        this.increaseInRequestsPerSecond = increaseInRequestsPerSecond;
    }
}
