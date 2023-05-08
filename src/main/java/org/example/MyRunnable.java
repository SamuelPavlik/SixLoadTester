package org.example;

import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

public class MyRunnable implements Runnable {

    private final AtomicInteger errorCount;

    private final AtomicInteger overallCount;

    private boolean init;

    public MyRunnable(AtomicInteger errorCount, AtomicInteger overallCount) {
        this.errorCount = errorCount;
        this.overallCount = overallCount;
        this.init = false;
    }

    @Override
    public void run() {
        if (!init) {
            init = true;
            System.out.println(overallCount.incrementAndGet());
        }

        var httpClient = HttpClientBuilder.create().build();
//        var startTime = System.currentTimeMillis();
        try {
            String jsonData = "{\"name\":\"Product\",\"price\":10.0}";
            String endpoint = "http://localhost:8080/products";
            var request = HttpUtils.createHttpRequest(endpoint, HttpMethod.POST, jsonData);
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
//        var endTime = System.currentTimeMillis();
    }
}
