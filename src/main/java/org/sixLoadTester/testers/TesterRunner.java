package org.sixLoadTester.testers;

import org.apache.http.impl.client.HttpClientBuilder;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.data.ResponseStatistics;
import org.sixLoadTester.exceptions.UnhandledHttpMethodException;
import org.sixLoadTester.utils.HttpUtils;

import java.io.IOException;
import java.net.SocketException;

public class TesterRunner implements Runnable {

    private final ResponseStatistics responseStatistics;
    private final Request request;

    private boolean hasBeenRun;

    public TesterRunner(Request request, ResponseStatistics responseStatistics) {
        this.request = request;
        this.responseStatistics = responseStatistics;
        this.hasBeenRun = false;
    }

    @Override
    public void run() {
        if (!hasBeenRun) {
            hasBeenRun = true;
            responseStatistics.maxRequestsPerSecond.getAndIncrement();
        }

        var httpClient = HttpClientBuilder.create().build();
        var startTime = System.currentTimeMillis();

        try {
            var request = HttpUtils.createHttpRequest(this.request);
            var response = httpClient.execute(request);
            var entity = response.getEntity();
            if (entity != null) {
                // Discard the response content
                entity.getContent().close();
            }
        } catch (SocketException e) {
            responseStatistics.errorCount.incrementAndGet();
            System.err.println("Request to " + request.endpoint + " failed: " + e.getMessage());
        } catch (UnhandledHttpMethodException e) {
            System.err.println("Unhandled HTTP method " + request.method + " in " + request.endpoint + " : " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var endTime = System.currentTimeMillis();

        synchronized (this) {
            responseStatistics.responseTimes.add(endTime - startTime);
        }
    }
}
