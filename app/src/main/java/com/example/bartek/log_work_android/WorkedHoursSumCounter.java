package com.example.bartek.log_work_android;

public class WorkedHoursSumCounter {
    private double sum;

    public WorkedHoursSumCounter() {
        sum = 0;
    }

    public WorkedHoursSumCounter(double sum) {
        if (sum < 0) {
            throw new IllegalArgumentException("Sum of worked hours can not be negative!");
        }
        this.sum = sum;
    }

    public double get() {
        return sum;
    }

    public void decreaseBy(double workedHours) {
        if (workedHours < 0) {
            throw new IllegalArgumentException("Worked hours can not be negative");
        }
        sum -= workedHours;
        if (sum < 0) {
            throw new IllegalArgumentException("Sum of worked hours can not be negative!");
        }
    }

    public boolean isEmpty() {
        return sum == 0;
    }

    public void empty() {
        sum = 0;
    }
}
