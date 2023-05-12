package org.sixLoadTester.testers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.exceptions.NegativeNumberArgumentException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EndpointLoadTesterTest {

    Request request;

    @BeforeEach
    void setUp() {
        //given api should be always available
        this.request = new Request(HttpMethod.GET, "https://jsonplaceholder.typicode.com/posts", "");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenGivenInvalidApiThenThrowRuntimeException() {
        var request = new Request(HttpMethod.GET, "invalid/api", "");
        EndpointLoadTester endpointLoadTester = new EndpointLoadTester(request);
        endpointLoadTester.setMaxRequestsPerSecond(50);

        assertThrows(RuntimeException.class, endpointLoadTester::execute);
    }

    @Test
    void whenMaxRequestsPerSecondBelow0ThenThrowException() {
        EndpointLoadTester endpointLoadTester = new EndpointLoadTester(request);
        endpointLoadTester.setMaxRequestsPerSecond(-1);
        assertThrows(NegativeNumberArgumentException.class, endpointLoadTester::execute);
    }

    @Test
    void whenRampUpTimeBelow0ThenThrowException() {
        EndpointLoadTester endpointLoadTester = new EndpointLoadTester(request);
        endpointLoadTester.setRampUpTimeInMs(-1);
        assertThrows(NegativeNumberArgumentException.class, endpointLoadTester::execute);
    }

    @Test
    void whenRampDownTimeBelow0ThenThrowException() {
        EndpointLoadTester endpointLoadTester = new EndpointLoadTester(request);
        endpointLoadTester.setRampDownTimeInMs(-1);
        assertThrows(NegativeNumberArgumentException.class, endpointLoadTester::execute);
    }

    @Test
    void whenDurationBelow0ThenThrowException() {
        EndpointLoadTester endpointLoadTester = new EndpointLoadTester(request);
        endpointLoadTester.setDurationInMs(-1);
        assertThrows(NegativeNumberArgumentException.class, endpointLoadTester::execute);
    }
}