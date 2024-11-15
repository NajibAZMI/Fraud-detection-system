package com.frauddetector.frontend.controllers;

import com.frauddetector.frontend.models.Transaction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
public class TransactionViewController {
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> payerIdColumn;
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    @FXML
    private TableColumn<Transaction, String> beneficiaryIdColumn;
    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        payerIdColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getPayerId()));
        amountColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
        beneficiaryIdColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getBeneficiaryId()));
        transactionTypeColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionType()));
        transactionTable.setItems(transactionList);
    }
    public void addTransaction(Transaction transaction) {
        Platform.runLater(() -> transactionList.add(transaction));
    }
}