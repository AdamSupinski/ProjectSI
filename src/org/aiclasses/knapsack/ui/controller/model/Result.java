package org.aiclasses.knapsack.ui.controller.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public class Result {
    private IntegerProperty iterationNumber;
    private IntegerProperty totalWeight;
    private DoubleProperty totalValue;

    public Result() {
        this(null, null, null);
    }

    public Result(IntegerProperty iterationNumber, DoubleProperty totalValue, IntegerProperty totalWeight) {
        this.iterationNumber = iterationNumber;
        this.totalValue = totalValue;
        this.totalWeight = totalWeight;
    }

    public double getIterationNumber() {
        return iterationNumber.get();
    }

    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber.set(iterationNumber);
    }

    public IntegerProperty iterationNumberProperty() {
        return iterationNumber;
    }

    public double getTotalWeight() {
        return totalWeight.get();
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight.set(totalWeight);
    }

    public IntegerProperty totalWeightProperty() {
        return totalWeight;
    }

    public double getTotalValue() {
        return totalValue.get();
    }

    public void setTotalValue(double totalValue) {
        this.totalValue.set(totalValue);
    }

    public DoubleProperty totalValueProperty() {
        return totalValue;
    }
}
