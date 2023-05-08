package org.example;

import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EndpointLoadTester {

    private final HttpMethod method;
    private final String endpoint;
    private String jsonData;

    private int maxRequestsPerSecond = 400;
    private int rampUpTimeInMiliseconds = 5000;
    private int rampDownTimeInMiliseconds = 5000;
    private int durationInMiliseconds = 10000;

    public EndpointLoadTester(String endpoint, HttpMethod method) {
        this.endpoint = endpoint;
        this.method = method;
        this.jsonData = "";
    }

    public EndpointLoadTester(String endpoint, HttpMethod method, String jsonData) {
        this(endpoint, method);
        this.jsonData = jsonData;
    }

    public List<Long> execute() throws InterruptedException {
        if (maxRequestsPerSecond <= 0)
            return new ArrayList<>();

        var executorService = Executors.newScheduledThreadPool(maxRequestsPerSecond);
        List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
        List<Long> responseTimes = new ArrayList<>();

        System.out.println("Ramp up initiated");

        for (int i = 0; i < maxRequestsPerSecond; i++) {
            long initialDelay = (long) (i * (((float) rampUpTimeInMiliseconds) / maxRequestsPerSecond));
            scheduledFutures.add(executorService.scheduleAtFixedRate(() -> runThread(responseTimes),
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

        return responseTimes;
    }

    private void runThread(List<Long> responseTimes) {
        var httpClient = HttpClientBuilder.create().build();
        var startTime = System.currentTimeMillis();
        try {
            var request = HttpUtils.createHttpRequest(endpoint, method, jsonData);
            var response = httpClient.execute(request);
            var entity = response.getEntity();
            if (entity != null) {
                // Discard the response content
                entity.getContent().close();
            }
        } catch (IOException e) {
            System.err.println("Request failed: " + e.getMessage());
        } catch (UnhandledHttpMethodException e) {
            System.err.println("Unhandled HTTP method: " + e.getMessage());
        }
        var endTime = System.currentTimeMillis();
        responseTimes.add(endTime - startTime);
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

    public int getRampUpTimeInMiliseconds() {
        return rampUpTimeInMiliseconds;
    }

    public int getRampDownTimeInMiliseconds() {
        return rampDownTimeInMiliseconds;
    }

    public int getDurationInMiliseconds() {
        return durationInMiliseconds;
    }
}

