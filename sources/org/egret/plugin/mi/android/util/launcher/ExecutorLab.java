package org.egret.plugin.mi.android.util.launcher;

import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorLab {
    private static final String TAG = "ExecutorLab";
    private static final int THREAD_SIZE = 3;
    private static ExecutorLab instance = null;
    private ExecutorService pool = Executors.newFixedThreadPool(3);
    private volatile boolean running = true;

    public static ExecutorLab getInstance() {
        if (instance == null) {
            instance = new ExecutorLab();
        }
        return instance;
    }

    public static void releaseInstance() {
        ExecutorLab executorLab = instance;
        if (executorLab != null) {
            executorLab.shutDown();
            instance = null;
        }
    }

    private ExecutorLab() {
    }

    public void addTask(Runnable task) {
        if (this.running) {
            this.pool.execute(task);
        } else {
            Log.d(TAG, "ExecutorLab is stop");
        }
    }

    private void shutDown() {
        if (!this.pool.isShutdown()) {
            this.running = false;
            this.pool.shutdown();
            while (!this.pool.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.pool = null;
        }
    }

    public boolean isRunning() {
        return this.running;
    }
}
