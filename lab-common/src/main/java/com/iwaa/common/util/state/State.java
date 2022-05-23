package com.iwaa.common.util.state;

import java.util.concurrent.atomic.AtomicBoolean;

public class State {

    private final AtomicBoolean performanceStatus = new AtomicBoolean(true);

    public boolean getPerformanceStatus() {
        return performanceStatus.get();
    }

    public void switchPerformanceStatus() {
        performanceStatus.set(!performanceStatus.get());
    }

}
