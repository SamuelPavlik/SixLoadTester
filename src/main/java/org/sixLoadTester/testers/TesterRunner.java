package org.sixLoadTester.testers;

import org.apache.http.impl.client.HttpClientBuilder;
import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.data.ResponseData;
import org.sixLoadTester.exceptions.UnhandledHttpMethodException;
import org.sixLoadTester.utils.HttpUtils;

import java.io.IOException;
import java.net.SocketException;

public class TesterRunner implements Runnable {

    private final ResponseData responseData;
    private final RequestData requestData;

    private boolean hasBeenRun;

    public TesterRunner(RequestData requestData, ResponseData responseData) {
        this.requestData = requestData;
        this.responseData = responseData;
        this.hasBeenRun = false;
    }

    @Override
    public void run() {
        if (!hasBeenRun) {
            hasBeenRun = true;
            responseData.maxRequestsPerSecond.getAndIncrement();
        }

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
        } catch (SocketException e) {
            responseData.errorCount.incrementAndGet();
            System.err.println("Request failed: " + e.getMessage());
        } catch (UnhandledHttpMethodException e) {
            System.err.println("Unhandled HTTP method: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var endTime = System.currentTimeMillis();

        synchronized (this) {
            responseData.responseTimes.add(endTime - startTime);
        }
    }
}
