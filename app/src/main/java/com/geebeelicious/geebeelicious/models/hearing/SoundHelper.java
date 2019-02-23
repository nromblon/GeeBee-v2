package com.geebeelicious.geebeelicious.models.hearing;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * The SoundHelper class contains functionality to
 * generate and play sounds for the hearing test.
 *
 * @author Katrina Lacsamana
 * @since 03/11/2016
 */
public class SoundHelper {

    /**
     * Number of samples.
     */
    private int numSamples;

    /**
     * Sample rate of the test.
     */
    private int sampleRate;

    /**
     * Constructor.
     *
     * @param numSamples {@link #numSamples}
     * @param sampleRate {@link #sampleRate}
     */
    public SoundHelper(int numSamples, int sampleRate) {
        this.numSamples = numSamples;
        this.sampleRate = sampleRate;
    }

    /**
     * Generate sound.
     *
     * @param increment increment value for the sound
     * @param volume    volume of the sound.
     * @return generated sound.
     */
    public byte[] generateSound(float increment, int volume) {
        float angle = 0;
        double sample[] = new double[numSamples];
        byte generatedSound[] = new byte[2 * numSamples];
        int index = 0;
        for (int i = 0; i < numSamples; i++) {
            sample[i] = Math.sin(angle);
            angle += increment;
        }

        for (final double dVal : sample) {
            final short val = (short) ((dVal * volume)); //volume controlled by the value multiplied by dVal; max value is 32767
            generatedSound[index++] = (byte) (val & 0x00ff);
            generatedSound[index++] = (byte) ((val & 0xff00) >>> 8);
        }

        return generatedSound;
    }

    /**
     * Get AudioTrack to be played
     *
     * @param generatedSound generated sound made for the test.
     * @return sound to be played.
     */
    public AudioTrack playSound(byte[] generatedSound) {
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, generatedSound.length, AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSound, 0, generatedSound.length);
        return audioTrack;
    }

    /**
     * Play sound.
     *
     * @param generatedSound generated sound made for the test.
     * @param ear            ear tested
     * @return track played.
     */
    public AudioTrack playSound(byte[] generatedSound, int ear) {
        AudioTrack audioTrack = playSound(generatedSound);
        if (ear == 0) {
            audioTrack.setStereoVolume(0, AudioTrack.getMaxVolume());
        } else {
            audioTrack.setStereoVolume(AudioTrack.getMaxVolume(), 0);
        }
        audioTrack.play();
        return audioTrack;
    }

}
