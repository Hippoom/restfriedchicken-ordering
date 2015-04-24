package com.restfriedchicken.ordering.rest;

public class ApplicationMeta {

    private String version;

    public ApplicationMeta(String version) {
        this.version = version;
    }

    /**
     * for framework only
     */
    private ApplicationMeta() {
    }

    public String getVersion() {
        return version;
    }
}
