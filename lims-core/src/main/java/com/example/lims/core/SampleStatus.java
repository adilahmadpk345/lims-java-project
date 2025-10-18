package com.example.lims.core;

public enum SampleStatus {
    NEW("New"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String label;
    SampleStatus(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
