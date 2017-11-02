package com.geebeelicious.geebeelicious.models.finemotor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.widget.ImageView;

import com.geebeelicious.geebeelicious.R;

import java.util.Random;


/**
 * Serves as the helper class of FineMotorActivity
 * sets the conditions for each round of the test
 *
 * @author Mary Grace Malana
 * @since 25/03/2016
 */
public class FineMotorHelper {

    /**
     * Used to identify the source of a log message
     */
    private String TAG = "FineMotorHelper";

    /**
     * Max number of wrongs to pass the fine motor test.
     */
    private static final int MAX_NUM_WRONG = 2;

    /**
     * View for the path.
     */
    private ImageView imageViewPathToTrace;

    /**
     * Media player of playing sounds.
     */
    private MediaPlayer mp;

    /**
     * Flag whether the user was outside the path or not.
     */
    private boolean wasOutside = false;

    /**
     * Counter for the number of wrongs the user
     * has commited.
     */
    private int numWrongs = 0;

    /**
     * List of results for each round. result[i] is true if pass, false if fail
     */
    private boolean[] result = new boolean[3];

    /**
     * List of instructions for each round.
     */
    private int[] instructions;

    /**
     * Constructor.
     *
     * @param context              current context.
     * @param imageViewPathToTrace {@link #imageViewPathToTrace}
     */
    public FineMotorHelper(Context context, ImageView imageViewPathToTrace) {
        this.imageViewPathToTrace = imageViewPathToTrace;
        int pathNumber = getRandomPathDrawable();

        mp = MediaPlayer.create(context, R.raw.fine_motor_outside_path);
        mp.setLooping(true);

        imageViewPathToTrace.setImageResource(pathNumber);
        instructions = getInstructions(pathNumber);
    }

    /**
     * Gets {@link #result}.
     *
     * @return {@link #result}
     */
    public boolean[] getResults() {
        return result;
    }

    /**
     * Sets {@link #result}
     *
     * @param index index of the result to be set
     * @param isYes new value
     */
    public void setResult(int index, boolean isYes) {
        result[index] = isYes;
    }

    /**
     * Called if the user is outside the path.
     */
    public void doIfOutSideThePath() {
        if (!wasOutside) {
            mp.start();
            wasOutside = true;
            numWrongs++;
        }
    }

    /**
     * Check whether touch is within path.
     *
     * @return true if touch was outside, else false.
     */
    public boolean doIfWithinPath() {
        if (wasOutside) {
            pauseMp();
            wasOutside = false;
            return true;
        }
        return false;
    }

    /**
     * Starts the next test. resets the variables
     *
     * @param currentTest index of the current test
     * @return instructions for the next test.
     */
    public int doNextTest(int currentTest) {
        pauseMp();
        result[currentTest] = numWrongs <= MAX_NUM_WRONG;
        numWrongs = 0;
        return setInstructions(currentTest + 1);
    }

    /**
     * Called when user lifted touch during the test.
     *
     * @return instructions when touch is lifted.
     */
    public String doIfTouchIsUp() {
        pauseMp();
        return "Don't lift your finger. Go back to start";
    }


    /**
     * Returns the equivalent x and y coordinates of the bitmap given
     * x and y coordinates of the touch event.
     *
     * @param bitmap image of the path
     * @param eventX x coordinate of the touch of the user
     * @param eventY y coordinate of the touch of the user
     * @return x and y coordinates of the touch inside the picture
     */
    public int[] getBitMapCoordinates(Bitmap bitmap, float eventX, float eventY) {
        Matrix invertMatrix = new Matrix();
        float[] eventXY = new float[]{eventX, eventY};
        int x;
        int y;

        imageViewPathToTrace.getImageMatrix().invert(invertMatrix);
        invertMatrix.mapPoints(eventXY);
        x = (int) eventXY[0];
        y = (int) eventXY[1];

        if (x < 0) {
            x = 0;
        } else if (x > bitmap.getWidth() - 1) {
            x = bitmap.getWidth() - 1;
        }

        if (y < 0) {
            y = 0;
        } else if (y > bitmap.getHeight() - 1) {
            y = bitmap.getHeight() - 1;
        }
        return new int[]{x, y};
    }

    /**
     * Gets the instructions.
     *
     * @param index index in {@link #instructions}
     * @return instructions in specific index
     */
    public int setInstructions(int index) {
        return (instructions[index]);
    }

    /**
     * Get random path.
     *
     * @return path resource id
     */
    private int getRandomPathDrawable() {
        int[] path = new int[]{R.drawable.path_to_trace_1, R.drawable.path_to_trace_2};
        Random random = new Random((int) System.nanoTime());
        return path[random.nextInt(path.length)];
    }

    /**
     * Get the instructions depending on the chosen path
     *
     * @param path path chosen
     * @return instructions for the specific path
     */
    private int[] getInstructions(int path) {
        int[] instructionList = null;
        switch (path) {
            case R.drawable.path_to_trace_1:
                instructionList = new int[]{R.string.finemotor_path1_1,
                        R.string.finemotor_path1_2,
                        R.string.finemotor_question
                };
                break;
            case R.drawable.path_to_trace_2:
                instructionList = new int[]{R.string.finemotor_path2_1,
                        R.string.finemotor_path2_2,
                        R.string.finemotor_question
                };
                break;
        }
        return instructionList;

    }

    /**
     * Pauses the {@link #mp}
     */
    private void pauseMp() {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

}
