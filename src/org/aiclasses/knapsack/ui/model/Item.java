package org.aiclasses.knapsack.ui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Item {
    private IntegerProperty weight;
    private DoubleProperty value;

    public Item() {
        this(0, 0.0);
    }

    public Item(Integer weight, Double value) {
        this.weight = new SimpleIntegerProperty(weight);
        this.value = new SimpleDoubleProperty(value);
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

    public int getWeight() {
        return weight.get();
    }

    public void setWeight(int weight) {
        this.weight.set(weight);
    }

    public IntegerProperty weightProperty() {
        return weight;
    }
}
