package org.sixLoadTester.testers;

import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.data.ResponseData;
import org.sixLoadTester.utils.StatisticsUtils;

public abstract class EndpointTester {

    RequestData requestData;

    public EndpointTester(RequestData requestData) {
        this.requestData = requestData;
    }

    protected static void produceStatistics(ResponseData responseData) {
        StatisticsUtils.calculateStatistics(responseData);
        StatisticsUtils.createChart(responseData.responseTimes);
    }

    public abstract void execute() throws InterruptedException;
}
