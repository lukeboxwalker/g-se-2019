package de.techfak.gse.lwalkenhorst.controller;

public interface Initializable<T> {
    void initialize(T initiator) throws Exception;
}
