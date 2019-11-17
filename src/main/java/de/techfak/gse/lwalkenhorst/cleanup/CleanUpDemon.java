package de.techfak.gse.lwalkenhorst.cleanup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Runnable responsible for memory cleaning.
 */
public final class CleanUpDemon implements Runnable {

    public static final Thread CLEANUP_THREAD = new Thread(new CleanUpDemon());
    private static final HashMap<CleanUp, List<Cleaner>> CLEANERS = new HashMap<>();

    private CleanUpDemon() {
        super();
    }

    /**
     * Registers a new cleaning operation from a CleanUp.
     * Adding the cleaning operation in the static map.
     *
     * @param cleanUp the object the cleaner comes from.
     * @param cleaner the cleaning operation.
     */
    public static void register(CleanUp cleanUp, Cleaner cleaner) {
        synchronized (CLEANERS) {
            if (CLEANERS.get(cleanUp) == null) {
                List<Cleaner> cleaners = new ArrayList<>();
                cleaners.add(cleaner);
                CLEANERS.put(cleanUp, cleaners);
            } else {
                CLEANERS.get(cleanUp).add(cleaner);
            }
        }
    }

    /**
     * Removes all cleaning operation set by a CleanUp.
     *
     * @param cleanUp the object to remove all cleanup from.
     */
    public static void unregister(CleanUp cleanUp) {
        synchronized (CLEANERS) {
            List<Cleaner> cleaners = CLEANERS.get(cleanUp);
            if (cleaners != null) {
               CLEANERS.remove(cleanUp);
            }
        }
    }

    /**
     * Removes all cleaning operation set by a CleanUp.
     *
     * @param cleanUp the object to remove all cleanup from.
     */
    public static void clean(CleanUp cleanUp) {
        synchronized (CLEANERS) {
            List<Cleaner> cleaners = CLEANERS.get(cleanUp);
            if (cleaners != null) {
                cleaners.forEach((Cleaner::clean));
                CLEANERS.remove(cleanUp);
            }
        }
    }


    @Override
    public void run() {
        synchronized (CLEANERS) {
            CLEANERS.forEach(((cleanUp, cleaners) -> cleaners.forEach(Cleaner::clean)));
        }
    }
}
