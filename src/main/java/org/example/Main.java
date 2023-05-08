package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
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