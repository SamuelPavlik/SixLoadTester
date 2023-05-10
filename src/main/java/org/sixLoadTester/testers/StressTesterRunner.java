package org.sixLoadTester.testers;

import org.apache.http.impl.client.HttpClientBuilder;
import org.sixLoadTester.exceptions.UnhandledHttpMethodException;
import org.sixLoadTester.utils.HttpUtils;
import org.sixLoadTester.utils.RequestData;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StressTesterRunner implements Runnable {

    private final AtomicInteger errorCount;
    private final AtomicInteger overallCount;
    private final List<Long> responseTimes;
    private final RequestData requestData;

    private boolean init;

    public StressTesterRunner(RequestData requestData, AtomicInteger errorCount, AtomicInteger overallCount, List<Long> responseTimes) {
        this.requestData = requestData;
        this.errorCount = errorCount;
        this.overallCount = overallCount;
        this.responseTimes = responseTimes;
        this.init = false;
    }

    @Override
    public void run() {
        if (!init) {
            init = true;
            overallCount.getAndIncrement();
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
            errorCount.incrementAndGet();
            System.err.println("Request failed: " + e.getMessage());
        } catch (UnhandledHttpMethodException e) {
            System.err.println("Unhandled HTTP method: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var endTime = System.currentTimeMillis();
        synchronized (this) {
            responseTimes.add(endTime - startTime);
        }
    }
}
