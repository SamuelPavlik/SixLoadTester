package org.sixLoadTester.data;

public class Request {
    public HttpMethod method;
    public String endpoint;
    public String body;

    public Request(HttpMethod method, String endpoint, String body) {
        this.method = method;
        this.endpoint = endpoint;
        this.body = body;
    }
}
