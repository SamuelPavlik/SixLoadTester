package org.sixLoadTester.testers;

import org.sixLoadTester.data.Request;
import org.sixLoadTester.data.ResponseStatistics;
import org.sixLoadTester.utils.StatisticsUtils;

public abstract class EndpointTester {

    Request request;

    public EndpointTester(Request request) {
        this.request = request;
    }

    public void execute(boolean withChart) throws InterruptedException {
        runTestRequest();
        executeInternal(withChart);
    }

    public void execute() throws InterruptedException {
        execute(false);
    }

    protected abstract void executeInternal(boolean withChart) throws InterruptedException;

    private void runTestRequest() {
        ResponseStatistics responseStatistics = new ResponseStatistics();
        TesterRunner runner = new TesterRunner(request, responseStatistics);
        runner.run();

        if (responseStatistics.errorCount.get() > 0)
        {
            throw new RuntimeException("Unable to establish connection with the endpoint "
                    + request.method.name() + " " + request.endpoint);
        }
    }

    protected static void produceStatistics(Request request, ResponseStatistics responseStatistics, boolean withChart) {
        StatisticsUtils.calculateStatistics(responseStatistics);

        if (withChart) {
            StatisticsUtils.createChart(request, responseStatistics.responseTimes);
        }
    }
}
