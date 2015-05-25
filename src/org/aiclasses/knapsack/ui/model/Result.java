package org.aiclasses.knapsack.ui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Result
{
    private IntegerProperty iterationNumber;
    private IntegerProperty totalWeight;
    private DoubleProperty totalValue;

    public Result()
    {
        this(0, 0.0, 0);
    }

    public Result(Integer iterationNumber, Double totalValue, Integer totalWeight)
    {
        this.iterationNumber = new SimpleIntegerProperty(iterationNumber);
        this.totalValue = new SimpleDoubleProperty(totalValue);
        this.totalWeight = new SimpleIntegerProperty(totalWeight);
    }

    public double getIterationNumber()
    {
        return iterationNumber.get();
    }

    public void setIterationNumber(int iterationNumber)
    {
        this.iterationNumber.set(iterationNumber);
    }

    public IntegerProperty iterationNumberProperty()
    {
        return iterationNumber;
    }

    public double getTotalWeight()
    {
        return totalWeight.get();
    }

    public void setTotalWeight(int totalWeight)
    {
        this.totalWeight.set(totalWeight);
    }

    public IntegerProperty totalWeightProperty()
    {
        return totalWeight;
    }

    public double getTotalValue()
    {
        return totalValue.get();
    }

    public void setTotalValue(double totalValue)
    {
        this.totalValue.set(totalValue);
    }

    public DoubleProperty totalValueProperty()
    {
        return totalValue;
    }
}
