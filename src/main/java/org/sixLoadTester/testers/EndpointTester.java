package org.sixLoadTester.testers;

import org.sixLoadTester.data.RequestData;

public abstract class EndpointTester {

    RequestData requestData;

    public EndpointTester(RequestData requestData) {
        this.requestData = requestData;
    }

    public abstract void execute() throws InterruptedException;
}
