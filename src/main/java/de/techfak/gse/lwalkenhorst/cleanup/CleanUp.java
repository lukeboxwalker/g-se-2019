package de.techfak.gse.lwalkenhorst.cleanup;

/**
 * Providing default methods to register a cleanup.
 */
public interface CleanUp {

    default void registerCleanUp(final Cleaner cleaner) {
        CleanUpDemon.register(this, cleaner);
    }

    default void unregisterCleanUp() {
        CleanUpDemon.unregister(this);
    }

    default void clean() {
        CleanUpDemon.clean(this);
    }
}
