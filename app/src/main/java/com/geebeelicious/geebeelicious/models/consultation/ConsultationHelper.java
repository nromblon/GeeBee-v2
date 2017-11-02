package com.geebeelicious.geebeelicious.models.consultation;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * The ConsultationHelper class serves to generate chief complaint
 * questions, and retrieves succeeding questions from the ExpertSystem.
 *
 * @author Mary Grace Malana
 * @since 3/22/2016
 */
public class ConsultationHelper {

    /**
     * Used to identify the source of a log message
     */
    private static String TAG = "ConsultationHelper";

    /**
     * Whether the consultation is done or not.
     */
    private boolean isConsultationDone;

    /**
     * Whether the current question is asking about a chief complaint
     */
    private boolean isAskingChiefComplaint;

    /**
     * Index counter for the current chief complaint asked.
     */
    private int currentChiefComplaint;

    /**
     * List of all chief complaints.
     */
    private ArrayList<ChiefComplaint> chiefComplaints;

    /**
     * List of all the chief complaints that the patient has answered "Yes" to.
     */
    private ArrayList<ChiefComplaint> patientChiefComplaints;

    /**
     * Used for getting non-chief complaint questions.
     */
    private ExpertSystem expertSystem;

    /**
     * Patient information of the one currently taking the test
     */
    private Patient patient;

    /**
     * Data that the current consultation is taken.
     */
    private String dateConsultation;

    /**
     * Constructor.
     * Initializes the values of the object's attributes.
     *
     * @param context          context for the object.
     * @param patient          {@link #patient}
     * @param dateConsultation {@link #dateConsultation}
     */
    public ConsultationHelper(Context context, Patient patient, String dateConsultation) {
        isConsultationDone = false;
        isAskingChiefComplaint = true;
        currentChiefComplaint = 0;
        chiefComplaints = new ArrayList<>();
        patientChiefComplaints = new ArrayList<>();
        this.patient = patient;
        this.dateConsultation = dateConsultation;
        expertSystem = new ExpertSystem(context, patient);

        chiefComplaints = expertSystem.getChiefComplaintsQuestions();
    }

    /**
     * Gets {@link #isConsultationDone}.
     *
     * @return {@link #isConsultationDone}
     */
    public boolean isConsultationDone() {
        return isConsultationDone;
    }

    /**
     * Gets the next question to be asked from {@link #chiefComplaints} or {@link #expertSystem}.
     *
     * @param isYes answer of the patient to the current question.
     *              If {@code isYes} is true, the patient answered yes.
     *              Else, the patient answered no.
     * @return next question to be asked to the user. If there's no more next question, returns null.
     */
    public Question getNextQuestion(boolean isYes) { //returns null if there's no more questions
        if (isAskingChiefComplaint) { //if asking about chief complaint
            if (isYes) { //if isYes, adds to patient's chief complaints
                ChiefComplaint complaint = chiefComplaints.get(currentChiefComplaint);
                Log.d(TAG, "Added complaint with question '" + complaint.getQuestion() + "' to patient's chief complaint");
                patientChiefComplaints.add(complaint);
            }
            currentChiefComplaint++;

            if (currentChiefComplaint == chiefComplaints.size()) { //checks if no more questions
                isAskingChiefComplaint = false; //to skip this parent conditional statement
                if (patientChiefComplaints.size() == 0)
                    return null;
                return expertSystem.startExpertSystem(patientChiefComplaints);
            } else {
                return new Question(3, chiefComplaints.get(currentChiefComplaint).getQuestion());
            }
        } else {
            Question question = expertSystem.getNextQuestion(isYes);
            if (question == null) { //if there's no more question, consultation is done. also saves the answers
                isConsultationDone = true;
            }
            return question;
        }
    }

    /**
     * Gets the first question from {@link #chiefComplaints}
     *
     * @return first question to be asked to the patient.
     */
    public Question getFirstQuestion() {
        return new Question(3, chiefComplaints.get(0).getQuestion());
    }

    /**
     * At the end of consultation, get the generated HPI according to the answer of
     * the user to the consultation questions.
     *
     * @return generated HPI.
     */
    public String getHPI() {
        return expertSystem.getHPI();
    }

    /**
     * Save the {@code hpi} of the patient with the current date and
     * patient ID to the database.
     *
     * @param hpi generated HPI to be saved to the database.
     */
    public void saveToDatabase(String hpi) {
        expertSystem.saveToDatabase(new HPI(patient.getPatientID(), hpi, dateConsultation));
    }
}
