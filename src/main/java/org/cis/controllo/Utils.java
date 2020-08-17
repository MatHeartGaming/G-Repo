package org.cis.controllo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Utils {

    private static final String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);


    public static void setTimeout(Runnable runnable, long delay) {
        if (delay < 0) throw new IllegalArgumentException("The delay argument cannot be negative: " + delay);
        if (runnable == null) throw new IllegalArgumentException("The runnable argument cannot be null");

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
                t.cancel();
            }
        }, delay);
    }

    public static TimerInterval setInterval(Runnable runnable, long period) {
        if (period <= 0) throw new IllegalArgumentException("The period argument cannot be negative or zero : " + period);
        if (runnable == null) throw new IllegalArgumentException("The runnable argument cannot be null");

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, period, period);
        return new TimerInterval(t);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException("The places argument cannot be negative: " + places);

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static class TimerInterval {

        private Timer timer;

        public TimerInterval(Timer timer) {
            this.timer = timer;
        }

        public void cancel() {
            this.timer.cancel();
        }
    }
}
