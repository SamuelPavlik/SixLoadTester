package org.sixLoadTester.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.Request;
import org.sixLoadTester.exceptions.InvalidNumberOfArgsException;
import org.sixLoadTester.testers.EndpointLoadTester;
import org.sixLoadTester.testers.EndpointStressTester;
import org.sixLoadTester.testers.EndpointTester;

import static org.junit.jupiter.api.Assertions.*;

class EndpointTesterParserUtilsTest {

    Request request;

    @BeforeEach
    void setUp() {
        this.request = new Request(HttpMethod.GET, "https://jsonplaceholder.typicode.com/posts", "");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenGivenLessArgsThanMinExpectedThenThrowInvalidNumberOfArgsException() {
        assertThrows(InvalidNumberOfArgsException.class, () -> EndpointTesterParserUtils.parseArgs(new String[1]));
    }

    @Test
    void whenGivenValidArgsForLoadTestShouldReturnLoadTester() {
        String[] args = {"load", "GET", "https://jsonplaceholder.typicode.com/posts", "", "2000", "10000", "2000", "400"};
        EndpointTester endpointTester = EndpointTesterParserUtils.parseArgs(args);

        assertInstanceOf(EndpointLoadTester.class, endpointTester);
    }

    @Test
    void whenGivenInvalidNumberOfArgsForLoadTestShouldThrowInvalidNumberOfArgsException() {
        String[] args = {"load", "GET", "https://jsonplaceholder.typicode.com/posts", "", "less", "than", "expected"};
        assertThrows(InvalidNumberOfArgsException.class, () -> EndpointTesterParserUtils.parseArgs(args));
    }

    @Test
    void whenGivenValidArgsForStressTestShouldReturnStressTester() {
        String[] args = {"stress", "GET", "https://jsonplaceholder.typicode.com/posts", "", "50"};
        EndpointTester endpointTester = EndpointTesterParserUtils.parseArgs(args);

        assertInstanceOf(EndpointStressTester.class, endpointTester);
    }

    @Test
    void whenGivenInvalidNumberOfArgsForStressTestShouldThrowInvalidNumberOfArgsException() {
        String[] args = {"stress", "GET", "https://jsonplaceholder.typicode.com/posts", "0"};
        assertThrows(InvalidNumberOfArgsException.class, () -> EndpointTesterParserUtils.parseArgs(args));
    }

    @Test
    void whenGivenInvalidNumberInputShouldThrowNumberFormatException() {
        String[] args = {"stress", "GET", "https://jsonplaceholder.typicode.com/posts", "0", "more", "than", "expected"};
        assertThrows(NumberFormatException.class, () -> EndpointTesterParserUtils.parseArgs(args));
    }
}