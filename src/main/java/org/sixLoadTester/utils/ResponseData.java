package org.sixLoadTester.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ResponseData {
    public AtomicInteger overallCount;
    public AtomicInteger errorCount;
    public List<Long> responseTimes;

    public ResponseData(AtomicInteger overallCount, AtomicInteger errorCount, List<Long> responseTimes) {
        this.overallCount = overallCount;
        this.errorCount = errorCount;
        this.responseTimes = responseTimes;
    }
}
