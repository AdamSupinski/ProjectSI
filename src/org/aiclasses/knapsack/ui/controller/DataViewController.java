package org.aiclasses.knapsack.ui.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.aiclasses.knapsack.Treasure;
import org.aiclasses.knapsack.TreasureFactory;
import org.aiclasses.knapsack.ui.model.Item;

import java.util.Optional;


public class DataViewController
{
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

    @FXML
    private CheckBox stepsCheckbox;

    private MainApp mainApp;

    public DataViewController()
    {
    }

    @FXML
    private void initialize()
    {
        weightTableColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        valueTableColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());
    }

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;

        setItems(mainApp.getAppData().getItemsObservableList());

        if (mainApp.getAppData().getIterations() != null
                && mainApp.getAppData().getPopulation() != null
                && mainApp.getAppData().getCrossover() != null
                && mainApp.getAppData().getMutation() != null
                && mainApp.getAppData().getKnapsack() != null
                && mainApp.getAppData().getSteps() != null)
        {
            iterationsTextField.setText(mainApp.getAppData().getIterations().toString());
            populationTextField.setText(mainApp.getAppData().getPopulation().toString());
            crossoverTextField.setText(mainApp.getAppData().getCrossover().toString());
            mutationTextField.setText(mainApp.getAppData().getMutation().toString());
            knapsackTextField.setText(mainApp.getAppData().getKnapsack().toString());
            stepsCheckbox.setSelected(mainApp.getAppData().getSteps());
        }
    }

    public void setItems(ObservableList<Item> list)
    {
        this.itemTableView.setItems(list);
    }

    /**
     * Method adds new item
     */
    @FXML
    private void handleAddItem()
    {
        try
        {
            Integer weight = Integer.parseInt(weightTextField.getText());
            Double value = Double.parseDouble(valueTextField.getText());

            weightTextField.setText("");
            valueTextField.setText("");

            Item item = new Item(weight, value);

            itemTableView.getItems().add(item);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd danych");
            alert.setHeaderText("Podane dane są nieprawidłowe");
            alert.setContentText("Waga powinna być wartością całkowitą, wartość liczbą zmiennoprzecinkową");
            alert.showAndWait();
        }
    }

    /**
     * Method deletes item selected from TableView
     */
    @FXML
    private void handleDeleteItem()
    {
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();

        itemTableView.getItems().remove(selectedItem);
    }

    /**
     * Method reads parameters for genetic algorithm (number of iterations, crossover rate,
     * mutation probability)
     *
     * @return true if parsing and saving of all parameters is successfull
     */
    private boolean getParameters()
    {
        try
        {
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
            mainApp.getAppData().setSteps(stepsCheckbox.isSelected());

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method checks if provided input is enough to proceed with genetic algorithm
     *
     * @return true if all parameters are valid and there is at least one item to calculate
     */
    private boolean isDataValid()
    {
        if (!getParameters())
            return false;
        if (mainApp.getAppData().getCrossover() < 0.0 || mainApp.getAppData().getCrossover() > 1.0)
            return false;
        if (mainApp.getAppData().getMutation() < 0.0 || mainApp.getAppData().getMutation() > 1.0)
            return false;

        return itemTableView.getItems().size() != 0;
    }

    /**
     * Method checks if provided input is valid, then saves all created items into AppData
     */
    @FXML
    private void handleCalculate()
    {
        if (isDataValid())
        {
            mainApp.getAppData().setItemsObservableList(itemTableView.getItems());
            mainApp.showResultsView();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd danych");
            alert.setHeaderText("Podane dane są nieprawidłowe");
            alert.setContentText("Upewnij się, czy:\n- wszystkie pola zostały uzupełnione\n" +
                    "- współczynnik krzyżowania jest z przedziału <0;1>\n" +
                    "- prawdopodobieństwo mutacji jest z przedziału <0;1>\n" +
                    "- lista przedmiotów nie jest pusta");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGenerateData()
    {
        int knapsack;
        try
        {
            knapsack = Integer.parseInt(knapsackTextField.getText());
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd danych");
            alert.setHeaderText("Podane dane są nieprawidłowe");
            alert.setContentText("Podaj prawidłową pojemność plecaka");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog("100");
        dialog.setTitle("Wygeneruj przedmioty");
        dialog.setHeaderText("Wybierz liczbę przedmiotów");
        dialog.setContentText("Wpisz liczbę przedmiotów");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            int number = Integer.parseInt(result.get());
            itemTableView.getItems().clear();

            Treasure[] treasures = TreasureFactory.generate(knapsack, number);
            for (Treasure t : treasures)
            {
                itemTableView.getItems().add(new Item(t.getWeight(), t.getValue()));
            }

        }
    }
}
