package de.techfak.gse.lwalkenhorst.closeup;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

/**
 * Responsible for reference closing properly after program closes.
 */
public final class ObjectCloseupManager {

    private static final Map<Object, Closeup> CLOSEUPS = new HashMap<>();
    private static final ObjectCloseupManager INSTANCE = new ObjectCloseupManager();

    private ObjectCloseupManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (CLOSEUPS) {
                CLOSEUPS.forEach((ref, closeup) -> {
                    closeup.close();
                });
            }
        }));
    }

    public static ObjectCloseupManager getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a new close operation from an object.
     * Adding the close operation in the static map.
     *
     * @param reference the object reference to clean from.
     * @param closeup   the closing operation.
     */
    public void register(final Object reference, final Closeup closeup) {
        synchronized (CLOSEUPS) {
            CLOSEUPS.put(reference, closeup);
        }
    }

    /**
     * Execute the close operation registered by given reference.
     *
     * @param reference the cleanup is coming from.
     */
    public void closeObject(final Object reference) {
        synchronized (CLOSEUPS) {
            final Closeup closeup = CLOSEUPS.get(reference);
            if (closeup != null) {
                closeup.close();
                unregister(reference);
            }
        }
    }

    /**
     * Execute all close operations.
     */
    public void closeReferences() {
        synchronized (CLOSEUPS) {
            final Iterator<Map.Entry<Object, Closeup>> iterator = CLOSEUPS.entrySet().iterator();
            while (iterator.hasNext()) {
                iterator.next().getValue().close();
                iterator.remove();
            }
        }
    }

    public void unregister(final Object reference) {
        CLOSEUPS.remove(reference);
    }
}
