package tpafull.utils;

import java.util.concurrent.*;

public class GlobalScheduler {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public static void schedule(Runnable task, long delay, TimeUnit unit) {
        scheduler.schedule(task, delay, unit);
    }

    public static void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}


