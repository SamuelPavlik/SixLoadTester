package org.example;

public class Main {

    public static void main(String[] args) throws InterruptedException, UnhandledHttpMethodException {
        String jsonData = "{\"name\":\"Product\",\"price\":10.0}";
        String endpoint = "http://localhost:8080/products";
//        EndpointLoadTester loadTester = new EndpointLoadTester(endpoint, HttpMethod.POST, jsonData);
//        loadTester.setRampUpTimeInMiliseconds(2000);
//        loadTester.setDurationInMiliseconds(10000);
//        loadTester.setRampDownTimeInMiliseconds(2000);
//        loadTester.setMaxRequestsPerSecond(4000);
//
//        loadTester.execute();
//
//        loadTester.produceStatistics();

        EndpointStressTester stressTester = new EndpointStressTester(endpoint, HttpMethod.POST, jsonData);
        stressTester.setIncreaseInRequestsPerSecond(20);

        stressTester.execute();

        try {
            stressTester.produceStatistics();
        } catch (NoDataAvailableException e) {
            throw new RuntimeException(e);
        }
    }


}