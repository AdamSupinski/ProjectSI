package org.aiclasses.knapsack.ui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.aiclasses.knapsack.ui.model.AppData;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private RootLayoutController rootLayoutController;
    private DataViewController dataViewController;
    private ResultsViewController resultsViewController;

    private AppData appData;

    public MainApp() {
        this.appData = new AppData();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Knapsack Problem Solver");

        initRootLayout();

        showDataView();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/org/aiclasses/knapsack/ui/view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the DataView inside the RootLayout.
     */
    public void showDataView() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/org/aiclasses/knapsack/ui/view/DataView.fxml"));
            AnchorPane dataView = loader.load();

            rootLayout.setCenter(dataView);

            dataViewController = loader.getController();
            dataViewController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the ResultsView inside the RootLayout.
     */
    public void showResultsView() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/org/aiclasses/knapsack/ui/view/ResultsView.fxml"));
            AnchorPane dataView = loader.load();

            rootLayout.setCenter(dataView);

            resultsViewController = loader.getController();
            resultsViewController.setMainApp(this);
            resultsViewController.runAlgorithmTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public AppData getAppData() {
        return appData;
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public DataViewController getDataViewController() {
        return dataViewController;
    }
}
