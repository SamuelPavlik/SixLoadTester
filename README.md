# SixLoadTester
Command line tool for performance tests of REST endpoints.

## Build
To build use ```mvn clean package```. The resulting jar file should be created in the target folder.

## Execution
There are 2 types of performance tests you can perform with the tool: load tests and stress tests.

To perform load tests following arguments need to be specified in this order:
1. "load"
2. http method - get/post/put/delete
3. endpoint
4. request body
5. ramp up time in ms
6. duration of the post-ramp up period in ms
7. ramp down time in ms
8. number of requests to reach per second

For example: ```java -jar target/SixLoadTester-SNAPSHOT.jar load get https://localhost:8080/products {} 2000 10000 2000 400```

To perform stress tests following arguments need to be specified in this order:
1. "stress"
2. http method - get/post/put/delete
3. endpoint
4. request body
8. increase in number of requests per second

For example: ```java -jar target/SixLoadTester-SNAPSHOT.jar stress get https://localhost:8080/products {} 50```

## Design
The tool recognizes 2 types of tests:
- load tests
- stress tests

Load tests simulate an expected load on the system when in production. Normally, they also simulate ramping up and ramping down of requests to the expected so as not to congest the system at the start. Load tests are designed to ensure that you meet user expectations, such as service level agreement (SLA) promises. The goal is to ensure an acceptable overall user experience rather than try to break the application.

Stress tests serve to find the breaking point of given endpoint, meaning, how many requests per second can be sent to given endpoint before it starts erroring out. The goal is more to determine a maximum limit than to identify bottlenecks.
