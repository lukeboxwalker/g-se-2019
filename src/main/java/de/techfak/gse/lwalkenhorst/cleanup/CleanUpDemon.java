package de.techfak.gse.lwalkenhorst.cleanup;

import java.util.HashMap;

/**
 * Runnable responsible for memory cleaning.
 */
import java.util.Map;

public final class CleanUpDemon {

    private static final Map<Object, Cleaner> CLEANERS = new HashMap<>();

    static {
        new CleanUpDemon();
    }

    private CleanUpDemon() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (CLEANERS) {
                CLEANERS.forEach((ref, cleaner) -> cleaner.clean());
            }
        }));
    }

    public static void register(Object ref, Cleaner cleaner) {
        synchronized (CLEANERS) {
            CLEANERS.put(ref, cleaner);
        }
    }
}
