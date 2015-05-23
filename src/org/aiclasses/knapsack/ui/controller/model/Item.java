package org.aiclasses.knapsack.ui.controller.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public class Item {
    private IntegerProperty weight;
    private DoubleProperty value;

    public Item() {
        this(null, null);
    }

    public Item(IntegerProperty weight, DoubleProperty value) {
        this.weight = weight;
        this.value = value;
    }

    public double getValue() {
        return value.get();
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(int weight) {
        this.weight.set(weight);
    }

    public IntegerProperty weightProperty() {
        return weight;
    }
}
