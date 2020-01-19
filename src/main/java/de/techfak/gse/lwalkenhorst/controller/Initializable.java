package de.techfak.gse.lwalkenhorst.controller;

/**
 * Represents an Object that need to be initialized.
 * @param <T> Class needed for initialization. (The initiator)
 */
public interface Initializable<T> {
    void initialize(T initiator) throws Exception;
}
