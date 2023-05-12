package org.sixLoadTester.testers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.data.ResponseStatistics;

import static org.junit.jupiter.api.Assertions.*;

class TesterRunnerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenEndpointValidThenExpectErrorCount0AndNoExceptions() {
        Request request = new Request(HttpMethod.GET, "https://jsonplaceholder.typicode.com/posts", "");
        ResponseStatistics responseStatistics = new ResponseStatistics();
        TesterRunner runner = new TesterRunner(request, responseStatistics);

        runner.run();

        assertEquals(0, responseStatistics.errorCount.get());
    }

    @Test
    void whenEndpointInvalidThenExceptionThrown() {
        Request request = new Request(HttpMethod.GET, "invalid/api", "");
        ResponseStatistics responseStatistics = new ResponseStatistics();
        TesterRunner runner = new TesterRunner(request, responseStatistics);

        assertThrows(RuntimeException.class, runner::run);
    }
}