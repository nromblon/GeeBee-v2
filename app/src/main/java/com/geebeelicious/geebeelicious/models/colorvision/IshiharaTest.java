package com.geebeelicious.geebeelicious.models.colorvision;

import java.util.HashMap;
import java.util.Random;

/**
 * The IshiharaTest class is generated and
 * will contain the generated test items
 * composed of plates and options. The class
 * also keeps track of the score of the user.
 *
 * @author Katrina Lacsamana
 * @since 03/04/2016
 */
public class IshiharaTest {

    /**
     * Map of questions that contains the ishihara plate and options.
     */
    private HashMap<Integer, Question> generatedTest;

    /**
     * Used for generating random numbers.
     */
    private Random randomGenerator;

    /**
     * Array of all possible Ishihara plates that may be shown to the user.
     */
    private IshiharaPlate[] ishiharaPlates;

    /**
     * List of possible options for the user to choose from.
     */
    private Option[] options;

    /**
     * Current score of the user.
     */
    private int score;

    /**
     * Constructor
     *
     * @param ishiharaPlates {@link #ishiharaPlates}
     * @param options        {@link #options}
     */
    public IshiharaTest(IshiharaPlate[] ishiharaPlates, Option[] options) {
        randomGenerator = new Random((int) System.nanoTime());
        this.ishiharaPlates = ishiharaPlates;
        this.options = options;
        this.score = 0;
    }

    /**
     * Gets generated IshiharaTest by choosing random plates and generating choices for each chosen plate
     *
     * @return {@link #generatedTest}
     */
    public HashMap<Integer, Question> generateTest() {
        generatedTest = new HashMap<Integer, Question>();
        IshiharaPlate plate;
        Option[] generatedOptions;

        for (int i = 0; i < 11; i++) {
            plate = makeQuestion(i);
            generatedOptions = makeAnswers(plate);
            generatedTest.put(i, new Question(plate, generatedOptions));
        }
        return generatedTest;
    }

    /**
     * Gets IshiharaPlate based on question index on which the necessary plate style is based
     *
     * @param i question index
     * @return IshiharaPlate with the necessary plate style.
     */
    private IshiharaPlate makeQuestion(int i) {
        IshiharaPlate plate;

        if (i == 0) {
            plate = getPlateWithStyle(6);
        } else if (i % 2 == 0) {
            plate = getPlateWithStyle(3);
        } else {
            plate = getPlateWithStyle(1);
        }
        return plate;
    }

    /**
     * Gets IshiharaPlate based on selected style that has not yet been added in the generated test
     *
     * @param i style
     * @return IshiharaPlate with the necessary plate style.
     */
    private IshiharaPlate getPlateWithStyle(int i) {
        boolean isFound = false;
        int index;
        while (!isFound) {
            index = randomGenerator.nextInt(36);
            if ((!ishiharaPlates[index].isAdded()) && (ishiharaPlates[index].getStyle() == i)) {
                isFound = true;
                ishiharaPlates[index].setAdded();
                return ishiharaPlates[index];
            }
        }
        return null;
    }

    /**
     * Gets an Option based on given shape
     *
     * @param shape Shape of the option needed
     * @return Option with the given shape.
     */
    private Option getOptionWithShape(String shape) {
        for (Option o : options) {
            if (o.getShape().equals(shape)) {
                o.setAdded();
                return o;
            }
        }
        return null;
    }

    /**
     * Gets a random Option.
     *
     * @return random Option.
     */
    private Option getRandomOption() {
        boolean isFound = false;
        int index;

        while (!isFound) {
            index = randomGenerator.nextInt(11);
            if (!options[index].isAdded()) {
                isFound = true;
                options[index].setAdded();
                return options[index];
            }
        }
        return null;
    }

    /**
     * Gets Option[5] for a given plate, 1 correct option and 4 random options
     *
     * @param plate Plate to generate options for.
     * @return List of possible answers
     */
    private Option[] makeAnswers(IshiharaPlate plate) {
        Option[] generatedOptions = new Option[5];
        int correctAnswer = randomGenerator.nextInt(5);
        generatedOptions[correctAnswer] = getOptionWithShape(plate.getShape());

        for (int i = 0; i < 5; i++) {
            if (i != correctAnswer) {
                generatedOptions[i] = getRandomOption();
            }
        }
        for (Option o : generatedOptions) {
            o.resetAdded();
        }
        return generatedOptions;
    }

    /**
     * Gets IshiharaPlate from generated test based on index
     *
     * @param i index of the {@link Question} in {@link #generatedTest}
     * @return IshiharaPlate of the {@link Question} with index {@code i} in {@link #generatedTest}
     */
    public IshiharaPlate getPlate(int i) {
        return generatedTest.get(i).getIshiharaPlate();
    }

    /**
     * Gets Option[] from generated test based on index
     *
     * @param i index of the {@link Question} in {@link #generatedTest}
     * @return Options of the {@link Question} with index {@code i} in {@link #generatedTest}
     */
    public Option[] getOptions(int i) {
        return generatedTest.get(i).getOptions();
    }

    /**
     * Gets the Option from given questionNumber and optionNumber
     *
     * @param questionNumber index of the {@link Question} in {@link #generatedTest}
     * @param optionNumber   index of option to be retrieved.
     * @return Option from given questionNumber and optionNumber
     */
    private Option getOption(int questionNumber, int optionNumber) {
        return getOptions(questionNumber)[optionNumber];
    }

    //

    /**
     * Gets correct Option based on index of generated test
     *
     * @param i index of the {@link Question} in {@link #generatedTest}
     * @return Option with the same shape as the current question's shape.
     */
    private Option getCorrectAnswer(int i) {
        for (Option o : getOptions(i)) {
            if (o.getShape().equals(getPlate(i).getShape())) {
                return o;
            }
        }
        return null;
    }

    /**
     * Return boolean based on comparison of user-selected Option and correct Option
     *
     * @param a user's chosen option
     * @param b correct option
     * @return true if the user's choosen option matches the correct option, else return false.
     */
    private boolean isCorrect(Option a, Option b) {
        return a.getShape().equals(b.getShape());
    }

    /**
     * Increment score by 1
     */
    private void addScore() {
        score++;
    }

    /**
     * Gets {@link #score}
     *
     * @return {@link #score}
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks answer of given question given the questionNumber and the user-selected answer
     *
     * @param questionNumber index of the {@link Question} in {@link #generatedTest}
     * @param answer         index of user's chosen option.
     */
    public void checkAnswer(int questionNumber, int answer) {
        Option userAnswer = getOption(questionNumber, answer);
        Option correctAnswer = getCorrectAnswer(questionNumber);

        if (isCorrect(userAnswer, correctAnswer)) {
            addScore();
        }
    }


}
