package com.frauddetector.backend;
public class Transaction {
    private String payerId;
    private double amount;
    private String beneficiaryId;
    private String transactionType;
    public Transaction() {
    }
    public Transaction(String payerId, double amount, String beneficiaryId, String
            transactionType) {
        this.payerId = payerId;
        this.amount = amount;
        this.beneficiaryId = beneficiaryId;
        this.transactionType = transactionType;
    }
    public String getPayerId() {
        return payerId;
    }
    public void setPayerId(String payerId) {
        this.payerId = payerId;
    } public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getBeneficiaryId() {
        return beneficiaryId;
    }
    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}