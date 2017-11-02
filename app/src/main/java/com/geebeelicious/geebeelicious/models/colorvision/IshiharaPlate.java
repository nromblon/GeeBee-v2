package com.geebeelicious.geebeelicious.models.colorvision;

/**
 * The IshiharaPlate represents a plate
 * to be used in the test. The class
 * represents the plate through information
 * about its shape, its style, and its
 * corresponding drawable.
 *
 * @author Katrina Lacsamana
 * @since 03/04/2016
 */
public class IshiharaPlate {

    /**
     * Shape inside the plate.
     */
    private String shape;

    /**
     * Type of Ishihara plate. Types are depending
     * on the color combination of the plate.
     */
    private int style;

    /**
     * Drawable of the ishihira plate.
     */
    private int ishiharaPlateDrawable;

    /**
     * Flag to show whether it has already been added
     * in the generated test. True if added, false if not.
     */
    private boolean isAdded;

    /**
     * Constructor.
     *
     * @param shape                 {@link #shape}
     * @param style                 {@link #style}
     * @param ishiharaPlateDrawable {@link #ishiharaPlateDrawable}
     */
    public IshiharaPlate(String shape, int style, int ishiharaPlateDrawable) {
        this.shape = shape;
        this.style = style;
        this.ishiharaPlateDrawable = ishiharaPlateDrawable;
        this.isAdded = false;
    }

    /**
     * Gets {@link #shape}
     *
     * @return {@link #shape}
     */
    public String getShape() {
        return shape;
    }

    /**
     * Gets {@link #style}
     *
     * @return {@link #style}
     */
    public int getStyle() {
        return style;
    }

    /**
     * Gets {@link #ishiharaPlateDrawable}
     *
     * @return {@link #ishiharaPlateDrawable}
     */
    public int getIshiharaPlateDrawable() {
        return ishiharaPlateDrawable;
    }

    /**
     * Gets {@link #isAdded}
     *
     * @return {@link #isAdded}
     */
    public boolean isAdded() {
        return isAdded;
    }

    /**
     * Sets {@link #isAdded} to true
     */
    public void setAdded() {
        isAdded = true;
    }
}
