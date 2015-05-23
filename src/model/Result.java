package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

/**
 * Created by adam on 23.05.15.
 */
public class Result {
    private IntegerProperty iterationNumber;
    private DoubleProperty totalWeight;
    private DoubleProperty totalValue;

    public Result() {
        this(null, null, null);
    }

    public Result(IntegerProperty iterationNumber, DoubleProperty totalValue, DoubleProperty totalWeight) {
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

    public void setTotalWeight(double totalWeight) {
        this.totalWeight.set(totalWeight);
    }

    public DoubleProperty totalWeightProperty() {
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
