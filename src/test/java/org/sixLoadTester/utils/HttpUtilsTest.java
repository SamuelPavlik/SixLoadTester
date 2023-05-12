package org.sixLoadTester.utils;

import org.apache.http.HeaderIterator;
import org.apache.http.client.methods.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.exceptions.UnhandledHttpMethodException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenGivenGetRequestThenCreateHttpGetRequestWithRightParams() throws UnhandledHttpMethodException {
        var request = new Request(HttpMethod.GET, "endpoint", "");
        HttpRequestBase httpRequest = HttpUtils.createHttpRequest(request);

        assertInstanceOf(HttpGet.class, httpRequest);
        assertEquals(request.endpoint, httpRequest.getURI().toString());

        HeaderIterator headerIterator = httpRequest.headerIterator("Content-Type");
        assertTrue(headerIterator.hasNext());
        assertEquals("application/json", headerIterator.nextHeader().getValue());
    }

    @Test
    void whenGivenPostRequestThenCreateHttpGetRequestWithRightParams() throws UnhandledHttpMethodException, IOException {
        var request = new Request(HttpMethod.POST, "endpoint", "{\"name\": \"random\"}");
        HttpRequestBase httpRequest = HttpUtils.createHttpRequest(request);

        assertInstanceOf(HttpPost.class, httpRequest);
        assertEquals(request.endpoint, httpRequest.getURI().toString());

        HeaderIterator headerIterator = httpRequest.headerIterator("Content-Type");
        HttpPost httpPost = (HttpPost) httpRequest;
        assertEquals(request.body, new String(httpPost.getEntity().getContent().readAllBytes()));
        assertTrue(headerIterator.hasNext());
        assertEquals("application/json", headerIterator.nextHeader().getValue());
    }

    @Test
    void whenGivenPutRequestThenCreateHttpGetRequestWithRightParams() throws UnhandledHttpMethodException, IOException {
        var request = new Request(HttpMethod.PUT, "endpoint", "{\"name\": \"random\"}");
        HttpRequestBase httpRequest = HttpUtils.createHttpRequest(request);

        assertInstanceOf(HttpPut.class, httpRequest);
        assertEquals(request.endpoint, httpRequest.getURI().toString());

        HeaderIterator headerIterator = httpRequest.headerIterator("Content-Type");
        HttpPut httpPost = (HttpPut) httpRequest;
        assertEquals(request.body, new String(httpPost.getEntity().getContent().readAllBytes()));
        assertTrue(headerIterator.hasNext());
        assertEquals("application/json", headerIterator.nextHeader().getValue());
    }

    @Test
    void whenGivenDeleteRequestThenCreateHttpGetRequestWithRightParams() throws UnhandledHttpMethodException {
        var request = new Request(HttpMethod.DELETE, "endpoint", "");
        HttpRequestBase httpRequest = HttpUtils.createHttpRequest(request);

        assertInstanceOf(HttpDelete.class, httpRequest);
        assertEquals(request.endpoint, httpRequest.getURI().toString());

        HeaderIterator headerIterator = httpRequest.headerIterator("Content-Type");
        assertTrue(headerIterator.hasNext());
        assertEquals("application/json", headerIterator.nextHeader().getValue());
    }
}