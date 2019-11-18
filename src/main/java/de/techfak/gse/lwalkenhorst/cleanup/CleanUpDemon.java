package de.techfak.gse.lwalkenhorst.cleanup;

import java.util.HashMap;

import java.util.Map;

/**
 * Responsible for memory cleaning after program closes.
 */
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

    /**
     * Registers a new cleaning operation from an object.
     * Adding the cleaning operation in the static map.
     *
     * @param ref the object reference to clean from.
     * @param cleaner the cleaning operation.
     */
    public static void register(Object ref, Cleaner cleaner) {
        synchronized (CLEANERS) {
            CLEANERS.put(ref, cleaner);
        }
    }
}
