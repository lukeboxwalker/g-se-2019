package de.techfak.gse.lwalkenhorst.cleanup;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

/**
 * Responsible for memory cleaning after program closes.
 */
public final class CleanUpDemon {

    private static final Map<Object, Cleaner> CLEANERS = new HashMap<>();
    private static final CleanUpDemon INSTANCE = new CleanUpDemon();

    private CleanUpDemon() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (CLEANERS) {
                CLEANERS.forEach((ref, cleaner) -> cleaner.clean());
            }
        }));
    }

    public static CleanUpDemon getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a new cleaning operation from an object.
     * Adding the cleaning operation in the static map.
     *
     * @param reference the object reference to clean from.
     * @param cleaner   the cleaning operation.
     */
    public void register(final Object reference, final Cleaner cleaner) {
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
    public void cleanup(final Object reference) throws NoCleanUpFoundException {
        synchronized (CLEANERS) {
            final Cleaner cleaner = CLEANERS.get(reference);
            if (cleaner == null) {
                throw new NoCleanUpFoundException("No cleanup registered from given reference object");
            } else {
                cleaner.clean();
                unregister(reference);
            }
        }
    }

    /**
     * Execute all cleanup operations.
     */
    public void cleanup() {
        synchronized (CLEANERS) {
            final Iterator<Map.Entry<Object, Cleaner>> iterator = CLEANERS.entrySet().iterator();
            while (iterator.hasNext()) {
                iterator.next().getValue().clean();
                iterator.remove();
            }
        }
    }

    private void unregister(final Object reference) {
        CLEANERS.remove(reference);
    }
}
