package com.geebeelicious.geebeelicious.models.consultation;

/**
 * GetBetter.
 *
 * @author Mike Dayupay
 * @since 11/12/15.
 */
public class PatientAnswers {

    private int caseRecordId;
    private int symptomId;
    private String answer;

    public PatientAnswers(int caseRecordId, int symptomId, String answer) {
        this.caseRecordId = caseRecordId;
        this.symptomId = symptomId;
        this.answer = answer;
    }

    public int getCaseRecordId() {
        return caseRecordId;
    }

    public int getSymptomId() {
        return symptomId;
    }

    public String getAnswer() {
        return answer;
    }

    public int hashCode() {

        return symptomId;
    }

    @Override
    public boolean equals(Object object) {

        boolean similar = false;

        if (object != null && object instanceof PatientAnswers) {
            similar = this.symptomId == ((PatientAnswers) object).symptomId;
        }

        return similar;
    }
}
