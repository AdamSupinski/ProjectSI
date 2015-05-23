package model;

import javafx.beans.property.DoubleProperty;

/**
 * Created by adam on 22.05.15.
 */
public class Item {
    private DoubleProperty weight;
    private DoubleProperty value;

    public Item() {
        this(null, null);
    }

    public Item(DoubleProperty weight, DoubleProperty value) {
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

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public DoubleProperty weightProperty() {
        return weight;
    }
}
