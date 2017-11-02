package com.geebeelicious.geebeelicious.models.grossmotor;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.geebeelicious.geebeelicious.R;

import java.util.Random;

/**
 * The MusicPlayer class contains
 * the music to be used during the
 * Gross Motor Test. The class allows
 * music to be played on the device
 * during the test.
 *
 * @author Katrina Lacsamana
 * @since 03/21/2016
 */
public class MusicPlayer {
    /**
     * Context of the music player
     */
    private Context context;

    /**
     * Media player to play the sound.
     */
    private MediaPlayer mediaPlayer;

    /**
     * Resource IDs of the choices in music.
     */
    private int[] music;

    /**
     * Resource IDs of the music already used.
     */
    private int[] usedMusic;

    /**
     * Number of music used.
     */
    private int usedCount;

    /**
     * Constructor.
     *
     * @param context {@link #context}
     */
    public MusicPlayer(Context context) {
        this.context = context;
        music = new int[8];
        usedMusic = new int[3];
        usedCount = 0;
        music[0] = R.raw.gross_motor_1;
        music[1] = R.raw.gross_motor_2;
        music[2] = R.raw.gross_motor_3;
        music[3] = R.raw.gross_motor_4;
        music[4] = R.raw.gross_motor_5;
        music[5] = R.raw.gross_motor_6;
        music[6] = R.raw.gross_motor_7;
        music[7] = R.raw.gross_motor_8;
    }

    /**
     * Check whether {@code key} is in {@code array}
     *
     * @param array to be checked
     * @param key   to be searched
     * @return true if {@code} array contains {@code key}. Else, return false.
     */
    private boolean checkDuplicates(int[] array, int key) {
        for (int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set random song.
     *
     * @param duration duration of how long the song will be played.
     */
    public void setRandomSong(int duration) {
        Random random = new Random((int) System.nanoTime());
        int randomSong;
        boolean isFound = false;

        while (!isFound) {
            int temp = 0;
            boolean isUsed = false;
            while (!isUsed) {
                temp = random.nextInt(7);
                if (!checkDuplicates(usedMusic, temp)) {
                    isUsed = true;
                    break;
                }
            }
            randomSong = music[temp];
            mediaPlayer = MediaPlayer.create(context, randomSong);
            if (mediaPlayer.getDuration() >= duration) {
                isFound = true;
                usedMusic[usedCount] = temp;
                usedCount++;
                break;
            }
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(false);
    }

    /**
     * Plays music.
     */
    public void playMusic() {
        mediaPlayer.start();
    }

    /**
     * Stops the music from playing.
     */
    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer = null;
    }


}
