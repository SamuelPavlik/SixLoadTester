package org.sixLoadTester.testers;

import org.sixLoadTester.data.Request;
import org.sixLoadTester.data.ResponseStatistics;
import org.sixLoadTester.exceptions.NegativeNumberArgumentException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EndpointStressTester extends EndpointTester {

    private static final int MAX_THREADS = 10000;
    private static final int MAX_ERRORS = 10;
    private static final int NUM_UPDATE_FREQUENCY_IN_MS = 100;

    private int increaseInRequestsPerSecond;

    public EndpointStressTester(Request request) {
        super(request);
    }

    @Override
    public void execute() throws InterruptedException {
        System.out.println("Stress test initiated");

        if (increaseInRequestsPerSecond <= 0)
        {
            throw new NegativeNumberArgumentException();
        }

        System.setErr(null);

        var executorService = Executors.newScheduledThreadPool(MAX_THREADS);
        var responseData = new ResponseStatistics();
        initiateSchedulersForRampUp(executorService, responseData);

        waitForReachingStressPoint(responseData);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        produceStatistics(request, responseData);
    }

    private static void waitForReachingStressPoint(ResponseStatistics responseStatistics) throws InterruptedException {
        while (responseStatistics.errorCount.get() < MAX_ERRORS) {
            Thread.sleep(NUM_UPDATE_FREQUENCY_IN_MS);
            System.out.print("\rNumber of requests per second: " + responseStatistics.maxRequestsPerSecond.get());
        }
    }

    private void initiateSchedulersForRampUp(ScheduledExecutorService executorService, ResponseStatistics responseStatistics) {
        long delayBetweenRequestsInNs = TimeUnit.SECONDS.toNanos(1) / increaseInRequestsPerSecond;
        long oneSecInNs = TimeUnit.SECONDS.toNanos(1);

        for (int i = 0; i < MAX_THREADS; i++) {
            long initialDelayInNs = i * delayBetweenRequestsInNs;
            var runner = new TesterRunner(request, responseStatistics);
            executorService.scheduleAtFixedRate(runner, initialDelayInNs, oneSecInNs, TimeUnit.NANOSECONDS);
        }
    }

    public void setIncreaseInRequestsPerSecond(int increaseInRequestsPerSecond) {
        this.increaseInRequestsPerSecond = increaseInRequestsPerSecond;
    }
}
