package org.aiclasses.knapsack.ui.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.aiclasses.knapsack.Treasure;
import org.aiclasses.knapsack.dynamic.KnapsackDynamic;
import org.aiclasses.knapsack.ga.KnapsackGA;
import org.aiclasses.knapsack.ui.model.Item;
import org.aiclasses.knapsack.ui.model.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultsViewController
{
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

    public ResultsViewController()
    {
    }

    @FXML
    private void initialize()
    {
        iterationNumberColumn.setCellValueFactory(cellData -> cellData.getValue().iterationNumberProperty().asObject());
        totalValueColumn.setCellValueFactory(cellData -> cellData.getValue().totalValueProperty().asObject());
        totalWeightColumn.setCellValueFactory(cellData -> cellData.getValue().totalWeightProperty().asObject());

        weightGeneticTableColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        valueGeneticTableColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());

        weightDynamicTableColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        valueDynamicTableColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());
    }

    public void runAlgorithmTask()
    {
        Task task = new AlgorithmTask(new ArrayList<>(mainApp.getAppData().getItemsObservableList()));
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    class AlgorithmTask extends Task<Boolean>
    {
        List<Item> items;

        public AlgorithmTask(List<Item> items)
        {
            this.items = items;
        }

        @Override
        protected Boolean call() throws Exception
        {
            // prepare treasures
            Treasure[] treasures = new Treasure[items.size()];
            int i = 0;
            for (Item item : items)
            {
                treasures[i] = new Treasure(item.getWeight(), item.getValue());
                i++;
            }

            // dynamic solution
            KnapsackDynamic knapsackDynamic = new KnapsackDynamic();
            long startTime = System.currentTimeMillis();
            List<Treasure> dynamicSolution = knapsackDynamic.solve(treasures, mainApp.getAppData().getKnapsack());
            long endTime = System.currentTimeMillis();
            long dynamicTime = endTime - startTime;
            double dynamicScore = 0.0;
            for (Treasure treasure : dynamicSolution)
            {
                dynamicScore += treasure.getValue();
            }

            // update dynamic tableview
            Platform.runLater(() -> {
                mainApp.getAppData().getDynamicKnapsackObservableList().clear();
                for (Treasure treasure : dynamicSolution)
                {
                    Item item = new Item(treasure.getWeight(), treasure.getValue());
                    mainApp.getAppData().getDynamicKnapsackObservableList().add(item);
                }
            });

            Platform.runLater(() -> {
                resultsTableView.getItems().clear();
            });

            // ga solution
            KnapsackGA knapsackGA = new KnapsackGA(mainApp.getAppData().getPopulation(), mainApp.getAppData().getIterations(), mainApp.getAppData().getCrossover(), mainApp.getAppData().getMutation());
            knapsackGA.setListener((solution, population) -> {
                Platform.runLater(() -> {
                    geneticTableView.getItems().clear();
                    int totalWeight = 0;
                    double totalValue = 0.0;
                    for (Treasure treasure : solution)
                    {
                        geneticTableView.getItems().add(new Item(treasure.getWeight(), treasure.getValue()));
                        totalWeight += treasure.getWeight();
                        totalValue += treasure.getValue();
                    }

                    Result result = new Result();
                    result.setIterationNumber(population);
                    result.setTotalWeight(totalWeight);
                    result.setTotalValue(totalValue);
                    resultsTableView.getItems().add(result);
                    progressBar.setProgress((double) population / (double) mainApp.getAppData().getIterations());

                    try
                    {
                        Thread.sleep(300);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                });
            });
            startTime = System.currentTimeMillis();
            List<Treasure> gaSolution = knapsackGA.solve(treasures, mainApp.getAppData().getKnapsack());
            endTime = System.currentTimeMillis();
            long gaTime = endTime - startTime;
            double gaScore = 0.0;
            for (Treasure treasure : gaSolution)
            {
                gaScore += treasure.getValue();
            }

            double error = 1.0 - gaScore / dynamicScore;

            Platform.runLater(() -> {
                averageErrorLabel.setText(String.format("%.2f%%", error * 100));
                calculateTimeLabel.setText(gaTime + " ms");
            });

            return true;
        }


    }

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;

        resultsTableView.setItems(mainApp.getAppData().getResultsObservableList());

        geneticTableView.setItems(mainApp.getAppData().getGeneticKnapsackObservableList());

        dynamicTableView.setItems(mainApp.getAppData().getDynamicKnapsackObservableList());
    }

    @FXML
    private void handleReturn()
    {
        mainApp.showDataView();
    }
}