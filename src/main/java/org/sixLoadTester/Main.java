package org.sixLoadTester;

import org.sixLoadTester.exceptions.NoDataAvailableException;
import org.sixLoadTester.testers.EndpointTester;
import org.sixLoadTester.utils.ParserUtils;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        EndpointTester tester = ParserUtils.parseArgs(args);
        tester.execute();
        try {
            tester.produceStatistics();
        } catch (NoDataAvailableException e) {
            throw new RuntimeException(e);
        }
//        String jsonData = "{\"name\":\"Product\",\"price\":10.0}";
//        String endpoint = "http://localhost:8080/products";
//        EndpointLoadTester loadTester = new EndpointLoadTester(endpoint, HttpMethod.POST, jsonData);
//        loadTester.setRampUpTimeInMiliseconds(2000);
//        loadTester.setDurationInMiliseconds(10000);
//        loadTester.setRampDownTimeInMiliseconds(2000);
//        loadTester.setMaxRequestsPerSecond(400);
//
//        loadTester.execute();
//
//        loadTester.produceStatistics();

//        EndpointStressTester stressTester = new EndpointStressTester(new RequestData(HttpMethod.POST, endpoint, jsonData));
//        stressTester.setIncreaseInRequestsPerSecond(20);
//
//        stressTester.execute();
//
//        try {
//            stressTester.produceStatistics();
//        } catch (NoDataAvailableException e) {
//            throw new RuntimeException(e);
//        }
    }
}