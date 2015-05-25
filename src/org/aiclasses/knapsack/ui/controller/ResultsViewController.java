package org.aiclasses.knapsack.ui.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
    private Label calculateTimeLabel;
    @FXML
    private Label geneticWeightLabel;
    @FXML
    private Label geneticValueLabel;
    @FXML
    private Label dynamicWeightLabel;
    @FXML
    private Label dynamicValueLabel;
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

    /**
     * Task that runs dynamic and ga algorithm in new thread and updates UI
     */
    class AlgorithmTask extends Task<Boolean>
    {
        private List<Item> items;

        public AlgorithmTask(List<Item> items)
        {
            this.items = items;
        }

        protected Result summarize(ObservableList<Item> list)
        {
            Result result = new Result();
            result.setTotalValue(0.00);
            result.setTotalWeight(0);

            for (Item i : list)
            {
                result.setTotalWeight((int) (i.getWeight() + result.getTotalWeight()));
                result.setTotalValue(i.getValue() + result.getTotalValue());
            }

            return result;
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
            List<Treasure> dynamicSolution = knapsackDynamic.solve(treasures, mainApp.getAppData().getKnapsack());
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

                Result summarizeDynamic = summarize(dynamicTableView.getItems());
                dynamicWeightLabel.setText(String.valueOf(summarizeDynamic.getTotalWeight()));
                dynamicValueLabel.setText(String.valueOf(summarizeDynamic.getTotalValue()));
                Collections.sort(dynamicTableView.getItems());
            });

            // clearing result tableview
            Platform.runLater(() -> resultsTableView.getItems().clear());

            // ga solution
            final boolean steps = mainApp.getAppData().getSteps();
            final long[] presentationTime = {0}; // time taken to present data about steps

            KnapsackGA knapsackGA = new KnapsackGA(mainApp.getAppData().getPopulation(), mainApp.getAppData().getIterations(), mainApp.getAppData().getCrossover(), mainApp.getAppData().getMutation());
            // inner class that handles data about every iteration
            knapsackGA.setListener((solution, population) -> {
                long start = System.currentTimeMillis();
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
                    resultsTableView.scrollTo(resultsTableView.getItems().size() - 1);
                    progressBar.setProgress((double) population / (double) mainApp.getAppData().getIterations());

                    geneticValueLabel.setText(String.valueOf(totalValue));
                    geneticWeightLabel.setText(String.valueOf(totalWeight));
                });
                if (steps)
                {
                    try
                    {
                        Thread.sleep(150);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                presentationTime[0] += System.currentTimeMillis() - start;
            });

            long startTime = System.currentTimeMillis();
            List<Treasure> gaSolution = knapsackGA.solve(treasures, mainApp.getAppData().getKnapsack());
            long endTime = System.currentTimeMillis();
            long gaTime = endTime - startTime - presentationTime[0];
            double gaScore = 0.0;
            for (Treasure treasure : gaSolution)
            {
                gaScore += treasure.getValue();
            }

            double error = 1.0 - gaScore / dynamicScore;

            // setting result labels
            Platform.runLater(() -> {
                averageErrorLabel.setText(String.format("%.2f%%", error * 100));
                calculateTimeLabel.setText(gaTime + " ms");

                Collections.sort(geneticTableView.getItems());

                List<Treasure> dynamicItems = new ArrayList<>(dynamicSolution);
                int sameItems = 0;
                for (Treasure treasure : gaSolution)
                {
                    if (dynamicItems.contains(treasure))
                    {
                        sameItems++;
                        dynamicItems.remove(treasure);
                    }
                }

                correctSolutionsLabel.setText(sameItems + "/" + dynamicSolution.size());
            });

            return true;
        }


    }
}