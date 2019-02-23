package com.geebeelicious.geebeelicious.models.consultation;

/**
 * GetBetter.
 *
 * @author Mike Dayupay
 * @since 11/19/15.
 */
public class PositiveResults {

    private String positiveName;
    private String positiveAnswerPhrase;

    public PositiveResults(String positiveName, String positiveAnswerPhrase) {
        this.positiveName = positiveName;
        this.positiveAnswerPhrase = positiveAnswerPhrase;
    }

    public String getPositiveName() {
        return positiveName;
    }

    public String getPositiveAnswerPhrase() {
        return positiveAnswerPhrase;
    }
}
