package de.techfak.gse.lwalkenhorst.cleanup;

/**
 * Providing default methods to register a cleanup.
 */
public interface CleanUp {

    default void registerCleanUp(final Cleaner cleaner) {
        DemonCleaner.register(this, cleaner);
    }

    default void unregisterCleanUp() {
        DemonCleaner.unregister(this);
    }

    default void clean() {
        DemonCleaner.clean(this);
    }
}
