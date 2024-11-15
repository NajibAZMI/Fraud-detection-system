package com.frauddetector.frontend.models;

public class Rule {
    private String aggregationFunction; // SUM, COUNT, AVERAGE
    private String field; // Transaction Amount, Payer ID, etc.
    private String operator; // GREATER, LESS, etc.
    private double threshold;
    private String timeWindow; // e.g., "30 minutes", "24 hours"
    private String windowType; // FIXED, SLIDING
    private String groupingKey; // Payer ID, Beneficiary ID, etc.
    private boolean active;
    public Rule() {
    }
    public Rule(String aggregationFunction, String field, String operator, double
            threshold,
                String timeWindow, String windowType, String groupingKey, boolean active)
    {
        this.aggregationFunction = aggregationFunction;
        this.field = field;
        this.operator = operator;
        this.threshold = threshold;
        this.timeWindow = timeWindow;
        this.windowType = windowType;
        this.groupingKey = groupingKey;
        this.active = active;
    }
    public String getAggregationFunction() {
        return aggregationFunction;
    }
    public void setAggregationFunction(String aggregationFunction) {
        this.aggregationFunction = aggregationFunction;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public double getThreshold() {
        return threshold;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    public String getTimeWindow() {
        return timeWindow;
    }
    public void setTimeWindow(String timeWindow) {
        this.timeWindow = timeWindow;
    }
    public String getWindowType() {
        return windowType;
    }
    public void setWindowType(String windowType) {
        this.windowType = windowType;
    }
    public String getGroupingKey() {
        return groupingKey;
    }
    public void setGroupingKey(String groupingKey) {
        this.groupingKey = groupingKey;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
