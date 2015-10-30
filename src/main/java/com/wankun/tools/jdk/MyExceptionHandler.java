package com.wankun.tools.jdk;

import org.apache.hadoop.util.ExitUtil;
import org.apache.hadoop.util.ShutdownHookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author : wankun
 * Date : 2015/10/30 14:50
 */
public class MyExceptionHandler {
    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        Thread.currentThread().sleep(1000);
        System.out.println(System.currentTimeMillis() + "  ..");
        double i = 1 / (2 - 2);
//        Thread.currentThread().sleep(1000);
//        System.out.println(System.currentTimeMillis() + "  ..");
//        System.exit(-99);
//        while (true) {
//            Thread.currentThread().sleep(1000);
//            System.out.println(System.currentTimeMillis() + "  ..");
//        }

    }
}

class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(MyUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.printf(t.getName());
        if (ShutdownHookManager.get().isShutdownInProgress()) {
            logger.error("Thread " + t + " threw an Throwable, but we are shutting " +
                    "down, so ignoring this", e);
        } else if (e instanceof Error) {
            try {
                logger.error("Thread " + t + " threw an Error.  Shutting down now...", e);
            } catch (Throwable err) {
                //We don't want to not exit because of an issue with loggerging
            }
            if (e instanceof OutOfMemoryError) {
                //After catching an OOM java says it is undefined behavior, so don't
                //even try to clean up or we can get stuck on shutdown.
                try {
                    System.err.println("Halting due to Out Of Memory Error...");
                } catch (Throwable err) {
                    //Again we done want to exit because of loggerging issues.
                }
                ExitUtil.halt(-1);
            } else {
                ExitUtil.terminate(-1);
            }
        } else {
            logger.error("Thread " + t + " threw an Exception.", e);
        }
    }
}
