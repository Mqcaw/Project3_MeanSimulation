package me.jackson.project_3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
    private String name;
    private int iterations;
    private int maxValue;
    private double mean;
    private double accuracy;

    public Game(String name, int iterations, int maxValue) {
        this.name = name;
        this.iterations = iterations;
        this.maxValue = maxValue;

        run();
    }

    public Game(String name, int iterations, int maxValue, double mean, double accuracy) {
        this.name = name;
        this.iterations = iterations;
        this.maxValue = maxValue;
        this.mean = mean;
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIterations() {
        return iterations;
    }
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
    public int getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    public double getMean() {
        return mean;
    }
    public void setMean(double mean) {
        this.mean = mean;
    }
    public double getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public void run() {
        Random random = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        float expectedMean = maxValue / 2;

        //generate random list
        for (int i = 0; i < iterations; i++) {
            list.add(random.nextInt(maxValue));

        }

        //calculate mean
        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        mean = sum / list.size();


        accuracy = 100 - Math.abs(((expectedMean - mean) / expectedMean) * 100);

    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", iterations=" + iterations +
                ", maxValue=" + maxValue +
                ", mean=" + mean +
                ", accuracy=" + accuracy +
                '}';
    }
}
