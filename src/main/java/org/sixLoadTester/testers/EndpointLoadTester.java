package org.sixLoadTester.testers;

import org.sixLoadTester.data.Request;
import org.sixLoadTester.data.ResponseStatistics;
import org.sixLoadTester.exceptions.NegativeNumberArgumentException;

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
    private long rampUpTimeInMs = DEFAULT_RAMP_UP_TIME_IN_MS;
    private long rampDownTimeInMs = DEFAULT_RAMP_DOWN_TIME_IN_MS;
    private long durationInMs = DEFAULT_DURATION_IN_MS;

    public EndpointLoadTester(Request request) {
        super(request);
    }

    @Override
    public void executeInternal(boolean withChart) throws InterruptedException {
        System.out.println("Load test initiated");

        if (maxRequestsPerSecond < 0 || rampUpTimeInMs < 0 || rampDownTimeInMs < 0 || durationInMs < 0)
        {
            throw new NegativeNumberArgumentException();
        }

        System.out.println("Ramp up initiated");

        var executorService = Executors.newScheduledThreadPool(maxRequestsPerSecond);
        var responseData = new ResponseStatistics();
        List<ScheduledFuture<?>> scheduledFutures = initiateSchedulersForRampUp(executorService, responseData);

        Thread.sleep(rampUpTimeInMs);
        System.out.println("Ramp up finished");

        Thread.sleep(durationInMs);
        System.out.println("Ramp down initiated");

        rampDownRequests(scheduledFutures);
        System.out.println("Ramp down finished");

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        produceStatistics(request, responseData, withChart);
    }

    private List<ScheduledFuture<?>> initiateSchedulersForRampUp(ScheduledExecutorService executorService, ResponseStatistics responseStatistics) {
        List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
        long oneSecInNs = TimeUnit.SECONDS.toNanos(1);

        for (int i = 0; i < maxRequestsPerSecond; i++) {
            long initialDelayInNs = i * (TimeUnit.MILLISECONDS.toNanos(rampUpTimeInMs) / maxRequestsPerSecond);
            TesterRunner runner = new TesterRunner(request, responseStatistics);
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

    public void setMaxRequestsPerSecond(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
    }

    public void setRampUpTimeInMs(long rampUpTimeInMs) {
        this.rampUpTimeInMs = rampUpTimeInMs;
    }

    public void setRampDownTimeInMs(long rampDownTimeInMs) {
        this.rampDownTimeInMs = rampDownTimeInMs;
    }

    public void setDurationInMs(long durationInMs) {
        this.durationInMs = durationInMs;
    }
}

