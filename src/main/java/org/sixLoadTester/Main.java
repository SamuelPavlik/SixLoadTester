package org.sixLoadTester;

import org.sixLoadTester.testers.EndpointTester;
import org.sixLoadTester.utils.EndpointTesterParserUtils;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        EndpointTester tester = EndpointTesterParserUtils.parseArgs(args);
        tester.execute();
    }
}