package org.sixLoadTester.testers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.exceptions.NegativeNumberArgumentException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EndpointStressTesterTest {

    Request request;

    @BeforeEach
    void setUp() {
        this.request = new Request(HttpMethod.GET, "https://jsonplaceholder.typicode.com/posts", "");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenGivenInvalidApiThenThrowRuntimeException() {
        var request = new Request(HttpMethod.GET, "invalid/api", "");
        EndpointStressTester endpointStressTester = new EndpointStressTester(request);
        endpointStressTester.setIncreaseInRequestsPerSecond(50);

        assertThrows(RuntimeException.class, endpointStressTester::execute);
    }

    @Test
    void whenMaxRequestsPerSecondBelow0ThenThrowException() {
        EndpointStressTester endpointStressTester = new EndpointStressTester(request);
        endpointStressTester.setIncreaseInRequestsPerSecond(-1);
        assertThrows(NegativeNumberArgumentException.class, endpointStressTester::execute);
    }
}