package org.firstinspires.ftc.teamcode.opmodesupport;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by FIXIT on 16-10-02.
 */
public final class TaskHandler {

    private static ExecutorService exec;

    private static HashMap<String, Future> futures = new HashMap<>();

    public static void init() {
        exec = Executors.newCachedThreadPool();
    }//init

    public static void addTask(String name, Runnable task) {
        futures.put(name, exec.submit(task));
    }//addTask

    public static void addLoopedTask(String name, Runnable task) {
        addTask(name, loop(task, 0));
    }

    public static void addLoopedTask(String name, Runnable task, int delay) {
        addTask(name, loop(task, delay));
    }

    public static boolean removeTask(String name) {
        if (futures.containsKey(name)) {
            futures.get(name).cancel(true);
            futures.put(name, null);
        } else {
            Log.e("TaskHandler", "Attempted to remove nonexistent task!");
        }//else

        return futures.containsKey(name);
    }//removeTask

    public static void switchTask(String remove, String add, Runnable task) {
        removeTask(remove);
        addTask(add, task);
    }

    private static Runnable loop (final Runnable r, final int delay) {

        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    r.run();

                    if (delay > 0) {
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }//catch
                    }//if

                    if (!Thread.currentThread().isInterrupted()) {
                        break;
                    }//if

                }//while
            }//run
        };

    }//loop

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
