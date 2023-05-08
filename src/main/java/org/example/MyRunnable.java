package org.example;

import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class MyRunnable implements Runnable {


    static int count = 0;

    static int errorCount = 0;
    boolean init = false;

    @Override
    public void run() {
        if (!init) {
            init = true;
            System.out.println(++count);
        }

        var httpClient = HttpClientBuilder.create().build();
        var startTime = System.currentTimeMillis();
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
        } catch (IOException e) {
            errorCount++;
            System.err.println("Request failed: " + e.getMessage());
        } catch (UnhandledHttpMethodException e) {
            System.err.println("Unhandled HTTP method: " + e.getMessage());
        }
        var endTime = System.currentTimeMillis();
    }
}
