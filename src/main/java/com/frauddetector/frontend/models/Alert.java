package com.frauddetector.frontend.models;

public class Alert {
    private String rule;
    private String details;
    private long timestamp;
    public Alert() {
    }
    public Alert(String rule, String details, long timestamp) {
        this.rule = rule;
        this.details = details;
        this.timestamp = timestamp;
    }
    public String getRule() {
        return rule;
    }
    public void setRule(String rule) {
        this.rule = rule;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}