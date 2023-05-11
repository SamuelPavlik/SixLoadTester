package org.sixLoadTester.testers;

import org.sixLoadTester.data.Request;
import org.sixLoadTester.data.ResponseStatistics;
import org.sixLoadTester.utils.StatisticsUtils;

public abstract class EndpointTester {

    Request request;

    public EndpointTester(Request request) {
        this.request = request;
    }

    protected static void produceStatistics(Request request, ResponseStatistics responseStatistics) {
        StatisticsUtils.calculateStatistics(responseStatistics);
        StatisticsUtils.createChart(request, responseStatistics.responseTimes);
    }

    public abstract void execute() throws InterruptedException;
}
