package com.geebeelicious.geebeelicious.models.colorvision;

/**
 * Created by Kate on 03/05/2016.
 * The Question class represents a question
 * asked during the color vision test. The
 * class contains an Ishihara plate and the
 * multiple choice options that the user
 * will choose an answer from.
 */
public class Question {

    /**
     * IshiharaPlate used for the question
     */
    private IshiharaPlate ishiharaPlate;

    /**
     * Possible answers for {@link #ishiharaPlate}. 1 correct option, 4 wrong option.
     */
    private Option[] options;

    /**
     * Constructor.
     *
     * @param ishiharaPlate {@link #ishiharaPlate}
     * @param options       {@link #options}
     */
    public Question(IshiharaPlate ishiharaPlate, Option[] options) {
        this.ishiharaPlate = ishiharaPlate;
        this.options = options;
    }

    /**
     * Gets {@link #ishiharaPlate}
     *
     * @return {@link #options}
     */
    public IshiharaPlate getIshiharaPlate() {
        return ishiharaPlate;
    }

    /**
     * Gets {@link #options}
     *
     * @return {@link #options}
     */
    public Option[] getOptions() {
        return options;
    }


}
