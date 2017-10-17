package com.example.bartek.log_work_android;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class WorkedHoursSumCounterTest {
    @Test
    public void shouldInitializeSumCounterWithZero() {
        // when
        WorkedHoursSumCounter sumCounter = new WorkedHoursSumCounter();

        // then
        assertTrue(sumCounter.isEmpty());
        assertEquals(0.0, sumCounter.get());
    }

    @Test
    public void shouldDecreaseSumOfWorkedHours() {
        // given
        WorkedHoursSumCounter sumCounter = new WorkedHoursSumCounter(30);
        double before = sumCounter.get();

        // when
        sumCounter.decreaseBy(10);

        // then
        assertEquals(30.0, before);
        assertEquals(20.0, sumCounter.get());
    }

    @Test
    public void shouldCheckIsEmptySumCounter() {
        // given
        WorkedHoursSumCounter sumCounter1 = new WorkedHoursSumCounter(0);
        WorkedHoursSumCounter sumCounter2 = new WorkedHoursSumCounter(20);

        // when
        boolean first = sumCounter1.isEmpty();
        boolean second = sumCounter2.isEmpty();

        // then
        assertTrue(first);
        assertFalse(second);
        assertEquals(0.0, sumCounter1.get());
        assertEquals(20.0, sumCounter2.get());
    }

    @Test
    public void shouldEmptySumCounter() {
        // given
        WorkedHoursSumCounter sumCounter = new WorkedHoursSumCounter(20);
        boolean isEmpty = sumCounter.isEmpty();
        double sumCounterValue = sumCounter.get();

        // when
        sumCounter.empty();

        // then
        assertFalse(isEmpty);
        assertTrue(sumCounter.isEmpty());
        assertEquals(20.0, sumCounterValue);
        assertEquals(0.0, sumCounter.get());
    }
}