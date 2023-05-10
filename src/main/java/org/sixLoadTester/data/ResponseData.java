package org.sixLoadTester.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ResponseData {
    public AtomicInteger maxRequestsPerSecond;
    public AtomicInteger errorCount;
    public List<Long> responseTimes;

    public ResponseData() {
        this.maxRequestsPerSecond = new AtomicInteger(0);
        this.errorCount = new AtomicInteger(0);
        this.responseTimes = new ArrayList<>();
    }
}
