package com.frauddetector.frontend.controllers;

import com.frauddetector.backend.RuleProcessor;
import com.frauddetector.frontend.models.Rule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
public class RuleManagerController {
    @FXML
    private TableView<Rule> ruleTable;
    @FXML
    private TableColumn<Rule, String> aggregationFunctionColumn;
    @FXML
    private TableColumn<Rule, String> fieldColumn;
    @FXML
    private TableColumn<Rule, String> operatorColumn;
    @FXML
    private TableColumn<Rule, Double> thresholdColumn;
    @FXML
    private TableColumn<Rule, String> timeWindowColumn;
    @FXML
    private TableColumn<Rule, String> windowTypeColumn;
    @FXML
    private TableColumn<Rule, String> groupingKeyColumn;
    @FXML
    private TableColumn<Rule, Boolean> activeColumn;
    @FXML
    private Button addRuleButton;
    @FXML
    private Button editRuleButton;
    @FXML
    private Button deleteRuleButton;
    @FXML
    private Button toggleActiveButton;
    private ObservableList<Rule> ruleList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        aggregationFunctionColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getAggregationFunction()));
        fieldColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getField()));
        operatorColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getOperator()));
        thresholdColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getThreshold()));
        timeWindowColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getTimeWindow()));
        windowTypeColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getWindowType()));
        groupingKeyColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleStringProperty(cellData.getValue().getGroupingKey()));
        activeColumn.setCellValueFactory(cellData -> new
                javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isActive()));
        ruleTable.setItems(ruleList);
    }
    @FXML
    private void handleAddRule() {
        Rule newRule = showRuleDialog(null);
        if (newRule != null) {
            ruleList.add(newRule);
            RuleProcessor.addRule(newRule);
        }
    }
    @FXML
    private void handleEditRule() {
        Rule selectedRule = ruleTable.getSelectionModel().getSelectedItem();
        if (selectedRule != null) {
            Rule updatedRule = showRuleDialog(selectedRule);
            if (updatedRule != null) {
                int index = ruleList.indexOf(selectedRule);
                ruleList.set(index, updatedRule);
                RuleProcessor.updateRule(updatedRule);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Rule Selected", "Please  select a rule to edit.");
        }
    }
    @FXML
    private void handleDeleteRule() {
        Rule selectedRule = ruleTable.getSelectionModel().getSelectedItem();
        if (selectedRule != null) {
            ruleList.remove(selectedRule);
            RuleProcessor.removeRule(selectedRule);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Rule Selected", "Please select a rule to delete.");
        }
    }
    @FXML
    private void handleToggleActive() {
        Rule selectedRule = ruleTable.getSelectionModel().getSelectedItem();
        if (selectedRule != null) {
            selectedRule.setActive(!selectedRule.isActive());
            ruleTable.refresh();
            RuleProcessor.updateRule(selectedRule);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Rule Selected", "Please select a rule to toggle.");
        }
    }
    private Rule showRuleDialog(Rule rule) {
        Dialog<Rule> dialog = new Dialog<>();
        dialog.setTitle(rule == null ? "Add Rule" : "Edit Rule");
        dialog.setHeaderText(rule == null ? "Define a new fraud detection rule." : "Edit the selected fraud detection rule.");
                ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // Aggregation Function
        ComboBox<String> aggregationFunctionBox = new ComboBox<>();
        aggregationFunctionBox.getItems().addAll("SUM", "COUNT", "AVERAGE");
        aggregationFunctionBox.setValue(rule != null ? rule.getAggregationFunction() :
                "SUM");
        // Field
        ComboBox<String> fieldBox = new ComboBox<>();
        fieldBox.getItems().addAll("Transaction Amount", "Payer ID", "Beneficiary ID",
                "Transaction Type");
        fieldBox.setValue(rule != null ? rule.getField() : "Transaction Amount");
        // Operator
        ComboBox<String> operatorBox = new ComboBox<>();
        operatorBox.getItems().addAll("GREATER", "LESS", "GREATER_EQUAL", "LESS_EQUAL");
        operatorBox.setValue(rule != null ? rule.getOperator() : "GREATER");
        // Threshold
        TextField thresholdField = new TextField();
        thresholdField.setPromptText("Threshold");
        thresholdField.setText(rule != null ? String.valueOf(rule.getThreshold()) : "");
        // Time Window
        TextField timeWindowField = new TextField();
        timeWindowField.setPromptText("Time Window (e.g., 30 minutes)");
        timeWindowField.setText(rule != null ? rule.getTimeWindow() : "30 minutes");
        // Window Type
        ComboBox<String> windowTypeBox = new ComboBox<>();
        windowTypeBox.getItems().addAll("FIXED", "SLIDING");
        windowTypeBox.setValue(rule != null ? rule.getWindowType() : "FIXED");
        // Grouping Key
        ComboBox<String> groupingKeyBox = new ComboBox<>();
        groupingKeyBox.getItems().addAll("Payer ID", "Beneficiary ID", "Transaction Type");
                groupingKeyBox.setValue(rule != null ? rule.getGroupingKey() : "Payer ID");
        // Active
        CheckBox activeCheckBox = new CheckBox("Active");
        activeCheckBox.setSelected(rule != null ? rule.isActive() : true);
        grid.add(new Label("Aggregation Function:"), 0, 0);
        grid.add(aggregationFunctionBox, 1, 0);
        grid.add(new Label("Field:"), 0, 1);
        grid.add(fieldBox, 1, 1);
        grid.add(new Label("Operator:"), 0, 2);
        grid.add(operatorBox, 1, 2);
        grid.add(new Label("Threshold:"), 0, 3);
        grid.add(thresholdField, 1, 3);
        grid.add(new Label("Time Window:"), 0, 4);
        grid.add(timeWindowField, 1, 4);
        grid.add(new Label("Window Type:"), 0, 5);
        grid.add(windowTypeBox, 1, 5);
        grid.add(new Label("Grouping Key:"), 0, 6);
        grid.add(groupingKeyBox, 1, 6);
        grid.add(activeCheckBox, 1, 7);
        dialog.getDialogPane().setContent(grid);
        // Convert the result to a Rule when the Save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    double threshold = Double.parseDouble(thresholdField.getText());
                    String aggregationFunction = aggregationFunctionBox.getValue();
                    String field = fieldBox.getValue();
                    String operator = operatorBox.getValue();
                    String timeWindow = timeWindowField.getText();
                    String windowType = windowTypeBox.getValue();
                    String groupingKey = groupingKeyBox.getValue();
                    boolean active = activeCheckBox.isSelected();
                    return new Rule(aggregationFunction, field, operator, threshold,
                            timeWindow, windowType, groupingKey, active);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Threshold must be a number.", e.getMessage());
                    return null;
                }
            }
            return null;
        });
        return dialog.showAndWait().orElse(null);
    }
    private void showAlert(Alert.AlertType type, String title, String header, String
            content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}