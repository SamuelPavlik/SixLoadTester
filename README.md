# SixLoadTester
Tool for performance tests of REST endpoints.

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
8. increase in number of requests to per second

For example: ```java -jar target/SixLoadTester-SNAPSHOT.jar load get https://localhost:8080/products {} 50```

## Design
//TODO
