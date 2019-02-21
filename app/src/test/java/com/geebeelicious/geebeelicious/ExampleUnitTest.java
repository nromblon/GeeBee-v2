package com.geebeelicious.geebeelicious;

import com.geebeelicious.geebeelicious.models.bmi.BMICalculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void computeBMI() throws Exception {
        assertEquals(20f, BMICalculator.computeBMIMetric(158, 50), 0.1f);
    }

    @Test
    public void computeBMIPercentile() throws Exception {
        assertEquals(0, BMICalculator.getBMIResult(true, 5, 12.8f));
        assertEquals(3, BMICalculator.getBMIResult(true, 5, 19));
        assertEquals(2, BMICalculator.getBMIResult(true, 5, 17));
        assertEquals(1, BMICalculator.getBMIResult(true, 5, 15));
        assertEquals(4, BMICalculator.getBMIResult(true, 1, 17));
        assertEquals(4, BMICalculator.getBMIResult(true, 21, 17));
    }

    @Test
    public void computeBMIPercentileString() throws Exception {
        assertEquals("Underweight", BMICalculator.getBMIResultString(true, 5, 11));
        assertEquals("Obese", BMICalculator.getBMIResultString(true, 5, 19));
        assertEquals("Overweight", BMICalculator.getBMIResultString(true, 5, 17));
        assertEquals("Normal", BMICalculator.getBMIResultString(true, 5, 14));
        assertEquals("N/A", BMICalculator.getBMIResultString(true, 2, 17));
    }
}