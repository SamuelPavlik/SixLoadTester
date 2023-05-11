package org.sixLoadTester.testers;

import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.data.ResponseData;
import org.sixLoadTester.utils.StatisticsUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
        System.setErr(null);

        var executorService = Executors.newScheduledThreadPool(MAX_THREADS);
        var responseData = new ResponseData();
        initiateRampUp(executorService, responseData);

        waitForReachingStressPoint(responseData);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        produceStatistics(responseData);
    }

    private static void waitForReachingStressPoint(ResponseData responseData) throws InterruptedException {
        while (responseData.errorCount.get() < MAX_ERRORS) {
            Thread.sleep(NUM_UPDATE_FREQUENCY_IN_MS);
            System.out.print("\rNumber of requests per second: " + responseData.maxRequestsPerSecond.get());
        }
    }

    private void initiateRampUp(ScheduledExecutorService executorService, ResponseData responseData) {
        long delayBetweenRequestsInNs = TimeUnit.SECONDS.toNanos(1) / increaseInRequestsPerSecond;
        long oneSecInNs = TimeUnit.SECONDS.toNanos(1);

        for (int i = 0; i < MAX_THREADS; i++) {
            long initialDelayInNs = i * delayBetweenRequestsInNs;
            var runner = new TesterRunner(requestData, responseData);
            executorService.scheduleAtFixedRate(runner, initialDelayInNs, oneSecInNs, TimeUnit.NANOSECONDS);
        }
    }

    protected void produceStatistics(ResponseData responseData) {
        StatisticsUtils.calculateStatistics(responseData);
        StatisticsUtils.createChart(responseData.responseTimes);
    }

    public void setIncreaseInRequestsPerSecond(int increaseInRequestsPerSecond) {
        this.increaseInRequestsPerSecond = increaseInRequestsPerSecond;
    }
}
