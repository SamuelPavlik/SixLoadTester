package org.sixLoadTester.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ResponseData {
    public AtomicInteger overallCount;
    public AtomicInteger errorCount;
    public List<Long> responseTimes;

    public ResponseData() {
        this.overallCount = new AtomicInteger(0);
        this.errorCount = new AtomicInteger(0);
        this.responseTimes = new ArrayList<>();
    }
}
