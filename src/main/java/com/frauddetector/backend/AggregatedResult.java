package com.frauddetector.backend;
public class AggregatedResult {
    private String key;
    private double value;
    public AggregatedResult() {
    }
    public AggregatedResult(String key, double value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public double getValue() {
        return value;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setValue(double value) {
        this.value = value;
    }
}