package de.techfak.gse.lwalkenhorst.radioplayer;

import java.beans.PropertyChangeListener;

/**
 * Marks Object as Observable.
 * Using {@link PropertyChangeListener} for
 * Observable pattern.
 */
public interface Observable {

    /**
     * Adding an observer to the Observable.
     * @param observer to add
     */
    void addPropertyChangeListener(PropertyChangeListener observer);

    /**
     * Removing an observer from the Observable.
     * @param observer to remove
     */
    void removePropertyChangeListener(PropertyChangeListener observer);
}
