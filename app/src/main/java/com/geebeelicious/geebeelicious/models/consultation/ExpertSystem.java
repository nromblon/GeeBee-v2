package com.geebeelicious.geebeelicious.models.consultation;

import android.content.Context;
import android.util.Log;

import com.geebeelicious.geebeelicious.database.DatabaseAdapter;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;

/**
 * The original code was created by Mike Dayupay 2015.
 * For the purpose of integration, the code was modified.
 * The ExpertSystem class gives specific questions depending on the input of the user
 * It uses the DatabaseAdapter class to connect with the database.
 *
 * @author Mike Dayupay
 * @author Mary Grace Malana
 * @since 2015
 */

public class ExpertSystem {

    /**
     * Used to identify the source of a log message
     */
    private final static String TAG = "ExpertSystem";

    /**
     * List of all Impressions to be probed depending on
     * {@link #patientChiefComplaints}
     */
    private ArrayList<Impressions> impressionsSymptoms;

    /**
     * List of symptom's names that the user has
     * answered 'Yes' to its corresponding question.
     */
    private ArrayList<String> positiveSymptomList;

    /**
     * List of symptom's names that the user has
     * answered 'No' to its corresponding question.
     */
    private ArrayList<String> ruledOutSymptomList;

    /**
     * List of symptoms to be asked about depending
     * on the current impression being probed.
     *
     * @see #impressionsSymptoms
     * @see #currentImpressionIndex
     */
    private ArrayList<Symptom> questions;

    /**
     * List of patient answers.
     */
    private ArrayList<PatientAnswers> answers;

    /**
     * List of all the chief complaints of the patient.
     */
    private ArrayList<ChiefComplaint> patientChiefComplaints;

    /**
     * Patient information of the one currently taking the test
     */
    private Patient patient;

    /**
     * Index counter for the current impression
     * from {@link #impressionsSymptoms} being probed.
     */
    private int currentImpressionIndex;

    /**
     * Index counter for the current Symptom from
     * {@link #questions}.
     */
    private int currentSymptomIndex;

    /**
     * Just used for placeholder, since the old code saves the users' answers
     * to the database.
     */
    private int caseRecordId;

    /**
     * Whether the SymptomFamily of the current symptom
     * from {@link #questions} was asked about. If true,
     * the SymptomFamily question was already asked. Else,
     * the SymptomFamily question hasn't been asked yet.
     *
     * @see #getQuestion()
     */
    private boolean flag;

    /**
     * Used in accessing the database.
     */
    private DatabaseAdapter getBetterDb;

    /**
     * Current SymptomFamily to be asked about
     */
    private SymptomFamily generalQuestion;

    /**
     * String format for the date
     */
    private DateFormat dateFormat;

    /**
     * Constructor.
     * Initializes the values of the object's attributes.
     *
     * @param context context for the object.
     * @param patient {@link #patient}
     */
    public ExpertSystem(Context context, Patient patient) {
        ruledOutSymptomList = new ArrayList<>();
        positiveSymptomList = new ArrayList<>();
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        this.patient = patient;
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");


        Log.d(TAG, patient.getFirstName());

        initializeDatabase(context);

        caseRecordId = 0; //just used for placeholder, since the old code saves the users' answers
    }

    /**
     * Starts the expert system. Prepares the
     * the questions to be asked.
     *
     * @param patientChiefComplaints {@link #patientChiefComplaints}
     * @return question to be asked.
     */
    public Question startExpertSystem(ArrayList<ChiefComplaint> patientChiefComplaints) {
        this.patientChiefComplaints = patientChiefComplaints;

        resetDatabaseFlags();
        initializeImpressionList();
        updateAnsweredStatusSymptomFamily();

        currentImpressionIndex = 0;
        currentSymptomIndex = 0;
        getQuestions(impressionsSymptoms.get(currentImpressionIndex).getImpressionId());

        return getQuestion();
    }

    /**
     * Saves the data from {@code hpi} to the database.
     *
     * @param hpi HPI to be saved.
     */
    public void saveToDatabase(HPI hpi) {
        getBetterDb.insertHPI(hpi);
        getBetterDb.closeDatabase(); //closes the database
    }

    /**
     * Initializes and returns chief complaints.
     *
     * @return list of all chief complaints.
     */
    public ArrayList<ChiefComplaint> getChiefComplaintsQuestions() {
        ArrayList<ChiefComplaint> chiefComplaints = new ArrayList<>();

        chiefComplaints.add(new ChiefComplaint(1, "Do you have a fever?"));
        chiefComplaints.add(new ChiefComplaint(2, "Are you feeling any pain?"));
        chiefComplaints.add(new ChiefComplaint(3, "Do you have any injuries?"));
        chiefComplaints.add(new ChiefComplaint(4, "Do you have a skin problem?"));
        chiefComplaints.add(new ChiefComplaint(5, "Are you having breathing problems?"));
        chiefComplaints.add(new ChiefComplaint(6, "Are you having bowel movement problems?"));
        chiefComplaints.add(new ChiefComplaint(7, "Do you feel unwell?"));

        return chiefComplaints;
    }

    /**
     * Initialize the database and opens it for
     * read and write manipulation.
     *
     * @param context
     */
    private void initializeDatabase(Context context) {

        getBetterDb = new DatabaseAdapter(context);

        try {
            getBetterDb.openDatabaseForRead();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset the value of the database flags
     * to default value.
     */
    private void resetDatabaseFlags() {
        getBetterDb.resetSymptomAnsweredFlag();
        getBetterDb.resetSymptomFamilyFlags();
    }

    /**
     * Initialize {@link #impressionsSymptoms}. Add the impression and its
     * symptoms.
     */
    private void initializeImpressionList() {

        impressionsSymptoms = new ArrayList<>();
        impressionsSymptoms.addAll(getBetterDb.getImpressions(patientChiefComplaints));

        impressionsSymptoms = new ArrayList<>(new LinkedHashSet<>(impressionsSymptoms)); // Remove duplicates

        for (int i = 0; i < impressionsSymptoms.size(); i++) {
            ArrayList<Symptom> symptoms = getBetterDb.getSymptoms(impressionsSymptoms.get(i).getImpressionId());
            impressionsSymptoms.get(i).setSymptoms(symptoms);
        }
    }

    /**
     * Update the answered status for the SymptomFamily for
     * each ChiefComplaint in {@link #patientChiefComplaints}.
     *
     * @see DatabaseAdapter#updateAnsweredStatusSymptomFamily(int)
     */
    private void updateAnsweredStatusSymptomFamily() {

        for (ChiefComplaint c : patientChiefComplaints) {
            getBetterDb.updateAnsweredStatusSymptomFamily(c.getComplaintID());
        }
    }

    /**
     * Update the answered status for the SymptomFamily with
     * the answer status as {@code answer}.
     *
     * @param answer
     */
    private void updateAnsweredStatusSymptomFamily(int answer) {
        Log.e(TAG,"GetbetterDB: "+getBetterDb);
        Log.e(TAG, "General question: "+generalQuestion);
        getBetterDb.updateAnsweredStatusSymptomFamily(generalQuestion.getSymptomFamilyId(), answer);
    }

    /**
     * Update the value of {@link #questions} to
     * symptoms that are related to the specified impression.
     *
     * @param impressionId
     * @see DatabaseAdapter#getQuestions(int)
     */
    private void getQuestions(int impressionId) {
        questions.clear();
        questions.addAll(getBetterDb.getQuestions(impressionId));
        //Log.d("questions size", questions.size() + "");
    }

    /**
     * Get either symptom or symptom family question depending
     * on the value of calling {@link #isSymptomFamilyQuestionAnswered(int)}
     *
     * @return SymptomFamily question if {@link #isSymptomFamilyQuestionAnswered(int)} returns false, else
     * return Symptom question.
     */
    private Question getQuestion() {
        Question questionAsked;

        if (isSymptomFamilyQuestionAnswered(questions.get(currentSymptomIndex).getSymptomFamilyId())) {
            Symptom symptom = questions.get(currentSymptomIndex);
            questionAsked = new Question(symptom.getEmotion(), symptom.getQuestionEnglish());
            flag = true;
        } else {
            getGeneralQuestion(questions.get(currentSymptomIndex).getSymptomFamilyId());
            questionAsked = new Question(2, generalQuestion.getGeneralQuestionEnglish());
            flag = false;
        }

        return questionAsked;
    }

    /**
     * Gets whether the question of symptom family with id {@code symptomFamilyId} was already answered.
     *
     * @param symptomFamilyId {@link SymptomFamily#symptomFamilyId}
     * @return true if the question was already answered, else return false.
     */
    private boolean isSymptomFamilyQuestionAnswered(int symptomFamilyId) {
        return getBetterDb.symptomFamilyIsAnswered(symptomFamilyId);
    }

    /**
     * @param symptomFamilyId
     */
    private void getGeneralQuestion(int symptomFamilyId) {
        generalQuestion = getBetterDb.getGeneralQuestion(symptomFamilyId);
    }

    /**
     * Called after the user answers the question. Gets the next
     * question to be asked.
     *
     * @param isYes answer of the user from the previous question.
     * @return next question to be asked. If no more question, return null.
     */
    public Question getNextQuestion(boolean isYes) { //returns null if no more question


        if (isYes) {
            if (flag) {
                positiveSymptomList.add(questions.get(currentSymptomIndex).getSymptomNameEnglish());
                positiveSymptomList = new ArrayList<>(new LinkedHashSet<>(positiveSymptomList));
                updateAnsweredFlagPositive(questions.get(currentSymptomIndex).getSymptomId());
                addToAnswers(new PatientAnswers(caseRecordId, questions.get(currentSymptomIndex).getSymptomId(), "Yes"));
            } else {
                updateAnsweredStatusSymptomFamily(1);
            }
        } else {
            if (flag) {
                ruledOutSymptomList.add(questions.get(currentSymptomIndex).getSymptomNameEnglish());
                ruledOutSymptomList = new ArrayList<>(new LinkedHashSet<>(ruledOutSymptomList));
                updateAnsweredFlagPositive(questions.get(currentSymptomIndex).getSymptomId());
                addToAnswers(new PatientAnswers(caseRecordId, questions.get(currentSymptomIndex).getSymptomId(), "No"));
            } else {
                updateAnsweredStatusSymptomFamily(0);
                updateAnsweredFlagPositive(questions.get(currentSymptomIndex).getSymptomId());
                ruledOutSymptomList.add(questions.get(currentSymptomIndex).getSymptomNameEnglish());
                ruledOutSymptomList = new ArrayList<>(new LinkedHashSet<>(ruledOutSymptomList));
                addToAnswers(new PatientAnswers(caseRecordId, questions.get(currentSymptomIndex).getSymptomId(), "No"));
            }
        }

        if (flag) {

            currentSymptomIndex++;

            if (currentSymptomIndex >= questions.size()) {
                currentSymptomIndex = 0;
                currentImpressionIndex++;


                if (currentImpressionIndex >= impressionsSymptoms.size()) {
                    currentImpressionIndex = impressionsSymptoms.size() - 1;

                    return null;
                } else {
                    getQuestions(impressionsSymptoms.get(currentImpressionIndex).getImpressionId());

                    while (questions.size() == 0) {
                        currentImpressionIndex++;

                        if (currentImpressionIndex >= impressionsSymptoms.size()) {
                            currentImpressionIndex = impressionsSymptoms.size() - 1;

                            return null;
                        }
                    }

                    return getQuestion();
                }

            } else {
                return getQuestion();
            }

        } else {
            if (!isSymptomFamilyPositive(questions.get(currentSymptomIndex).getSymptomFamilyId())) {
                currentSymptomIndex++;
                if (currentSymptomIndex >= questions.size()) {
                    currentSymptomIndex = 0;
                    currentImpressionIndex++;


                    if (currentImpressionIndex >= impressionsSymptoms.size()) {
                        currentImpressionIndex = impressionsSymptoms.size() - 1;

                        return null;
                    } else {
                        getQuestions(impressionsSymptoms.get(currentImpressionIndex).getImpressionId());

                        while (questions.size() == 0) {
                            currentImpressionIndex++;
                            if (currentImpressionIndex >= impressionsSymptoms.size()) {
                                currentImpressionIndex = impressionsSymptoms.size() - 1;

                                return null;
                            }
                        }

                        return getQuestion();
                    }

                } else {
                    return getQuestion();
                }

            } else {
                return getQuestion();
            }

        }
    }

    /**
     * Update the answered flag of the symptom with ID {@code symptomId}.
     *
     * @param symptomId
     */
    private void updateAnsweredFlagPositive(int symptomId) {
        getBetterDb.updateAnsweredFlagPositive(symptomId);
    }

    /**
     * Add {@code answer} in {@link #answers}.
     *
     * @param answer to be added.
     */
    private void addToAnswers(PatientAnswers answer) {

        if (!answers.contains(answer)) {
            answers.add(answer);
        }

    }

    /**
     * Update the answer status of the symptom family with ID {@code symptomFamilyId}.
     *
     * @param symptomFamilyId
     */
    private boolean isSymptomFamilyPositive(int symptomFamilyId) {
        return getBetterDb.symptomFamilyAnswerStatus(symptomFamilyId);
    }

    /**
     * Generate HPI using the information and answers of patient.
     *
     * @return HPI of the patient.
     */
    public String getHPI() {
        StringBuilder HPI = new StringBuilder(generateIntroductionSentence());

        if (patientChiefComplaints != null) {
            ArrayList<PositiveResults> positiveSymptoms;
            String subjectivePronoun;
            String objectivePronoun;

            if (patient.getGender() == 0) {
                subjectivePronoun = "He";
                objectivePronoun = "His";
            } else {
                subjectivePronoun = "She";
                objectivePronoun = "Her";
            }

            positiveSymptoms = getBetterDb.getPositiveSymptoms(answers);

            for (int i = 0; i < positiveSymptoms.size(); i++) {
                if (positiveSymptoms.get(i).getPositiveName().equals("High fever")) {
                    HPI.append(objectivePronoun).append(" ").append(positiveSymptoms.get(i).getPositiveAnswerPhrase()).append(". ");
                } else {
                    HPI.append(subjectivePronoun).append(" ").append(positiveSymptoms.get(i).getPositiveAnswerPhrase()).append(". ");
                }
            }
        }

        return HPI.toString();
    }

    /**
     * Get introduction sentence for the HPI generation.
     *
     * @return introduction sentence of the patient's HPI.
     */
    private String generateIntroductionSentence() {
        String introductionSentence;
        String patientGender = getGender(patient.getGender());
        String patientName = patient.getFirstName() + " " + patient.getLastName();
        int patientAge = getAge(patient.getBirthday());

        if (patientChiefComplaints == null) { //if patient has no chief complaints
            introductionSentence = "A " + patientGender + " patient, " + patientName + ", who is " + patientAge +
                    " years old, has a complaint that is not within the scope of the expert system.";
        } else {
            introductionSentence = "A " + patientGender + " patient, " + patientName + ", who is " + patientAge +
                    " years old, is complaining about " + attachComplaints();

        }

        return introductionSentence;
    }

    /**
     * Put together the chief complaints of the
     * patient into a phrase.
     *
     * @return phrase that contains the chief complaints of the patient.
     * @see #generateIntroductionSentence()
     */
    private String attachComplaints() {

        StringBuilder complaints = new StringBuilder("");

        String[] chiefComplaints = getChiefComplaints();

        if (chiefComplaints.length == 1) {
            complaints.append(chiefComplaints[0].toLowerCase());
        } else if (chiefComplaints.length > 1) {
            complaints.append(chiefComplaints[0].toLowerCase());

            for (int i = 1; i < chiefComplaints.length - 1; i++) {
                complaints.append(", ").append(chiefComplaints[i].toLowerCase());
            }

            complaints.append(", and ").append(chiefComplaints[chiefComplaints.length - 1].toLowerCase());
        }

        complaints.append(". ");
        return complaints.toString();
    }

    /**
     * Get the list of english phrases of the patient's chief complaints.
     *
     * @return list of english phrases of the patient's chief complaints.
     * @see DatabaseAdapter#getChiefComplaints(int)
     */
    private String[] getChiefComplaints() {
        String[] chiefComplaints = new String[patientChiefComplaints.size()];

        for (int i = 0; i < chiefComplaints.length; i++) {
            chiefComplaints[i] = getBetterDb.getChiefComplaints(patientChiefComplaints.get(i).getComplaintID());
        }

        return chiefComplaints;
    }

    /**
     * Gets the string gender of the patient.
     *
     * @param gender int gender of the patient.
     * @return "male" if {@code gender} is equal to 0, else return "female"
     */
    private String getGender(int gender) {
        return (gender == 0 ? "male" : "female");
    }

    /**
     * Gets the age depending on the birthdate
     *
     * @param birthdayString birthday of the patient.
     * @return age of the patient.
     */
    private int getAge(String birthdayString) {
        Calendar birthday = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();

        try {
            birthday.setTime(dateFormat.parse(birthdayString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) ((currentDate.getTimeInMillis() - birthday.getTimeInMillis()) / 31536000000L); //divides by number of milliseconds in a year
    }

}
