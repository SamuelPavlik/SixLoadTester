package org.sixLoadTester.testers;

import org.apache.http.impl.client.HttpClientBuilder;
import org.sixLoadTester.utils.HttpUtils;
import org.sixLoadTester.exceptions.NoDataAvailableException;
import org.sixLoadTester.exceptions.UnhandledHttpMethodException;
import org.sixLoadTester.utils.RequestData;

import java.io.IOException;
import java.util.List;

public abstract class EndpointTester {

    RequestData requestData;

    public EndpointTester(RequestData requestData) {
        this.requestData = requestData;
    }

    public abstract void execute() throws InterruptedException;

    public abstract void produceStatistics() throws NoDataAvailableException;

    protected void runThread(List<Long> responseTimes) {
        var httpClient = HttpClientBuilder.create().build();
        var startTime = System.currentTimeMillis();
        try {
            var request = HttpUtils.createHttpRequest(requestData);
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
}
