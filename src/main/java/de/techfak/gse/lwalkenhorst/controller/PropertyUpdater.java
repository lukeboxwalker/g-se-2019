package de.techfak.gse.lwalkenhorst.controller;

import java.beans.PropertyChangeEvent;

/**
 * Updating a property that the class is visualizing.
 *
 * @param <T> property type to update
 */
public interface PropertyUpdater<T> {

    default void updateProperty(PropertyChangeEvent propertyChangeEvent, Class<T> clazz) throws ClassCastException {
        updateProperty(clazz.cast(propertyChangeEvent.getOldValue()), clazz.cast(propertyChangeEvent.getOldValue()));
    }

    void updateProperty(T oldProperty, T newProperty);
}
