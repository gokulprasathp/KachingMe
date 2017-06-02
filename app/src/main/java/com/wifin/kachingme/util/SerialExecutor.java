package com.wifin.kachingme.util;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * Created by comp on 5/9/2017.
 */
public  class SerialExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque();
    final Executor executor;
    Runnable active;

   public SerialExecutor(Executor executor) {
        this.executor = executor;
    }


    public synchronized void execute(final Runnable r) {
        tasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }
}