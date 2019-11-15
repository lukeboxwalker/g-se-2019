package de.techfak.gse.lwalkenhorst.cleanup;

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
