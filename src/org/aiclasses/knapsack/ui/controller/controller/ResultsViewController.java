package org.aiclasses.knapsack.ui.controller.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.aiclasses.knapsack.ui.controller.model.Item;
import org.aiclasses.knapsack.ui.controller.model.Result;


/**
 * Created by adam on 23.05.15.
 */
public class ResultsViewController {
    @FXML
    private TableView<Result> resultsTableView;
    @FXML
    private TableColumn<Result, Integer> iterationNumberColumn;
    @FXML
    private TableColumn<Result, Integer> totalWeightColumn;
    @FXML
    private TableColumn<Result, Double> totalValueColumn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TableView<Item> geneticTableView;
    @FXML
    private TableColumn<Item, Integer> weightGeneticTableColumn;
    @FXML
    private TableColumn<Item, Double> valueGeneticTableColumn;

    @FXML
    private TableView<Item> dynamicTableView;
    @FXML
    private TableColumn<Item, Integer> weightDynamicTableColumn;
    @FXML
    private TableColumn<Item, Double> valueDynamicTableColumn;

    @FXML
    private Label correctSolutionsLabel;
    @FXML
    private Label averageErrorLabel;
    @FXML
    private Label maximalErrorLabel;
    @FXML
    private Label calculateTimeLabel;

    @FXML
    private Button returnButton;

    private MainApp mainApp;

    public ResultsViewController() {
    }

    @FXML
    private void initialize() {
        iterationNumberColumn.setCellValueFactory(cellData -> cellData.getValue().iterationNumberProperty().asObject());
        totalValueColumn.setCellValueFactory(cellData -> cellData.getValue().totalValueProperty().asObject());
        totalWeightColumn.setCellValueFactory(cellData -> cellData.getValue().totalWeightProperty().asObject());

        weightGeneticTableColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        valueGeneticTableColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());

        weightDynamicTableColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        valueDynamicTableColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        resultsTableView.setItems(mainApp.getAppData().getResultsObservableList());

        geneticTableView.setItems(mainApp.getAppData().getGeneticKnapsackObservableList());

        dynamicTableView.setItems(mainApp.getAppData().getDynamicKnapsackObservableList());
    }

    @FXML
    private void handleReturn() {
        mainApp.showDataView();
    }
}