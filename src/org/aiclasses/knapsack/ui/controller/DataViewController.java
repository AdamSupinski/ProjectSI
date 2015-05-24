package org.aiclasses.knapsack.ui.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.aiclasses.knapsack.Treasure;
import org.aiclasses.knapsack.TreasureFactory;
import org.aiclasses.knapsack.ui.model.Item;


public class DataViewController {
    @FXML
    private TableView<Item> itemTableView;
    @FXML
    private TableColumn<Item, Integer> weightTableColumn;
    @FXML
    private TableColumn<Item, Double> valueTableColumn;

    @FXML
    private TextField weightTextField;
    @FXML
    private TextField valueTextField;
    @FXML
    private TextField iterationsTextField;
    @FXML
    private TextField populationTextField;
    @FXML
    private TextField crossoverTextField;
    @FXML
    private TextField mutationTextField;
    @FXML
    private TextField knapsackTextField;


    @FXML
    private Button addItemButton;
    @FXML
    private Button deleteItemButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Button generateButton;

    private MainApp mainApp;

    public DataViewController() {
    }

    @FXML
    private void initialize() {
        weightTableColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());

        valueTableColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());

        // TODO temporary default values for testing
        iterationsTextField.setText("5");
        populationTextField.setText("2");
        crossoverTextField.setText("0.2");
        mutationTextField.setText("0.2");
        knapsackTextField.setText("5");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        setItems(mainApp.getAppData().getItemsObservableList());
        // TODO temporary default values for testing
        mainApp.getAppData().getItemsObservableList().add(new Item(5, 1.0));
        mainApp.getAppData().getItemsObservableList().add(new Item(5, 2.0));

        if (mainApp.getAppData().getIterations() != null
                && mainApp.getAppData().getPopulation() != null
                && mainApp.getAppData().getCrossover() != null
                && mainApp.getAppData().getMutation() != null
                && mainApp.getAppData().getKnapsack() != null) {
            iterationsTextField.setText(mainApp.getAppData().getIterations().toString());
            populationTextField.setText(mainApp.getAppData().getPopulation().toString());
            crossoverTextField.setText(mainApp.getAppData().getCrossover().toString());
            mutationTextField.setText(mainApp.getAppData().getMutation().toString());
            knapsackTextField.setText(mainApp.getAppData().getKnapsack().toString());
        }
    }

    public void setItems(ObservableList<Item> list) {
        this.itemTableView.setItems(list);
    }

    /**
     * Method adds new item
     */
    @FXML
    private void handleAddItem() {
        try {
            Integer weight = Integer.parseInt(weightTextField.getText());
            Double value = Double.parseDouble(valueTextField.getText());

            weightTextField.setText("");
            valueTextField.setText("");

            Item item = new Item(weight, value);

            itemTableView.getItems().add(item);
        } catch (Exception e) {
        }
    }

    /**
     * Method deletes item selected from TableView
     */
    @FXML
    private void handleDeleteItem() {
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();

        itemTableView.getItems().remove(selectedItem);
    }

    /**
     * Method reads parameters for genetic algorithm (number of iterations, crossover rate,
     * mutation probability)
     *
     * @return true if parsing and saving of all parameters is successfull
     */
    private boolean getParameters() {
        try {
            Integer iterations = Integer.parseInt(iterationsTextField.getText());
            Integer population = Integer.parseInt(populationTextField.getText());
            Double crossover = Double.parseDouble(crossoverTextField.getText());
            Double mutation = Double.parseDouble(mutationTextField.getText());
            Integer knapsack = Integer.parseInt(knapsackTextField.getText());

            mainApp.getAppData().setIterations(iterations);
            mainApp.getAppData().setPopulation(population);
            mainApp.getAppData().setCrossover(crossover);
            mainApp.getAppData().setMutation(mutation);
            mainApp.getAppData().setKnapsack(knapsack);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method checks if provided input is enough to proceed with genetic algorithm
     *
     * @return true if all parameters are valid and there is at least one item to calculate
     */
    private boolean isDataValid() {
        return getParameters() && itemTableView.getItems().size() != 0;
    }

    /**
     * Method checks if provided input is valid, then saves all created items into AppData
     */
    @FXML
    private void handleCalculate() {
        if (isDataValid()) {
            mainApp.getAppData().setItemsObservableList(itemTableView.getItems());
            mainApp.showResultsView();
        }
    }

    @FXML
    private void handleGenerateData() {
        try {
            Integer knapsack = Integer.parseInt(knapsackTextField.getText());
            Integer population = Integer.parseInt(populationTextField.getText());

            Treasure[] treasures = TreasureFactory.generate(knapsack, population);

            for (Treasure t : treasures) {
                itemTableView.getItems().add(new Item(t.getWeight(), t.getValue()));
            }
        } catch (Exception e) {
            return;
        }

    }
}
