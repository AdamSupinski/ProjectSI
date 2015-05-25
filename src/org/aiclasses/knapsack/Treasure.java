package org.aiclasses.knapsack;

/**
 * Represents single item to pick by thief
 */
public class Treasure
{
    private int weight;
    private double value;

    public Treasure(int weight, double value)
    {
        this.weight = weight;
        this.value = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Treasure treasure = (Treasure) o;

        return weight == treasure.weight && Double.compare(treasure.value, value) == 0;

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = weight;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int getWeight()
    {
        return weight;
    }

    public double getValue()
    {
        return value;
    }
}
