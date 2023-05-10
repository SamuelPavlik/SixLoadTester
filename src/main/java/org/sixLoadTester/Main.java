package org.sixLoadTester;

import org.sixLoadTester.testers.EndpointTester;
import org.sixLoadTester.utils.ParserUtils;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        EndpointTester tester = ParserUtils.parseArgs(args);
        tester.execute();
    }
}