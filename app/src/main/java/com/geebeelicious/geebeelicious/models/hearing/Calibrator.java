package com.geebeelicious.geebeelicious.models.hearing;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * The Calibrator class functions to calibrate
 * the earphones used by the device. The
 * volume will be adjusted depending on the
 * device.
 * <p>
 * The following class is based on the Calibration.java class created by
 * Reece Stevens (2014). The source code is available under the MIT License and
 * is published through a public GitHub repository:
 * https://github.com/ReeceStevens/ut_ewh_audiometer_2014/blob/master/app/src/main/java/ut/ewh/audiometrytest/Calibration.java
 *
 * @author Reece Stevens
 * @author Katrina Lacsamana
 * @version 03/07/2016
 */

public class Calibrator {

    final private int sampleRate = 44100;
    final private int numSamples = 4 * sampleRate;
    final private int bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
    final private int frequencies[] = {500, 1000, 2000, 4000, 6000, 8000};
    final private int volume = 30000;
    final private double gain = 0.0044;
    final private double alpha = 0.9;
    final private double[] dbHLCorrectionCoefficients = {13.5, 7.5, 11.5, 12, 16, 15.5};

    private double[] inputSignalImaginary = new double[2048];
    private static boolean isRunning = true;
    private SoundHelper soundHelper;
    private double[] calibrationArray;
    private Context context;
    private boolean isDone = false;
    private boolean isValid = false;

    public Calibrator(Context context) {
        soundHelper = new SoundHelper(numSamples, sampleRate);
        calibrationArray = new double[frequencies.length];
        this.context = context;
        isDone = true;
        isValid = false;
    }

    public void resetCalibrator() {
        soundHelper = new SoundHelper(numSamples, sampleRate);
        calibrationArray = new double[frequencies.length];
        isDone = true;
        isValid = false;
    }

    private int newBitReverse(int i) {
        int a = 0;
        while (i != 0) {
            a <<= 1;
            a |= (i & 1);
            i >>= 1;
        }
        return a;
    }

    //FFT (Fast Fourier Transform) Analysis
    private double[] fftAnalysis(double[] inputReal, double[] inputImaginary) {
        int n = 2048;
        int nu = 11;
        double[] bufferReal = new double[n];
        double[] shortenedReal = new double[n];
        double[][] real;
        double[][] imaginary;
        int step;
        double[] transformResult;

        //Shorten buffer data to a power of two
        for (int i = 0; i < n; i++) {
            shortenedReal[i] = inputReal[i];
        }

        //Compute coefficients for everything ahead of time
        real = new double[nu + 1][n];
        int counter = 2;
        for (int i = 1; i <= nu; i++) {
            for (int j = 0; j < n; j++) {
                real[i][j] = Math.cos(((double) 2) * Math.PI * ((double) j) / ((double) counter));
            }
            counter *= 2;
        }

        imaginary = new double[nu + 1][n];
        counter = 2;
        for (int i = 1; i <= nu; i++) {
            for (int j = 0; j < n; j++) {
                imaginary[i][j] = -1 * Math.sin(((double) 2) * Math.PI * ((double) j) / ((double) counter));
            }
            counter *= 2;
        }

        //populate bufferReal with inputReal in bit-reversed order
        for (int i = 0; i < shortenedReal.length; i++) {
            int reversedBit = newBitReverse(i);
            bufferReal[i] = shortenedReal[reversedBit];
        }

        //Begin Fast Fourier Transform (FFT)
        step = 1;
        for (int level = 1; level <= nu; level++) {
            int incrementValue = step * 2;
            for (int j = 0; j < step; j++) {
                for (int i = j; i < n; i += incrementValue) {
                    double realCoefficient = real[level][j];
                    double imaginaryCoefficient = imaginary[level][j];
                    realCoefficient *= bufferReal[i + step];
                    imaginaryCoefficient *= bufferReal[i + step];
                    bufferReal[i + step] = bufferReal[i];
                    inputImaginary[i + step] = inputImaginary[i];
                    bufferReal[i + step] -= realCoefficient;
                    inputImaginary[i + step] -= imaginaryCoefficient;
                    bufferReal[i] += realCoefficient;
                    inputImaginary[i] += imaginaryCoefficient;
                }
            }
            step *= 2;
        }

        transformResult = new double[bufferReal.length];
        //Calculate magnitude of FFT coefficients
        for (int i = 0; i < bufferReal.length; i++) {
            transformResult[i] = sqrt(pow(bufferReal[i], 2) + pow(inputImaginary[i], 2));
        }

        return transformResult;
    }

    private double[] dbListen(int frequency) {
        double rmsArray[] = new double[5];
        for (int i = 0; i < rmsArray.length; i++) {
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            short[] buffer = new short[bufferSize];
            int bufferReadResult;
            double[] inputSignal;
            double[] outputSignal;
            int k;

            try {
                audioRecord.startRecording();
            } catch (IllegalStateException e) {

            }
            bufferReadResult = audioRecord.read(buffer, 0, buffer.length);
            //Convert buffer from short[] to double[]
            inputSignal = new double[buffer.length];
            for (int j = 0; j < buffer.length; j++) {
                inputSignal[j] = (double) buffer[j];
            }

            outputSignal = fftAnalysis(inputSignal, inputSignalImaginary);
            //Select value from transform array corresponding to desired frequency
            k = frequency * 2048 / sampleRate;
            rmsArray[i] = outputSignal[k];
            //FFT Decibel Calculation
            audioRecord.stop();
            audioRecord.release();
        }

        return rmsArray;
    }

    public static void stopThread() {
        isRunning = false;
    }

    private static void startThread() {
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void endCalibration() {
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }

    private void writeToFile(byte[] calibrationByteArray) {
        try {
            FileOutputStream fos = context.openFileOutput("HearingTestCalibrationPreferences", Context.MODE_PRIVATE);
            try {
                fos.write(calibrationByteArray);
                fos.close();
            } catch (IOException ioe) {

            }
        } catch (FileNotFoundException fe) {

        }
        endCalibration();
    }

    private boolean isValidCalibration() {
        for (double d : calibrationArray) {
            Double temp = new Double(d);
            if (temp.isInfinite()) {
                return false;
            }
        }
        isValid = true;
        return true;
    }

    public boolean isValid() {
        return isValid;
    }

    public void calibrate() {
        int counter = 0;
        startThread();
        for (int i = 0; i < frequencies.length; i++) {
            int frequency = frequencies[i];
            final float increment = (float) (Math.PI) * frequency / sampleRate;
            double backgroundRms[];
            double soundRms[];
            double resultingRms[];
            double resultingDb[];
            double rmsSum = 0;
            double dbAverage = 0;

            AudioTrack audioTrack = soundHelper.playSound(soundHelper.generateSound(increment, volume));

            if (!isRunning()) {
                return;
            }

            backgroundRms = dbListen(frequency);
            audioTrack.play();
            soundRms = dbListen(frequency);

            resultingRms = new double[5];
            resultingDb = new double[5];

            for (int j = 0; j < resultingRms.length; j++) {
                resultingRms[j] = soundRms[j] / backgroundRms[j];
                resultingDb[j] = 20 * log10(resultingRms[j]) + 70;
                resultingDb[j] -= dbHLCorrectionCoefficients[i];
            }

            for (double rms : resultingRms) {
                if (rms > 0) {
                    rmsSum += rms;
                }
            }

            for (int j = 0; j < resultingDb.length; j++) {
                dbAverage += resultingDb[j];
            }
            dbAverage /= (resultingDb.length - 1);
            calibrationArray[i] = dbAverage / volume;

            if (!isRunning()) {
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }

            audioTrack.release();
        }

        byte calibrationByteArray[] = new byte[calibrationArray.length * 8];
        for (int i = 0; i < calibrationArray.length; i++) {
            byte tempByteArray[] = new byte[8];
            ByteBuffer.wrap(tempByteArray).putDouble(calibrationArray[i]);
            for (int j = 0; j < tempByteArray.length; j++) {
                calibrationByteArray[counter] = tempByteArray[j];
                counter++;
            }
        }

        if (isValidCalibration()) {
            writeToFile(calibrationByteArray);
        } //else error message to fix earphone distance
    }


}
