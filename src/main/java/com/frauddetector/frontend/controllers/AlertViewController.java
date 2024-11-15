package com.frauddetector.frontend.controllers;
import com.frauddetector.backend.AlertViewControllerSingleton;

import com.frauddetector.frontend.models.Alert;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
public class AlertViewController {
    @FXML
    private TableView<Alert> alertTable;
    @FXML
    private TableColumn<Alert, String> ruleColumn;
    @FXML
    private TableColumn<Alert, String> detailsColumn;
    @FXML
    private TableColumn<Alert, Long> timestampColumn;
    private ObservableList<Alert> alertList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        ruleColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getRule()));
        detailsColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getDetails()));
        timestampColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTimestamp()));
        alertTable.setItems(alertList);


        AlertViewControllerSingleton.setInstance(this);
    }
    public void addAlert(Alert alert) {
        Platform.runLater(() -> alertList.add(alert));
    }
}