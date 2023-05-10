package org.sixLoadTester.testers;

import org.sixLoadTester.data.ResponseData;
import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.utils.StatisticsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EndpointLoadTester extends EndpointTester {

    private static final int DEFAULT_MAX_REQUESTS_PER_SECOND = 400;
    private static final int DEFAULT_RAMP_UP_TIME_IN_MS = 5000;
    private static final int DEFAULT_RAMP_DOWN_TIME_IN_MS = 5000;
    private static final int DEFAULT_DURATION_IN_MS = 10000;

    private int maxRequestsPerSecond = DEFAULT_MAX_REQUESTS_PER_SECOND;
    private int rampUpTimeInMiliseconds = DEFAULT_RAMP_UP_TIME_IN_MS;
    private int rampDownTimeInMiliseconds = DEFAULT_RAMP_DOWN_TIME_IN_MS;
    private int durationInMiliseconds = DEFAULT_DURATION_IN_MS;

    public EndpointLoadTester(RequestData requestData) {
        super(requestData);
    }

    public void execute() throws InterruptedException {
        if (maxRequestsPerSecond <= 0)
            return;

        var executorService = Executors.newScheduledThreadPool(maxRequestsPerSecond);
        List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
        ResponseData responseData = new ResponseData();

        System.out.println("Load test initiated");
        System.out.println("Ramp up initiated");

        for (int i = 0; i < maxRequestsPerSecond; i++) {
            long initialDelay = (long) (i * (((float) rampUpTimeInMiliseconds) / maxRequestsPerSecond));
            TesterRunner runner = new TesterRunner(requestData, responseData);
            scheduledFutures.add(executorService.scheduleAtFixedRate(runner, initialDelay, 1000, TimeUnit.MILLISECONDS));
        }

        Thread.sleep(rampUpTimeInMiliseconds);
        System.out.println("Ramp up finished");

        Thread.sleep(durationInMiliseconds);
        System.out.println("Ramp down initiated");

        long pauseBetweenCancels = rampDownTimeInMiliseconds / maxRequestsPerSecond;
        for (int i = 0; i < maxRequestsPerSecond; i++) {
            scheduledFutures.get(i).cancel(false);
            Thread.sleep(pauseBetweenCancels);
        }

        System.out.println("Ramp down finished");

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        produceStatistics(responseData, durationInMiliseconds + rampDownTimeInMiliseconds + rampUpTimeInMiliseconds);
    }

    private void produceStatistics(ResponseData responseData, int totalDurationInMs) {
        StatisticsUtils.calculateStatistics(responseData, totalDurationInMs);
        StatisticsUtils.createChart(responseData.responseTimes);
    }

    public void setMaxRequestsPerSecond(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
    }

    public void setRampUpTimeInMiliseconds(int rampUpTimeInMiliseconds) {
        this.rampUpTimeInMiliseconds = rampUpTimeInMiliseconds;
    }

    public void setRampDownTimeInMiliseconds(int rampDownTimeInMiliseconds) {
        this.rampDownTimeInMiliseconds = rampDownTimeInMiliseconds;
    }

    public void setDurationInMiliseconds(int durationInMiliseconds) {
        this.durationInMiliseconds = durationInMiliseconds;
    }
}

