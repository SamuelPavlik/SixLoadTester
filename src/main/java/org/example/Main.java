package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    private int errorCount = 0;

    public static void main(String[] args) throws InterruptedException, UnhandledHttpMethodException {
        String jsonData = "{\"name\":\"Product\",\"price\":10.0}";
        String endpoint = "http://localhost:8080/products";
        EndpointLoadTester loadTester = new EndpointLoadTester(endpoint, HttpMethod.POST, jsonData);
        loadTester.setRampUpTimeInMiliseconds(2000);
        loadTester.setDurationInMiliseconds(10000);
        loadTester.setRampDownTimeInMiliseconds(2000);
        loadTester.setMaxRequestsPerSecond(4000);

        List<Long> responseTimes = loadTester.execute();

        StatisticsUtils.createChart(responseTimes);
        StatisticsUtils.calculateStatistics(responseTimes, loadTester.getDurationInMiliseconds() +
                loadTester.getRampDownTimeInMiliseconds() + loadTester.getRampUpTimeInMiliseconds());
    }


}