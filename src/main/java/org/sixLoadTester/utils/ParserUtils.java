package org.sixLoadTester.utils;

import org.sixLoadTester.data.HttpMethod;
import org.sixLoadTester.data.RequestData;
import org.sixLoadTester.exceptions.InvalidNumberOfArgsException;
import org.sixLoadTester.exceptions.InvalidTestNameException;
import org.sixLoadTester.testers.EndpointLoadTester;
import org.sixLoadTester.testers.EndpointStressTester;
import org.sixLoadTester.testers.EndpointTester;

public class ParserUtils {
    public static EndpointTester parseArgs(String[] args) throws IllegalArgumentException {
        int minNumOfArgs = 5;
        if (args.length < minNumOfArgs) {
            throw new InvalidNumberOfArgsException();
        }

        RequestData requestData = new RequestData(HttpMethod.valueOf(args[1].toUpperCase()), args[2], args[3]);

        if (args[0].equalsIgnoreCase("load")) {
            return getLoadTester(args, requestData);
        } else if (args[0].equalsIgnoreCase("stress")) {
            return getStressTester(args, requestData);
        } else {
            throw new InvalidTestNameException();
        }
    }

    private static EndpointStressTester getStressTester(String[] args, RequestData requestData) {
        int stressTesterMinNumOfArgs = 5;
        if (args.length < stressTesterMinNumOfArgs) {
            throw new InvalidNumberOfArgsException();
        }

        EndpointStressTester stressTester = new EndpointStressTester(requestData);
        stressTester.setIncreaseInRequestsPerSecond(Integer.parseInt(args[4]));
        return stressTester;
    }

    private static EndpointLoadTester getLoadTester(String[] args, RequestData requestData) {
        int loadTesterMinNumOfArgs = 8;
        if (args.length < loadTesterMinNumOfArgs) {
            throw new InvalidNumberOfArgsException();
        }

        EndpointLoadTester loadTester = new EndpointLoadTester(requestData);
        loadTester.setRampUpTimeInMiliseconds(Integer.parseInt(args[4]));
        loadTester.setDurationInMiliseconds(Integer.parseInt(args[5]));
        loadTester.setRampDownTimeInMiliseconds(Integer.parseInt(args[6]));
        loadTester.setMaxRequestsPerSecond(Integer.parseInt(args[7]));
        return loadTester;
    }

}
