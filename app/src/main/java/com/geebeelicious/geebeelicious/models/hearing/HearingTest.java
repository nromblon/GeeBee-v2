package com.geebeelicious.geebeelicious.models.hearing;

import android.content.Context;
import android.media.AudioTrack;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The HearingTest class functions
 * to help manage the proctoring of the
 * audiometry hearing test.
 * <p>
 * The following class is based on the TestProctoring.java class created by
 * Reece Stevens (2014). The source code is available under the MIT License and
 * is published through a public GitHub repository:
 * https://github.com/ReeceStevens/ut_ewh_audiometer_2014/blob/master/app/src/main/java/ut/ewh/audiometrytest/TestProctoring.java
 *
 * @author Reece Stevens
 * @author Katrina Lacsamana
 * @version 03/11/2016
 */
public class HearingTest {

    final private int duration = 1;
    final private int sampleRate = 44100;
    final private int numSamples = duration * sampleRate;
    final private int volume = 32767;
    final private int[] testingFrequencies = {500, 1000, 2000};
    final private double mGain = 0.0044;
    final private double mAlpha = 0.9;

    /**
     * Whether the sound was heard or not.
     */
    private boolean isHeard = false;

    /**
     * Whether the sound is in loop or not.
     */
    private boolean inLoop = true;

    /**
     * Whether the test is done or not.
     */
    private boolean isDone = false;

    /**
     * Whether the test is inside the gap between samples or not.
     */
    private boolean isGap = false;

    /**
     * Number of times the user responded within the gap.
     */
    private int hasCheated = 0;

    /**
     * Whether sound is currently played.
     */
    private static boolean isRunning = true;

    /**
     * Threshold for the right ear.
     */
    private double[] thresholdsRight = {0, 0, 0};

    /**
     * Threshold for the left ear.
     */
    private double[] thresholdsLeft = {0, 0, 0};

    /**
     * Constructor.
     */
    public HearingTest() {
        this.isHeard = false;
        this.inLoop = true;
        this.isDone = false;
        isRunning = true;
        thresholdsRight = new double[]{0, 0, 0};
        thresholdsLeft = new double[]{0, 0, 0};
    }

    /**
     * Get random gap time between sounds.
     *
     * @return gap time.
     */
    private int getRandomGapDuration() {
        int time;
        double random = Math.random();
        if (random < 0.1) {
            time = 2000;
        } else if (random < 0.2 && random >= 0.1) {
            time = 3000;
        } else if (random < 0.3 && random >= 0.2) {
            time = 4000;
        } else if (random < 0.4 && random >= 0.3) {
            time = 5000;
        } else if (random < 0.5 && random >= 0.4) {
            time = 6000;
        } else if (random < 0.6 && random >= 0.5) {
            time = 2500;
        } else if (random < 0.7 && random >= 0.6) {
            time = 3500;
        } else if (random < 0.8 && random >= 0.7) {
            time = 4500;
        } else if (random < 0.9 && random >= 0.8) {
            time = 5500;
        } else {
            time = 6500;
        }
        return time;
    }

    /**
     * Gets calibration data.
     *
     * @param context current context.
     * @return calibration data.
     */
    public double[] getCalibrationData(Context context) {
        byte calibrationByteData[] = new byte[24];

        try {
            FileInputStream fis = context.openFileInput("HearingTestCalibrationPreferences");
            fis.read(calibrationByteData, 0, 24);
            fis.close();
        } catch (IOException e) {
        }

        final double calibrationArray[] = new double[3];
        int counter = 0;
        for (int i = 0; i < calibrationArray.length; i++) {
            byte tempByteBuffer[] = new byte[8];
            for (int j = 0; j < tempByteBuffer.length; j++) {
                tempByteBuffer[j] = calibrationByteData[counter];
                counter++;
            }
            calibrationArray[i] = ByteBuffer.wrap(tempByteBuffer).getDouble();
        }
        return calibrationArray;
    }

    /**
     * Run the test.
     *
     * @param calibrationArray calibration data.
     */
    public void performTest(double[] calibrationArray) {
        SoundHelper soundHelper = new SoundHelper(numSamples, sampleRate);
        int tempResponse;

        for (int e = 0; e < 2; e++) {
            for (int i = 0; i < testingFrequencies.length; i++) {
                int frequency = testingFrequencies[i];
                float increment = (float) (Math.PI) * frequency / sampleRate;
                int maxVolume = volume;
                int minVolume = 0;

                //Loop for each individual sample using binary search algorithm
                for (; ; ) {
                    tempResponse = 0;
                    int actualVolume = (minVolume + maxVolume) / 2;
                    if ((maxVolume - minVolume) < 50) {
                        if (e == 0) {
                            thresholdsRight[i] = actualVolume * calibrationArray[i];
                        } else if (e == 1) {
                            thresholdsLeft[i] = actualVolume * calibrationArray[i];
                        } else {

                        }
                        break;
                    } else {
                        for (int z = 0; z < 3; z++) {
                            AudioTrack audioTrack;
                            isHeard = false;
                            hasCheated = 0;
                            if (!isRunning) {
                                return;
                            }
                            try {
                                isGap = true;
                                Thread.sleep(getRandomGapDuration());
                            } catch (InterruptedException ie) {

                            }

                            isGap = false;
                            audioTrack = soundHelper.playSound(soundHelper.generateSound(increment, actualVolume), e);
                            try {
                                Thread.sleep(1000); //1 second to allow sound to play and wait for response
                            } catch (InterruptedException ie) {

                            }
                            if (isHeard) {
                                tempResponse++;
                            }

                            audioTrack.release();

                            //Check if first two test were positive, skips the third to speed up the test
                            if (tempResponse >= 2) {
                                break;
                            }
                            //Check if first two were misses and skips the third
                            if (z == 1 && tempResponse == 0) {
                                break;
                            }
                        }
                        //If response was 2/3, register as heard
                        if (tempResponse >= 2) {
                            maxVolume = actualVolume;
                        } else {
                            minVolume = actualVolume;
                        }
                    } //Continue with test
                }
            }
            //Run
        }
        inLoop = false;
        isDone = true;
    }

    /**
     * Gets {@link #hasCheated}.
     *
     * @return {@link #hasCheated}
     */
    public int hasCheated() {
        return hasCheated;
    }

    /**
     * Sets {@link #hasCheated}.
     */
    public void setCheated() {
        hasCheated++;
    }

    /**
     * Gets {@link #isGap}
     *
     * @return {@link #isGap}
     */
    public boolean isGap() {
        return isGap;
    }

    /**
     * Sets {@link #isHeard} to true.
     */
    public void setHeard() {
        isHeard = true;
    }

    /**
     * Gets {@link #isHeard}.
     *
     * @return {@link #isHeard}.
     */
    public boolean isHeard() {
        return isHeard;
    }

    /**
     * Gets {@link #isDone}
     *
     * @return {@link #isDone}
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Gets {@link #inLoop}.
     *
     * @return {@link #inLoop}
     */
    public boolean isInLoop() {
        return inLoop;
    }

    /**
     * Sets {@link #isRunning} to false.
     */
    public void setIsNotRunning() {
        isRunning = false;
    }

    /**
     * Gets {@link #isRunning}.
     *
     * @return {@link #isRunning}
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Get the average of the test results.
     *
     * @param testResults results of the test
     * @return average of the results
     */
    private double getPureToneAverage(double[] testResults) {
        double result = 0;
        for (double d : testResults) {
            result += d;
        }
        return (result / testResults.length);
    }

    /**
     * Get string results per frequency.
     *
     * @param testResults
     * @return
     */
    private String getResultsPerFrequency(double[] testResults) {
        String result = "";

        for (int i = 0; i < testResults.length; i++) {
            result += (testingFrequencies[i] + " Hz: " + String.format("%.2f", testResults[i]) + " db HL\n");

        }
        return result;
    }

    /**
     * Get the string value of the result.
     *
     * @param result result of the test
     * @return result
     */
    private String interpretPureToneAverage(double result) {
        if (result <= 20) {
            return "Normal Hearing";
        } else if (result >= 21 && result <= 40) {
            return "Mild Hearing Loss";
        } else if (result >= 41 && result <= 55) {
            return "Moderate Hearing Loss";
        } else if (result >= 56 && result <= 70) {
            return "Moderately-Severe Hearing Loss";
        } else if (result >= 71 && result <= 90) {
            return "Severe Hearing Loss";
        } else {
            return "Profound Hearing Loss";
        }
    }

    /**
     * Get the average result.
     *
     * @param testResults results
     * @return average result
     * @see #getResults()
     */
    private String getPureToneAverageResults(double[] testResults) {
        double ptaResult = getPureToneAverage(testResults);
        String result = "Pure Tone Average: " + String.format("%.2f", ptaResult) + " dB HL" +
                "\nYou have " + interpretPureToneAverage(ptaResult) + ".";
        return result;
    }

    /**
     * Get results for both ears.
     *
     * @return results for both ears.
     */
    public String getResults() {
        String result = "Right Ear\n" + getResultsPerFrequency(thresholdsRight) + getPureToneAverageResults(thresholdsRight)
                + "\n\nLeft Ear\n" + getResultsPerFrequency(thresholdsLeft) + getPureToneAverageResults(thresholdsLeft);
        return result;
    }

    /**
     * Get the results for the specific ear
     *
     * @param ear ear you want to get the results of
     * @return results for the ear.
     */
    public String getPureToneAverageInterpretation(String ear) {
        double[] thresholds;
        if (ear.equals("Right")) {
            thresholds = thresholdsRight;
        } else {
            thresholds = thresholdsLeft;
        }
        return interpretPureToneAverage(getPureToneAverage(thresholds));
    }


}


