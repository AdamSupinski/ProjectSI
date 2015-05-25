package org.aiclasses.knapsack.ga;

import org.aiclasses.knapsack.Treasure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Solving Knapsack using genetic algorithm
 * Complexity mostly depends on number of iterations and population size
 */
public class KnapsackGA
{
    private int populationSize;
    private int numberOfIterations;
    private double crossoverRate;
    private double mutationProbability;
    private Random random;

    private OnNextPopulationListener listener;

    public interface OnNextPopulationListener
    {
        void onNextPopulation(List<Treasure> solution, int population);
    }

    public KnapsackGA(int populationSize, int numberOfIterations, double crossoverRate, double mutationProbability)
    {
        this.populationSize = populationSize;
        this.numberOfIterations = numberOfIterations;
        this.crossoverRate = crossoverRate;
        this.mutationProbability = mutationProbability;
        random = new Random();
    }

    public List<Treasure> solve(Treasure[] treasures, int knapsackCapacity)
    {
        Population population = generateRandomPopulation(treasures.length);
        for (int i = 0; i < numberOfIterations; i++)
        {
            // first half of copiedPopulation will be used for crossover, second will remain the same
            Population copiedPopulation = copyPopulationTwice(population);

            // performing crossover
            List<Chromosome> availableToCrossover = new ArrayList<>(); // list for random picking
            for (int j = 0; j < populationSize; j++)
            {
                availableToCrossover.add(copiedPopulation.getChromosome(j));
            }
            int crossovers = (int) (populationSize * crossoverRate/2);
            for (int j = 0; j < crossovers; j++)
            {
                // picking 2 random chromosomes to crossover
                int firstToCrossoverIndex = random.nextInt(availableToCrossover.size());
                Chromosome firstToCrossover = availableToCrossover.get(firstToCrossoverIndex);
                availableToCrossover.remove(firstToCrossoverIndex);

                int secondToCrossoverIndex = random.nextInt(availableToCrossover.size());
                Chromosome secondToCrossover = availableToCrossover.get(secondToCrossoverIndex);
                availableToCrossover.remove(secondToCrossover);

                crossoverChromosomes(firstToCrossover, secondToCrossover);
            }

            // performing mutation and checking fitness of chromosomes
            // if fitness is -1 (thief can't carry those treasures) then it's mutated until he can
            for (int j = 0; j < populationSize * 2; j++)
            {
                if (random.nextDouble() <= mutationProbability)
                {
                    mutateChromosome(copiedPopulation.getChromosome(j));
                }
                while (rateChromosome(copiedPopulation.getChromosome(j), treasures, knapsackCapacity) == -1)
                {
                    mutateOneBitToZero(copiedPopulation.getChromosome(j));
                }
            }

            copiedPopulation.sortByScoreDesc();
            Population nextPopulation = new Population(populationSize);
            // picking chromosomes with probability
            // 25% best chromosomes are picked with 50% probability
            // next 25% group - 30%
            // next 25% group - 15%
            // worst 25% chromosomes - 5%
            for (int j = 0; j < populationSize; j++)
            {
                int group = random.nextInt(100);
                if (group < 50)
                {
                    nextPopulation.setChromosome(j, copiedPopulation.getChromosome(random.nextInt(populationSize / 2)));
                }
                else if (group < 80)
                {
                    nextPopulation.setChromosome(j, copiedPopulation.getChromosome(random.nextInt(populationSize / 2) + populationSize / 2));
                }
                else if (group < 95)
                {
                    nextPopulation.setChromosome(j, copiedPopulation.getChromosome(random.nextInt(populationSize / 2) + populationSize));
                }
                else
                {
                    nextPopulation.setChromosome(j, copiedPopulation.getChromosome(random.nextInt(populationSize / 2) + populationSize / 2 * 3));
                }
            }

            population = nextPopulation;

            if (listener != null)
            {
                listener.onNextPopulation(pickBestSolution(population, treasures), i + 1);
            }
        }

        return pickBestSolution(population, treasures);
    }

    /**
     * Picks best chromosome in population and converts it to list of treasures
     */
    private List<Treasure> pickBestSolution(Population population, Treasure[] treasures)
    {
        Chromosome bestChromosome = population.getChromosome(0);
        for (int i = 0; i < populationSize; i++)
        {
            if (bestChromosome.getScore() < population.getChromosome(i).getScore())
            {
                bestChromosome = population.getChromosome(i);
            }
        }

        List<Treasure> list = new ArrayList<>();
        for (int i = 0; i < bestChromosome.getNumOfBits(); i++)
        {
            if (bestChromosome.getBit(i))
            {
                list.add(treasures[i]);
            }
        }

        return list;
    }

    /**
     * create new population two times sized and put chromosomes twice
     */
    private Population copyPopulationTwice(Population population)
    {
        Population newPopulation = new Population(populationSize * 2);
        for (int i = 0; i < populationSize; i++)
        {
            newPopulation.setChromosome(i, new Chromosome(population.getChromosome(i)));
        }
        for (int i = populationSize; i < populationSize * 2; i++)
        {
            newPopulation.setChromosome(i, new Chromosome(population.getChromosome(i - populationSize)));
        }
        return newPopulation;
    }

    private Population generateRandomPopulation(int numberOfTreasures)
    {
        Population population = new Population(populationSize);
        for (int i = 0; i < populationSize; i++)
        {
            Chromosome chromosome = new Chromosome(numberOfTreasures);
            for (int j = 0; j < numberOfTreasures; j++)
            {
                chromosome.setBit(j, random.nextInt(2) == 1);
            }
            population.setChromosome(i, chromosome);
        }
        return population;
    }

    /**
     * Checks if items can be fit in knapsack. If so then it returns sum of items value.
     * Otherwise returns -1
     * It also sets field score in chromosome
     */
    private double rateChromosome(Chromosome chromosome, Treasure[] treasures, double knapsackCapacity)
    {
        double sumWeight = 0.0;
        double sumValues = 0.0;
        for (int i = 0; i < treasures.length; i++)
        {
            if (chromosome.getBit(i))
            {
                sumWeight += treasures[i].getWeight();
                sumValues += treasures[i].getValue();
            }
        }
        if (sumWeight <= knapsackCapacity)
        {
            chromosome.setScore(sumValues);
            return sumValues;
        }
        else
        {
            chromosome.setScore(-1);
            return -1;
        }
    }

    /**
     * Crossovers two chromosomes
     * example: bits in first chromosome 1100, bits in second chromosome 0011
     * bits in first chromosome will be changed to 0000, second to 1111
     */
    public void crossoverChromosomes(Chromosome firstChromosome, Chromosome secondChromosome)
    {
        Chromosome tempChromosome = new Chromosome(firstChromosome);
        int divideIndex = firstChromosome.getNumOfBits() / 2;
        for (int i = 0; i < divideIndex; i++)
        {
            firstChromosome.setBit(i, secondChromosome.getBit(i));
            secondChromosome.setBit(i, tempChromosome.getBit(i));
        }
    }

    /**
     * Changes random bit in chromosome
     */
    private void mutateChromosome(Chromosome chromosome)
    {
        int bit = random.nextInt(chromosome.getNumOfBits());
        chromosome.setBit(bit, !chromosome.getBit(bit));
    }

    /**
     * Takes random 1 bit and changes it to 0
     */
    private void mutateOneBitToZero(Chromosome chromosome)
    {
        ArrayList<Integer> onesIndex = new ArrayList<>();
        for (int i = 0; i < chromosome.getNumOfBits(); i++)
        {
            if (chromosome.getBit(i))
            {
                onesIndex.add(i);
            }
        }
        chromosome.setBit(onesIndex.get(random.nextInt(onesIndex.size())), false);
    }

    public void setListener(OnNextPopulationListener listener)
    {
        this.listener = listener;
    }
}