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
     * @param reference the object reference to clean from.
     * @param cleaner   the cleaning operation.
     */
    public static void register(Object reference, Cleaner cleaner) {
        synchronized (CLEANERS) {
            CLEANERS.put(reference, cleaner);
        }
    }

    /**
     * Execute the clean operation registered by given reference.
     *
     * @param reference the cleanup is coming from.
     * @throws NoCleanUpFoundException when there is no cleanup for given reference.
     */
    public static void cleanup(Object reference) throws NoCleanUpFoundException {
        synchronized (CLEANERS) {
            Cleaner cleaner = CLEANERS.get(reference);
            if (cleaner != null) {
                cleaner.clean();
                unregister(reference);
            } else {
                throw new NoCleanUpFoundException("No cleanup registered from given reference object");
            }
        }
    }

    private static void unregister(Object reference) {
        CLEANERS.remove(reference);
    }
}
