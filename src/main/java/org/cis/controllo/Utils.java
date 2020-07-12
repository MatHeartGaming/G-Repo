package org.cis.controllo;

import java.util.Timer;
import java.util.TimerTask;

public class Utils {

    public static void setTimeout(Runnable runnable, long delay) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
                t.cancel();
            }
        }, delay);
    }

    /*public static void setTimeout(Runnable runnable, long delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }*/
}
