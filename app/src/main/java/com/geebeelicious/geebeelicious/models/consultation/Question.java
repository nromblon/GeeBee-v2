package com.geebeelicious.geebeelicious.models.consultation;

/**
 * Question class is used inside for getting the
 * question and the emotion of the question.
 *
 * @author Mary Grace Malana
 */
public class Question {
    /**
     * Emotional intent of the question.
     * 1 = happy1, 2 = happy2, 3 = happy3,
     * 4 = concern1, 5 = concern2, 6 = concern3
     */
    int emotion;

    /**
     * Question to be asked.
     */
    String questionstring;

    /**
     * Constructor.
     *
     * @param emotion        {@link #emotion}
     * @param questionstring {@link #questionstring}
     */
    public Question(int emotion, String questionstring) {
        this.emotion = emotion;
        this.questionstring = questionstring;
    }

    /**
     * Gets {@link #emotion}.
     *
     * @return {@link #emotion}
     */
    public int getEmotion() {
        return emotion;
    }

    /**
     * Gets {@link #questionstring}.
     *
     * @return {@link #questionstring}
     */
    public String getQuestionstring() {
        return questionstring;
    }
}
