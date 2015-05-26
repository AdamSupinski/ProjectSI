package org.aiclasses.knapsack.ui.controller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.aiclasses.knapsack.ui.model.Item;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class RootLayoutController
{
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleOpen()
    {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        String data;

        //Read from XML file to String
        try
        {
            data = FileUtils.readFileToString(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        //Convert from XML
        XStream xStream = new XStream(new StaxDriver());

        //Set data
        mainApp.getAppData().setItemsObservableList((ObservableList<Item>) xStream.fromXML(data));
        mainApp.getDataViewController().setItems(mainApp.getAppData().getItemsObservableList());
    }

    @FXML
    private void handleSave()
    {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null)
        {
            if (!file.getPath().endsWith(".xml"))
            {
                file = new File(file.getPath() + ".xml");
            }

            XStream xStream = new XStream(new StaxDriver());

            String data = xStream.toXML(mainApp.getAppData().getItemsObservableList());

            PrintWriter fileOutput;
            try
            {
                fileOutput = new PrintWriter(file);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                return;
            }
            fileOutput.print(data);
            fileOutput.flush();
            fileOutput.close();
        }
    }


}
