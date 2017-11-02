package com.geebeelicious.geebeelicious.models.visualacuity;


/**
 * The ChartLine class represents
 * a line on the Snellen Eye Chart
 *
 * @author Katrina Lacsamana.
 */
public class ChartLine {

    /**
     * Line number of the chart line.
     */
    private int lineNumber;

    /**
     * Actual distance of the chart line.
     */
    private int actualDistance;

    /**
     * Expected distance of the chart line.
     */
    private int expectedDistance;

    /**
     * Resource ID of the chart line.
     */
    private int chartLineDrawable;

    /**
     * Constructor.
     *
     * @param lineNumber        {@link #lineNumber}
     * @param actualDistance    {@link #actualDistance}
     * @param expectedDistance  {@link #expectedDistance}
     * @param chartLineDrawable {@link #chartLineDrawable }
     */
    public ChartLine(int lineNumber, int actualDistance, int expectedDistance, int chartLineDrawable) {
        this.lineNumber = lineNumber;
        this.actualDistance = actualDistance;
        this.expectedDistance = expectedDistance;
        this.chartLineDrawable = chartLineDrawable;
    }

    /**
     * Gets {@link #lineNumber}.
     *
     * @return {@link #lineNumber}
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Gets {@link #actualDistance}.
     *
     * @return {@link #actualDistance}
     */
    public int getActualDistance() {
        return actualDistance;
    }

    /**
     * Gets {@link #expectedDistance}.
     *
     * @return {@link #expectedDistance}
     */
    public int getExpectedDistance() {
        return expectedDistance;
    }

    /**
     * Gets {@link #chartLineDrawable}.
     *
     * @return {@link #chartLineDrawable}
     */
    public int getChartLineDrawable() {
        return chartLineDrawable;
    }

    /**
     * Gets the corresponding result for that line in test.
     *
     * @return result for that line in test.
     */
    public String getVisualAcuity() {
        return new String(actualDistance + "/" + expectedDistance);
    }


}
