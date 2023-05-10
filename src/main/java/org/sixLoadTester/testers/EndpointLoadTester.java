package org.sixLoadTester.testers;

import org.sixLoadTester.exceptions.NoDataAvailableException;
import org.sixLoadTester.utils.RequestData;
import org.sixLoadTester.utils.StatisticsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EndpointLoadTester extends EndpointTester {

    private int maxRequestsPerSecond = 400;
    private int rampUpTimeInMiliseconds = 5000;
    private int rampDownTimeInMiliseconds = 5000;
    private int durationInMiliseconds = 10000;

    private List<Long> responseTimes;

    public EndpointLoadTester(RequestData requestData) {
        super(requestData);
    }

    public void execute() throws InterruptedException {
        if (maxRequestsPerSecond <= 0)
            return;

        var executorService = Executors.newScheduledThreadPool(maxRequestsPerSecond);
        List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
        List<Long> localResponseTimes = new ArrayList<>();

        System.out.println("Load test initiated");
        System.out.println("Ramp up initiated");

        for (int i = 0; i < maxRequestsPerSecond; i++) {
            long initialDelay = (long) (i * (((float) rampUpTimeInMiliseconds) / maxRequestsPerSecond));
            scheduledFutures.add(executorService.scheduleAtFixedRate(() -> runThread(localResponseTimes),
                    initialDelay, 1000, TimeUnit.MILLISECONDS));
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
        responseTimes = localResponseTimes;
    }

    @Override
    public void produceStatistics() throws NoDataAvailableException {
        if (responseTimes.isEmpty())
            throw new NoDataAvailableException();

        StatisticsUtils.createChart(responseTimes);
        StatisticsUtils.calculateStatistics(responseTimes, durationInMiliseconds +
                rampDownTimeInMiliseconds + rampUpTimeInMiliseconds);
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

