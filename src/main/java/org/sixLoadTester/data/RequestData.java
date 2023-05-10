package org.sixLoadTester.data;

public class RequestData {
    public HttpMethod method;
    public String endpoint;
    public String jsonData;

    public RequestData(HttpMethod method, String endpoint, String jsonData) {
        this.method = method;
        this.endpoint = endpoint;
        this.jsonData = jsonData;
    }

    public RequestData(HttpMethod method, String endpoint) {
        this(method, endpoint, "");
    }
}
