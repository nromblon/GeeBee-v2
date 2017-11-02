package com.geebeelicious.geebeelicious.models.visualacuity;

import android.widget.ImageView;

import com.geebeelicious.geebeelicious.R;

/**
 * The ChartHelper class manages the
 * administration of the visual
 * acuity test using the visual acuity chart
 *
 * @author Katrina Lacsamana
 * @since02/24/2016.
 */
public class ChartHelper {

    /**
     * List of chart lines of the test.
     */
    private ChartLine[] chart;

    /**
     * Current line number tested.
     */
    private int currentLineNumber;

    /**
     * where the chart is shown.
     */
    private ImageView chartView;

    /**
     * Line where the user last read correctly
     */
    private ChartLine result;

    /**
     * Whether the test is done or not.
     */
    private boolean isDone;

    /**
     * Whether the right eye is tested or not.
     */
    private boolean isRightTested;

    /**
     * Whether the left eye is tested or not.
     */
    private boolean isLeftTested;

    /**
     * Constructor.
     *
     * @param chartView       {@link #chartView}
     * @param chartPreference the preferred eye chart to
     *                        be used for the test.
     */
    public ChartHelper(ImageView chartView, int chartPreference) {
        chart = new ChartLine[11];
        initializeChartPreference(chartPreference);
        this.chartView = chartView;
        this.isRightTested = false;
        this.isLeftTested = false;
    }

    /**
     * Initialize the {@link #chart} depending on the {@code chartPreference}.
     *
     * @param chartPreference preferred chart of the user.
     */
    private void initializeChartPreference(int chartPreference) {
        if (chartPreference == 1) { //Tumbling E Chart
            chart[0] = new ChartLine(1, 20, 200, R.drawable.tumbling_e_line_1);
            chart[1] = new ChartLine(2, 20, 100, R.drawable.tumbling_e_line_2);
            chart[2] = new ChartLine(3, 20, 70, R.drawable.tumbling_e_line_3);
            chart[3] = new ChartLine(4, 20, 50, R.drawable.tumbling_e_line_4);
            chart[4] = new ChartLine(5, 20, 40, R.drawable.tumbling_e_line_5);
            chart[5] = new ChartLine(6, 20, 30, R.drawable.tumbling_e_line_6);
            chart[6] = new ChartLine(7, 20, 25, R.drawable.tumbling_e_line_7);
            chart[7] = new ChartLine(8, 20, 20, R.drawable.tumbling_e_line_8);
            chart[8] = new ChartLine(9, 20, 15, R.drawable.tumbling_e_line_9);
            chart[9] = new ChartLine(10, 20, 10, R.drawable.tumbling_e_line_10);
            chart[10] = new ChartLine(11, 20, 5, R.drawable.tumbling_e_line_11);
        } else { //Snellen Chart
            chart[0] = new ChartLine(1, 20, 200, R.drawable.snellen_line_1);
            chart[1] = new ChartLine(2, 20, 100, R.drawable.snellen_line_2);
            chart[2] = new ChartLine(3, 20, 70, R.drawable.snellen_line_3);
            chart[3] = new ChartLine(4, 20, 50, R.drawable.snellen_line_4);
            chart[4] = new ChartLine(5, 20, 40, R.drawable.snellen_line_5);
            chart[5] = new ChartLine(6, 20, 30, R.drawable.snellen_line_6);
            chart[6] = new ChartLine(7, 20, 25, R.drawable.snellen_line_7);
            chart[7] = new ChartLine(8, 20, 20, R.drawable.snellen_line_8);
            chart[8] = new ChartLine(9, 20, 15, R.drawable.snellen_line_9);
            chart[9] = new ChartLine(10, 20, 10, R.drawable.snellen_line_10);
            chart[10] = new ChartLine(11, 20, 5, R.drawable.snellen_line_11);
        }
    }

    /**
     * Gets the chart before the current chart line.
     *
     * @return previous line.
     */
    private ChartLine getPreviousLine() {
        return chart[currentLineNumber - 1];
    }

    /**
     * Gets the current chart line.
     *
     * @return current chart line.
     */
    private ChartLine getCurrentLine() {
        return chart[currentLineNumber];
    }

    /**
     * Change line shown to the user.
     */
    public void goToNextLine() {
        if (currentLineNumber < 10) {
            currentLineNumber++;
            displayChartLine();
        } else if (currentLineNumber == 10 && result == null) {
            setResult();
        }
    }

    /**
     * Displays the current chart line.
     */
    private void displayChartLine() {
        chartView.setImageResource(getCurrentLine().getChartLineDrawable());
    }

    /**
     * Starts the test.
     */
    public void startTest() {
        result = null;
        isDone = false;
        currentLineNumber = 0;
        displayChartLine();
    }

    /**
     * Gets {@link #isDone}.
     *
     * @return {@link #isDone}
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Sets {@link #result} depending on the current chart line.
     */
    public void setResult() {
        if (currentLineNumber == 0 || currentLineNumber == 10) {
            result = getCurrentLine();
        } else {
            result = getPreviousLine();
        }
        isDone = true;
    }

    /**
     * Gets {@link #result}
     *
     * @return
     */
    public ChartLine getResult() {
        return result;
    }

    /**
     * Gets {@link #isRightTested}.
     *
     * @return {@link #isRightTested}
     */
    public boolean isRightTested() {
        return isRightTested;
    }

    /**
     * Gets {@link #isLeftTested}.
     *
     * @return {@link #isLeftTested}
     */
    public boolean isLeftTested() {
        return isLeftTested;
    }

    /**
     * Sets {@link #isRightTested} to true.
     */
    public void setIsRightTested() {
        isRightTested = true;
    }

    /**
     * Sets {@link #isLeftTested} to true.
     */
    public void setIsLeftTested() {
        isLeftTested = true;
    }

    /**
     * Check whether two eyes were tested or not.
     *
     * @return true if both were tested. Else, false.
     */
    public boolean isBothTested() {
        return isRightTested && isLeftTested;
    }


}
