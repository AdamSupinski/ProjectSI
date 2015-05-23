package org.aiclasses.knapsack.dynamic;

import org.aiclasses.knapsack.Treasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Solving Knapsack using dynamic algorithm. Complexity O(n^2)
 */
public class KnapsackDynamic
{
    public List<Treasure> solve(Treasure[] treasures, int knapsackCapacity)
    {
        double d[] = new double[knapsackCapacity + 1];
        Treasure usedTreasures[][] = new Treasure[treasures.length][knapsackCapacity + 1];
        int treasureIndex = 0;
        for (Treasure treasure : treasures)
        {
            for (int i = knapsackCapacity; i >= treasure.getWeight(); i--)
            {
                if (treasure.getValue() + d[i - treasure.getWeight()] > d[i])
                {
                    d[i] = treasure.getValue() + d[i - treasure.getWeight()];
                    usedTreasures[treasureIndex][i] = treasure;
                }
            }
            treasureIndex++;
        }

        List<Treasure> pickedList = new ArrayList<>();
        int currentPos = knapsackCapacity;
        --treasureIndex;
        while (treasureIndex >= 0)
        {
            if (usedTreasures[treasureIndex][currentPos] != null)
            {
                pickedList.add(usedTreasures[treasureIndex][currentPos]);
                currentPos -= usedTreasures[treasureIndex][currentPos].getWeight();
            }
            treasureIndex--;
        }
        return pickedList;
    }

}
