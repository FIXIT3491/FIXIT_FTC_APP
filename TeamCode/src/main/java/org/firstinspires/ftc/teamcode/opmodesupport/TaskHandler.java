package org.firstinspires.ftc.teamcode.opmodesupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by FIXIT on 16-10-02.
 */
public final class TaskHandler {

    private static ScheduledExecutorService exec;

    private static HashMap<String, ScheduledFuture> futures = new HashMap<>();

    private static int delay = -1;

    public static void init(int numTasks) {
        exec = Executors.newScheduledThreadPool(numTasks);
    }

    public static void addTask(String name, Runnable task, int delayMS) {
        delay = delayMS;
        futures.put(name, exec.scheduleAtFixedRate(task, 0, delay, TimeUnit.MILLISECONDS));
    }

    public static void addTask(String name, Runnable task) {
        futures.put(name, exec.scheduleAtFixedRate(task, 0, delay, TimeUnit.MILLISECONDS));
    }//addTask

    public static void removeTask(String name) {
        futures.get(name).cancel(true);
        futures.put(name, null);
    }

    public static void switchTask(String remove, String add, Runnable task) {
        removeTask(remove);
        addTask(add, task);
    }

    public static class MultiRunnable implements Runnable {

        List<Runnable> tasks = new ArrayList<>();

        @Override
        public void run() {

            synchronized (tasks) {
                for (Runnable task : tasks) {
                    task.run();
                }//for
            }//synchronized

        }//run

        public void addRunnable(Runnable r) {
            synchronized (tasks) {
                tasks.add(r);
            }//synchronized
        }//addRunnable

        public void removeRunnable(Runnable r) {
            synchronized (tasks) {
                tasks.remove(r);
            }//synchronized
        }//removeRunnable

    }//MultiThread


}
