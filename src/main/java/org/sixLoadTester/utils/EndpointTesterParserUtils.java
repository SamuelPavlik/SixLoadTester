package org.sixLoadTester.utils;

import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.exceptions.InvalidNumberOfArgsException;
import org.sixLoadTester.exceptions.InvalidTestNameException;
import org.sixLoadTester.testers.EndpointLoadTester;
import org.sixLoadTester.testers.EndpointStressTester;
import org.sixLoadTester.testers.EndpointTester;

public class EndpointTesterParserUtils {

    private static final int STRESS_TESTER_MIN_NUM_OF_ARGS = 5;
    private static final int LOAD_TESTER_MIN_NUM_OF_ARGS = 8;
    private static final int TESTER_TYPE_INDEX = 0;
    private static final int HTTP_METHOD_INDEX = 1;
    private static final int ENDPOINT_INDEX = 2;
    private static final int REQUEST_BODY_INDEX = 3;
    private static final int RAMP_UP_INDEX = 4;
    private static final int INCREASE_IN_REQUESTS_INDEX = 4;
    private static final int DURATION_INDEX = 5;
    private static final int RAMP_DOWN_INDEX = 6;
    private static final int MAX_REQUESTS_PER_S_INDEX = 7;
    private static final String LOAD_TEST_TYPE = "load";
    private static final String STRESS_TEST_TYPE = "stress";

    public static EndpointTester parseArgs(String[] args) throws IllegalArgumentException {
        int minNumOfArgs = Math.min(STRESS_TESTER_MIN_NUM_OF_ARGS, LOAD_TESTER_MIN_NUM_OF_ARGS);
        if (args.length < minNumOfArgs) {
            throw new InvalidNumberOfArgsException();
        }

        RequestData requestData = new RequestData(
                HttpMethod.valueOf(args[HTTP_METHOD_INDEX].toUpperCase()),
                args[ENDPOINT_INDEX],
                args[REQUEST_BODY_INDEX]);

        if (args[TESTER_TYPE_INDEX].equalsIgnoreCase(LOAD_TEST_TYPE)) {
            return getLoadTester(args, requestData);
        } else if (args[TESTER_TYPE_INDEX].equalsIgnoreCase(STRESS_TEST_TYPE)) {
            return getStressTester(args, requestData);
        } else {
            throw new InvalidTestNameException();
        }
    }

    private static EndpointStressTester getStressTester(String[] args, RequestData requestData) {
        if (args.length < STRESS_TESTER_MIN_NUM_OF_ARGS) {
            throw new InvalidNumberOfArgsException();
        }

        EndpointStressTester stressTester = new EndpointStressTester(requestData);
        stressTester.setIncreaseInRequestsPerSecond(Integer.parseInt(args[INCREASE_IN_REQUESTS_INDEX]));
        return stressTester;
    }

    private static EndpointLoadTester getLoadTester(String[] args, RequestData requestData) {
        if (args.length < LOAD_TESTER_MIN_NUM_OF_ARGS) {
            throw new InvalidNumberOfArgsException();
        }

        EndpointLoadTester loadTester = new EndpointLoadTester(requestData);
        loadTester.setRampUpTimeInMs(Integer.parseInt(args[RAMP_UP_INDEX]));
        loadTester.setDurationInMs(Integer.parseInt(args[DURATION_INDEX]));
        loadTester.setRampDownTimeInMs(Integer.parseInt(args[RAMP_DOWN_INDEX]));
        loadTester.setMaxRequestsPerSecond(Integer.parseInt(args[MAX_REQUESTS_PER_S_INDEX]));
        return loadTester;
    }

}
