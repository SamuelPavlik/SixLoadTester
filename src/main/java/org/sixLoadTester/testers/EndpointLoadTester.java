package org.sixLoadTester.testers;

import org.sixLoadTester.data.ResponseData;
import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.utils.StatisticsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EndpointLoadTester extends EndpointTester {

    private static final int DEFAULT_MAX_REQUESTS_PER_SECOND = 400;
    private static final int DEFAULT_RAMP_UP_TIME_IN_MS = 5000;
    private static final int DEFAULT_RAMP_DOWN_TIME_IN_MS = 5000;
    private static final int DEFAULT_DURATION_IN_MS = 10000;

    private int maxRequestsPerSecond = DEFAULT_MAX_REQUESTS_PER_SECOND;
    private int rampUpTimeInMs = DEFAULT_RAMP_UP_TIME_IN_MS;
    private int rampDownTimeInMs = DEFAULT_RAMP_DOWN_TIME_IN_MS;
    private int durationInMs = DEFAULT_DURATION_IN_MS;

    public EndpointLoadTester(RequestData requestData) {
        super(requestData);
    }

    public void execute() throws InterruptedException {
        if (maxRequestsPerSecond <= 0)
            return;

        System.out.println("Load test initiated");
        System.out.println("Ramp up initiated");

        var executorService = Executors.newScheduledThreadPool(maxRequestsPerSecond);
        var responseData = new ResponseData();
        List<ScheduledFuture<?>> scheduledFutures = initiateRampUp(executorService, responseData);

        Thread.sleep(rampUpTimeInMs);
        System.out.println("Ramp up finished");

        Thread.sleep(durationInMs);
        System.out.println("Ramp down initiated");

        rampDownRequests(scheduledFutures);

        System.out.println("Ramp down finished");

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        produceStatistics(responseData, durationInMs + rampDownTimeInMs + rampUpTimeInMs);
    }

    private List<ScheduledFuture<?>> initiateRampUp(ScheduledExecutorService executorService, ResponseData responseData) {
        List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
        long oneSecInNs = TimeUnit.SECONDS.toNanos(1);

        for (int i = 0; i < maxRequestsPerSecond; i++) {
            long initialDelayInNs = i * (TimeUnit.MILLISECONDS.toNanos(rampUpTimeInMs) / maxRequestsPerSecond);
            TesterRunner runner = new TesterRunner(requestData, responseData);
            scheduledFutures.add(executorService.scheduleAtFixedRate(runner, initialDelayInNs, oneSecInNs, TimeUnit.NANOSECONDS));
        }

        return scheduledFutures;
    }

    private void rampDownRequests(List<ScheduledFuture<?>> scheduledFutures) throws InterruptedException {
        long pauseBetweenCancelsInNs = TimeUnit.MILLISECONDS.toNanos(rampDownTimeInMs) / maxRequestsPerSecond;
        long pauseBetweenCancelsInMs = TimeUnit.NANOSECONDS.toMillis(pauseBetweenCancelsInNs);
        int leftOverInNs = (int) (pauseBetweenCancelsInNs % TimeUnit.MILLISECONDS.toNanos(1));
        for (int i = 0; i < maxRequestsPerSecond; i++) {
            scheduledFutures.get(i).cancel(false);
            Thread.sleep(pauseBetweenCancelsInMs, leftOverInNs);
        }
    }

    private void produceStatistics(ResponseData responseData, int totalDurationInMs) {
        StatisticsUtils.calculateStatistics(responseData, totalDurationInMs);
        StatisticsUtils.createChart(responseData.responseTimes);
    }

    public void setMaxRequestsPerSecond(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
    }

    public void setRampUpTimeInMs(int rampUpTimeInMs) {
        this.rampUpTimeInMs = rampUpTimeInMs;
    }

    public void setRampDownTimeInMs(int rampDownTimeInMs) {
        this.rampDownTimeInMs = rampDownTimeInMs;
    }

    public void setDurationInMs(int durationInMs) {
        this.durationInMs = durationInMs;
    }
}

