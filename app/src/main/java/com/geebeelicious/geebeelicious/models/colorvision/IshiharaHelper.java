package com.geebeelicious.geebeelicious.models.colorvision;

import android.widget.ImageButton;
import android.widget.ImageView;

import com.geebeelicious.geebeelicious.R;

/**
 * The IshiharaHelper class functions to help
 * generate the test and conduct the test.
 * It contains all possible plates and options
 * that may be used in test generation.
 *
 * @author Katrina Lacsamana.
 * @since 03/04/2016
 */
public class IshiharaHelper {

    /**
     * Array of all possible Ishihara plates that may be shown to the user.
     */
    private IshiharaPlate[] ishiharaPlates;

    /**
     * Keeps track of the user's score and contains
     * the different Ishihara plates.
     */
    private IshiharaTest ishiharaTest;

    /**
     * List of possible options for the user to choose from.
     */
    private Option[] options;

    /**
     * View where the Ishihara plate is shown.
     */
    private ImageView plateView;

    /**
     * Serves as the index counter on how many Ishihara plates were shown.
     */
    private int currentPlate;

    /**
     * List of image buttons for the {@link #options}
     */
    private ImageButton[] buttonList;

    /**
     * Serves as the flag whether the test is on going or not.
     */
    private boolean isDone;

    /**
     * Constructor.
     * <p>
     * Initializes the {@link #ishiharaPlates}, {@link #options},
     * and the {@link #ishiharaTest}.
     *
     * @param plateView  {@link #plateView}
     * @param buttonList {@link #buttonList}
     */
    public IshiharaHelper(ImageView plateView, ImageButton[] buttonList) {
        this.plateView = plateView;
        this.buttonList = buttonList;

        ishiharaPlates = new IshiharaPlate[36];
        ishiharaPlates[0] = new IshiharaPlate("circle", 1, R.drawable.circle_tv_1);
        ishiharaPlates[1] = new IshiharaPlate("circle", 3, R.drawable.circle_tv_3);
        ishiharaPlates[2] = new IshiharaPlate("circle", 6, R.drawable.circle_tv_6);
        ishiharaPlates[3] = new IshiharaPlate("cloud", 1, R.drawable.cloud_tv_1);
        ishiharaPlates[4] = new IshiharaPlate("cloud", 3, R.drawable.cloud_tv_3);
        ishiharaPlates[5] = new IshiharaPlate("cloud", 6, R.drawable.cloud_tv_6);
        ishiharaPlates[6] = new IshiharaPlate("crescent", 1, R.drawable.crescent_tv_1);
        ishiharaPlates[7] = new IshiharaPlate("crescent", 3, R.drawable.crescent_tv_3);
        ishiharaPlates[8] = new IshiharaPlate("crescent", 6, R.drawable.crescent_tv_6);
        ishiharaPlates[9] = new IshiharaPlate("cross", 1, R.drawable.cross_tv_1);
        ishiharaPlates[10] = new IshiharaPlate("cross", 3, R.drawable.cross_tv_3);
        ishiharaPlates[11] = new IshiharaPlate("cross", 6, R.drawable.cross_tv_6);
        ishiharaPlates[12] = new IshiharaPlate("diamond", 1, R.drawable.diamond_tv_1);
        ishiharaPlates[13] = new IshiharaPlate("diamond", 3, R.drawable.diamond_tv_3);
        ishiharaPlates[14] = new IshiharaPlate("diamond", 6, R.drawable.diamond_tv_6);
        ishiharaPlates[15] = new IshiharaPlate("ellipse", 1, R.drawable.ellipse_tv_1);
        ishiharaPlates[16] = new IshiharaPlate("ellipse", 3, R.drawable.ellipse_tv_3);
        ishiharaPlates[17] = new IshiharaPlate("ellipse", 6, R.drawable.ellipse_tv_6);
        ishiharaPlates[18] = new IshiharaPlate("heart", 1, R.drawable.heart_tv_1);
        ishiharaPlates[19] = new IshiharaPlate("heart", 3, R.drawable.heart_tv_3);
        ishiharaPlates[20] = new IshiharaPlate("heart", 6, R.drawable.heart_tv_6);
        ishiharaPlates[21] = new IshiharaPlate("rectangle", 1, R.drawable.rectangle_tv_1);
        ishiharaPlates[22] = new IshiharaPlate("rectangle", 3, R.drawable.rectangle_tv_3);
        ishiharaPlates[23] = new IshiharaPlate("rectangle", 6, R.drawable.rectangle_tv_6);
        ishiharaPlates[24] = new IshiharaPlate("semicircle", 1, R.drawable.semicircle_tv_1);
        ishiharaPlates[25] = new IshiharaPlate("semicircle", 3, R.drawable.semicircle_tv_3);
        ishiharaPlates[26] = new IshiharaPlate("semicircle", 6, R.drawable.semicircle_tv_6);
        ishiharaPlates[27] = new IshiharaPlate("square", 1, R.drawable.square_tv_1);
        ishiharaPlates[28] = new IshiharaPlate("square", 3, R.drawable.square_tv_3);
        ishiharaPlates[29] = new IshiharaPlate("square", 6, R.drawable.square_tv_6);
        ishiharaPlates[30] = new IshiharaPlate("star", 1, R.drawable.star_tv_1);
        ishiharaPlates[31] = new IshiharaPlate("star", 3, R.drawable.star_tv_3);
        ishiharaPlates[32] = new IshiharaPlate("star", 6, R.drawable.star_tv_6);
        ishiharaPlates[33] = new IshiharaPlate("triangle", 1, R.drawable.triangle_tv_1);
        ishiharaPlates[34] = new IshiharaPlate("triangle", 3, R.drawable.triangle_tv_3);
        ishiharaPlates[35] = new IshiharaPlate("triangle", 6, R.drawable.triangle_tv_6);

        options = new Option[12];
        options[0] = new Option("circle", R.drawable.btn_cvt_circle);
        options[1] = new Option("cloud", R.drawable.btn_cvt_cloud);
        options[2] = new Option("crescent", R.drawable.btn_cvt_crescent);
        options[3] = new Option("cross", R.drawable.btn_cvt_cross);
        options[4] = new Option("diamond", R.drawable.btn_cvt_diamond);
        options[5] = new Option("ellipse", R.drawable.btn_cvt_ellipse);
        options[6] = new Option("heart", R.drawable.btn_cvt_heart);
        options[7] = new Option("rectangle", R.drawable.btn_cvt_rectangle);
        options[8] = new Option("semicircle", R.drawable.btn_cvt_semicircle);
        options[9] = new Option("square", R.drawable.btn_cvt_square);
        options[10] = new Option("star", R.drawable.btn_cvt_star);
        options[11] = new Option("triangle", R.drawable.btn_cvt_triangle);

        ishiharaTest = new IshiharaTest(ishiharaPlates, options);
    }

    /**
     * Displays current plate on screen.
     */
    private void displayPlate() {
        plateView.setImageResource(getCurrentPlate().getIshiharaPlateDrawable());
    }

    /**
     * Displays possible answers on buttons on screen.
     */
    private void displayOptions() {
        for (int i = 0; i < 5; i++) {
            buttonList[i].setImageResource(getCurrentOptions()[i].getOptionDrawable());
        }
    }

    /**
     * Gets current IshiharaPlate
     *
     * @return current IshiharaPlate
     */
    private IshiharaPlate getCurrentPlate() {
        return ishiharaTest.getPlate(currentPlate);
    }

    /**
     * Gets possible answers for current IshiharaPlate
     *
     * @return possible answers
     */
    private Option[] getCurrentOptions() {
        return ishiharaTest.getOptions(currentPlate);
    }

    /**
     * Resets values and screen for start of test
     */
    public void startTest() {
        currentPlate = 0;
        ishiharaTest.generateTest();
        displayPlate();
        displayOptions();
        isDone = false;
    }

    /**
     * Determines course of action for next question as test progresses
     */
    public void goToNextQuestion() {
        if (currentPlate < 10) {
            currentPlate++;
            displayPlate();
            displayOptions();
        } else if (currentPlate == 10) {
            ishiharaTest.getScore();
            isDone = true;
        }
    }

    /**
     * Sets the answer of the user for the current plate
     *
     * @param i index of the answer of the user.
     */
    public void answerQuestion(int i) {
        ishiharaTest.checkAnswer(currentPlate, i);
    }

    /**
     * Get {@link #isDone()}
     *
     * @return {@link #isDone()}
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Gets the final test score of user
     *
     * @return score of user.
     */
    public int getScore() {
        return ishiharaTest.getScore();
    }

    /**
     * Gets String interpretation of test score
     *
     * @return string interpretation
     */
    public String getResult() {
        if (isNormal()) {
            return "Normal";
        } else {
            return "Abnormal";
        }
    }

    /**
     * Returns whether the color vision test result
     * of the person is normal or not.
     *
     * @return if higher or equal to 10, return true. Else, false.
     */
    public boolean isNormal() {
        return getScore() >= 10;
    }

}
