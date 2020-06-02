package org.cis.controllo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThread {

    private ExecutorService service;

    public SingleThread start() {
        if (service != null) {
            return this;
        }

        service = Executors.newSingleThreadExecutor();
        return this;
    }

    public SingleThread close() {
        if (service == null) {
            return this;
        }

        service.shutdown();
        return this;
    }

    public SingleThread executeTask (Runnable runnable) {
        service.execute(runnable);
        return this;
    }

    /*public void executeTask (FutureTask futureTask) {
        service.submit(futureTask);
    }*/
}
