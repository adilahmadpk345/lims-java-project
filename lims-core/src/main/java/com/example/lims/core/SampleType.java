package com.example.lims.core;

public enum SampleType {
    HORMONES("Hormones"),
    HEMATOLOGY("Hematology"),
    MICROBIOLOGY("Microbiology"),
    CHEMISTRY("Chemistry"),
    SEROLOGY("Serology");

    private final String label;

    SampleType(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
