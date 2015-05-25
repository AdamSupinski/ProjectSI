package org.aiclasses.knapsack.ui.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppData
{
    private ObservableList<Item> itemsObservableList = FXCollections.observableArrayList();
    private ObservableList<Result> resultsObservableList = FXCollections.observableArrayList();
    private ObservableList<Item> geneticKnapsackObservableList = FXCollections.observableArrayList();
    private ObservableList<Item> dynamicKnapsackObservableList = FXCollections.observableArrayList();
    private Integer iterations;
    private Integer population;
    private Double crossover;
    private Double mutation;
    private Integer knapsack;
    private Boolean steps;

    public AppData()
    {

    }

    public ObservableList<Item> getItemsObservableList()
    {
        return itemsObservableList;
    }

    public void setItemsObservableList(ObservableList<Item> itemsObservableList)
    {
        this.itemsObservableList = itemsObservableList;
    }

    public Integer getIterations()
    {
        return iterations;
    }

    public void setIterations(Integer iterations)
    {
        this.iterations = iterations;
    }

    public Double getCrossover()
    {
        return crossover;
    }

    public void setCrossover(Double crossover)
    {
        this.crossover = crossover;
    }

    public Double getMutation()
    {
        return mutation;
    }

    public void setMutation(Double mutations)
    {
        this.mutation = mutations;
    }

    public Integer getPopulation()
    {
        return population;
    }

    public void setPopulation(Integer population)
    {
        this.population = population;
    }

    public Integer getKnapsack()
    {
        return knapsack;
    }

    public void setKnapsack(Integer knapsack)
    {
        this.knapsack = knapsack;
    }

    public ObservableList<Result> getResultsObservableList()
    {
        return resultsObservableList;
    }

    public void setResultsObservableList(ObservableList<Result> resultsObservableList)
    {
        this.resultsObservableList = resultsObservableList;
    }

    public ObservableList<Item> getDynamicKnapsackObservableList()
    {
        return dynamicKnapsackObservableList;
    }

    public void setDynamicKnapsackObservableList(ObservableList<Item> dynamicKnapsackObservableList)
    {
        this.dynamicKnapsackObservableList = dynamicKnapsackObservableList;
    }

    public ObservableList<Item> getGeneticKnapsackObservableList()
    {
        return geneticKnapsackObservableList;
    }

    public void setGeneticKnapsackObservableList(ObservableList<Item> geneticKnapsackObservableList)
    {
        this.geneticKnapsackObservableList = geneticKnapsackObservableList;
    }

    public Boolean getSteps()
    {
        return steps;
    }

    public void setSteps(Boolean steps)
    {
        this.steps = steps;
    }
}
