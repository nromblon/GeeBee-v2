package com.geebeelicious.geebeelicious.models.bmi;

/**
 * BMICalculator class is used by the monitoringFragment
 * for calculating BMI.
 * BMI percentile data retrieved from <a href="http://www.who.int/growthref/who2007_bmi_for_age/en/">WHO 2007</a>
 *
 * @author Mary Grace Malana
 */
public class BMICalculator {

    /**
     * Array of BMI percentile values for underweight, overweight, and obese for boys with
     * ages 5-19 years old. The limit for underweight is 5th percentile, overweight is 85th percentile,
     * and obese is 95th percentile.
     * <p>
     * bmi[x][y]
     * x: 0 = P5, 1 = P85, 2 = P95 //P5 = 3rd percentile
     * y: age - 5
     */
    private final static float[][] boyBMI = {
            {13.1f, 13.2f, 13.3f, 13.4f, 13.6f,
                    14.9f, 14.2f, 14.6f, 15.1f, 15.6f,
                    16.2f, 16.7f, 17.1f, 17.5f, 17.8f},
            {16.7f, 16.8f, 17.1f, 17.5f, 18.0f,
                    18.6f, 19.3f, 20.1f, 20.9f, 21.9f,
                    22.8f, 23.7f, 24.4f, 25.0f, 25.6f},
            {18.1f, 18.3f, 18.8f, 19.4f, 20.1f,
                    21.0f, 22.0f, 23.1f, 24.2f, 25.3f,
                    26.4f, 27.3f, 28.0f, 28.6f, 29.1f}
    };

    /**
     * Array of BMI percentile values for underweight, overweight, and obese for girls with
     * ages 5-19 years old. The limit for underweight is 5th percentile, overweight is 85th percentile,
     * and obese is 95th percentile.
     * <p>
     * bmi[x][y]
     * x: 0 = P5, 1 = P85, 2 = P95 //P5 = 3rd percentile
     * y: age - 5
     */
    private final static float[][] girlBMI = {
            {12.9f, 12.8f, 12.9f, 13.0f, 13.3f,
                    13.6f, 14.0f, 14.6f, 15.1f, 15.6f,
                    16.1f, 16.4f, 16.6f, 16.7f, 16.7f},
            {16.9f, 17.1f, 17.4f, 17.8f, 18.4f,
                    19.1f, 20.0f, 20.9f, 21.9f, 22.9f,
                    23.7f, 24.2f, 24.7f, 24.9f, 25.1f},
            {18.6f, 18.9f, 19.4f, 20.2f, 21.0f,
                    22.1f, 23.2f, 24.4f, 25.6f, 26.7f,
                    27.6f, 28.2f, 28.6f, 28.9f, 29.0f}
    };

    /**
     * Compute the BMI of the person given the {@code height}
     * and {@code weight}.
     * <p>
     * The method uses the metric system as its unit of measurement.
     *
     * @param height height of person in centimeters.
     * @param weight weight of person in kilograms.
     * @return BMI of the person.
     */
    public static float computeBMIMetric(int height, int weight) {
        if (height == 0) {
            return 0;
        } else {
            return ((float) weight / height / height) * 10000;
        }
    }

    /**
     * Get the weight category of the person given the parameters.
     * Calls {@link #getBMIResult(int, float, float[][])}
     *
     * @param isGirl     gender of the person, true if female, false if male.
     * @param age        age of the person
     * @param patientBMI BMI of the person
     * @return corresponding int value of the weight category of the person.
     * @see #getBMIResultString(boolean, int, float)
     */
    public static int getBMIResult(boolean isGirl, int age, float patientBMI) {
        int ageIndex = age - 5;


        if (ageIndex < 0 || ageIndex > boyBMI[0].length - 1) {
            return 4;
        } else if (isGirl) {
            return getBMIResult(ageIndex, patientBMI, girlBMI);
        } else {
            return getBMIResult(ageIndex, patientBMI, boyBMI);
        }
    }

    /**
     * Get the weight category of the person given the parameters.
     * Calls {@link #getBMIResult(boolean, int, float)}
     *
     * @param isGirl     gender of the person, true if female, false if male.
     * @param age        age of the person
     * @param patientBMI BMI of the person
     * @return weight category of the person.
     */
    public static String getBMIResultString(boolean isGirl, int age, float patientBMI) {
        int result = getBMIResult(isGirl, age, patientBMI);

        switch (result) {
            case 0:
                return "Underweight";
            case 1:
                return "Normal";
            case 2:
                return "Overweight";
            case 3:
                return "Obese";
        }

        return "N/A";
    }

    /**
     * Get the weight category of the person given the parameters.
     *
     * @param ageIndex   index adjusted age of the person.
     * @param patientBMI BMI of the person
     * @param bmiChart   bmiChart can either be {@link #boyBMI} or  {@link #girlBMI}
     *                   depending on the age of the person
     * @return weight category of the person.
     * @see #getBMIResultString(boolean, int, float)
     * @see #getBMIResult(boolean, int, float)
     */
    private static int getBMIResult(int ageIndex, float patientBMI, float bmiChart[][]) {
        if (patientBMI < bmiChart[0][ageIndex]) { //less than 5P
            return 0;
        } else if (patientBMI > bmiChart[2][ageIndex]) { //greater or equal to 95P
            return 3;
        } else if (patientBMI > bmiChart[1][ageIndex]) { //85P to less than 95P
            return 2;
        } else {
            return 1;
        }
    }
}
