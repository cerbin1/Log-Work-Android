package com.example.bartek.log_work_android;

public class WorkedHoursSumCounter {
    private double sum;

    public WorkedHoursSumCounter() {
        sum = 0;
    }

    public WorkedHoursSumCounter(double sum) {
        this.sum = sum;
    }

    public double get() {
        return sum;
    }

    public void decreaseBy(double workedHours) {
        sum -= workedHours;
    }

    public boolean isEmpty() {
        return sum  == 0;
    }

    public void empty() {
        sum = 0;
    }
}
