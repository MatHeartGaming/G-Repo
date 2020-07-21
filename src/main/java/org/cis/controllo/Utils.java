package org.cis.controllo;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
