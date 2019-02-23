package com.geebeelicious.geebeelicious.models.consultation;

/**
 * GetBetter.
 *
 * @author Mike Dayupay
 * @since 10/17/15
 */
public class SymptomFamily {

    private int symptomFamilyId;
    private String symptomFamilyNameEnglish;
    private String symptomFamilyNameTagalog;
    private String generalQuestionEnglish;
    private String responsesEnglish;
    private boolean isAnswered;
    private boolean answerStatus;
    private int chiefComplaintId;


    public SymptomFamily(int symptomFamilyId, String symptomFamilyNameEnglish,
                         String symptomFamilyNameTagalog, String generalQuestionEnglish,
                         String responsesEnglish, int chiefComplaintId) {

        this.symptomFamilyId = symptomFamilyId;
        this.symptomFamilyNameEnglish = symptomFamilyNameEnglish;
        this.symptomFamilyNameTagalog = symptomFamilyNameTagalog;
        this.generalQuestionEnglish = generalQuestionEnglish;
        this.responsesEnglish = responsesEnglish;
        this.chiefComplaintId = chiefComplaintId;
    }

    public int getSymptomFamilyId() {
        return symptomFamilyId;
    }

    public void setSymptomFamilyId(int symptomFamilyId) {
        this.symptomFamilyId = symptomFamilyId;
    }

    public String getSymptomFamilyNameEnglish() {
        return symptomFamilyNameEnglish;
    }

    public void setSymptomFamilyNameEnglish(String symptomFamilyNameEnglish) {
        this.symptomFamilyNameEnglish = symptomFamilyNameEnglish;
    }

    public String getSymptomFamilyNameTagalog() {
        return symptomFamilyNameTagalog;
    }

    public void setSymptomFamilyNameTagalog(String symptomFamilyNameTagalog) {
        this.symptomFamilyNameTagalog = symptomFamilyNameTagalog;
    }

    public String getGeneralQuestionEnglish() {
        return generalQuestionEnglish;
    }

    public void setGeneralQuestionEnglish(String generalQuestionEnglish) {
        this.generalQuestionEnglish = generalQuestionEnglish;
    }

    public String getResponsesEnglish() {
        return responsesEnglish;
    }

    public void setResponsesEnglish(String responsesEnglish) {
        this.responsesEnglish = responsesEnglish;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public boolean isAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(boolean answerStatus) {
        this.answerStatus = answerStatus;
    }

    public int getChiefComplaintId() {
        return chiefComplaintId;
    }
}
