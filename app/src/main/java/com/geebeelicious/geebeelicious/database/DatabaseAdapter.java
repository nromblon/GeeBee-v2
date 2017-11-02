package com.geebeelicious.geebeelicious.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geebeelicious.geebeelicious.models.consultation.ChiefComplaint;
import com.geebeelicious.geebeelicious.models.consultation.HPI;
import com.geebeelicious.geebeelicious.models.consultation.Impressions;
import com.geebeelicious.geebeelicious.models.consultation.Municipality;
import com.geebeelicious.geebeelicious.models.consultation.Patient;
import com.geebeelicious.geebeelicious.models.consultation.PatientAnswers;
import com.geebeelicious.geebeelicious.models.consultation.PositiveResults;
import com.geebeelicious.geebeelicious.models.consultation.School;
import com.geebeelicious.geebeelicious.models.consultation.Symptom;
import com.geebeelicious.geebeelicious.models.consultation.SymptomFamily;
import com.geebeelicious.geebeelicious.models.monitoring.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The DatabaseAdapter class contains methods which serve as database queries.
 * The original class used as a basis for this current version was
 * created by Mike Dayupay for HPI Generation module of the GetBetter project.
 *
 * @author Mike Dayupay
 * @author Mary Grace Malana
 */

public class DatabaseAdapter {

    /**
     * Used to identify the source of a log message
     */
    protected static final String TAG = "DatabaseAdapter";

    /**
     * use as the database of the app
     */
    private SQLiteDatabase getBetterDb;

    /**
     * Contains the helper class of the database
     */
    private DatabaseHelper getBetterDatabaseHelper;

    /**
     * Table name of the symptoms in the database
     */
    private static final String SYMPTOM_LIST = "tbl_symptom_list";

    /**
     * Table name of the symptom families in the database
     */
    private static final String SYMPTOM_FAMILY = "tbl_symptom_family";

    /**
     * URL of the remote database
     */
    private static String URL_SAVE_NAME = "http://128.199.205.226/save.php";

    private static Context context;

    /**
     * Creates a new instance of {@link DatabaseHelper}.
     *
     * @param context current context.
     */
    public DatabaseAdapter(Context context) {
        DatabaseAdapter.context = context;
        getBetterDatabaseHelper = new DatabaseHelper(context);
        //getBetterDb = getBetterDatabaseHelper.getReadableDatabase();
    }

    /**
     * Creates the database using the helper class
     *
     * @return a reference of itself
     * @throws SQLException if a database error occured
     */
    public DatabaseAdapter createDatabase() throws SQLException {

        try {
            getBetterDatabaseHelper.createDatabase();
        } catch (IOException ioe) {
            Log.e(TAG, ioe.toString() + "UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    /**
     * Opens the database for read or write unless the database problem occurs
     * that limits the user from writing to the database.
     *
     * @return a reference of itself
     * @throws SQLException if a database error occured
     * @see #openDatabaseForRead()
     */
    public DatabaseAdapter openDatabaseForRead() throws SQLException {

        try {
            getBetterDatabaseHelper.openDatabase();
            getBetterDatabaseHelper.close();
            getBetterDb = getBetterDatabaseHelper.getReadableDatabase();
        } catch (SQLException sqle) {
            Log.e(TAG, "open >>" + sqle.toString());
            throw sqle;
        }
        return this;
    }

    /**
     * Opens the database for read or write. Method call may fail
     * if a database problem occurs.
     *
     * @return a reference of itself
     * @throws SQLException if a database error occured
     * @see #openDatabaseForRead()
     */
    public DatabaseAdapter openDatabaseForWrite() throws SQLException {

        try {
            getBetterDatabaseHelper.openDatabase();
            getBetterDatabaseHelper.close();
            getBetterDb = getBetterDatabaseHelper.getWritableDatabase();
        } catch (SQLException sqle) {
            Log.e(TAG, "open >>" + sqle.toString());
            throw sqle;
        }
        return this;
    }

    /**
     * Closes the database
     */
    public void closeDatabase() {
        getBetterDatabaseHelper.close();
    }

    /**
     * Sets all symptom's {@code is_answered} column to 0.
     * 0 means that the symptom hasn't been answered yet.
     */
    public void resetSymptomAnsweredFlag() {

        ContentValues values = new ContentValues();
        values.put("is_answered", 0);
        int count = getBetterDb.update(SYMPTOM_LIST, values, null, null);

        //Log.d("updated rows reset", count + "");
    }

    /**
     * Sets all symptom family's {@code answered_flag} to 0
     * and {@code answer_status} to 1.
     */
    public void resetSymptomFamilyFlags() {

        ContentValues values = new ContentValues();
        values.put("answered_flag", 0);
        values.put("answer_status", 1);

        int count = getBetterDb.update(SYMPTOM_FAMILY, values, null, null);

        //Log.d("updated rows reset", count +"");
    }

    /**
     * Gets the list of impressions that were filtered using the chief complaints.
     *
     * @param chiefComplaints list of complaints used for filtering the impressions.
     * @return list of impression that is related to each of the chief complaints specified.
     */
    public ArrayList<Impressions> getImpressions(ArrayList<ChiefComplaint> chiefComplaints) {

        ArrayList<Impressions> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM tbl_case_impression AS i, tbl_impressions_of_complaints AS s " +
                "WHERE i._id = s.impression_id AND (");

        for (ChiefComplaint c : chiefComplaints) {
            sql.append("s.complaint_id = ").append(c.getComplaintID()).append(" OR ");
        }

        sql.delete(sql.lastIndexOf(" OR "), sql.length());

        sql.append(")");
        Log.d(TAG, "SQL Statement: " + sql);

        Cursor c = getBetterDb.rawQuery(sql.toString(), null);

        while (c.moveToNext()) {
            Impressions impressions = new Impressions(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("medical_term")),
                    c.getString(c.getColumnIndexOrThrow("scientific_name")),
                    c.getString(c.getColumnIndexOrThrow("local_name")),
                    c.getString(c.getColumnIndexOrThrow("treatment_protocol")),
                    c.getString(c.getColumnIndexOrThrow("remarks")));

            results.add(impressions);
        }

        c.close();
        return results;
    }

    /**
     * Gets the symptoms that are related to the specified impression.
     *
     * @param impressionId ID of the impression.
     * @return list of symptoms that is associated to the corresponding
     * impression which has an ID value of {@code impressionId}.
     */
    public ArrayList<Symptom> getSymptoms(int impressionId) {

        ArrayList<Symptom> results = new ArrayList<>();
        String sql = "SELECT * FROM tbl_symptom_list AS s, tbl_symptom_of_impression AS i " +
                "WHERE i.impression_id = " + impressionId + " AND s._id = i.symptom_id";

        Cursor c = getBetterDb.rawQuery(sql, null);
        while (c.moveToNext()) {
            Symptom symptom = new Symptom(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("symptom_name_english")),
                    c.getString(c.getColumnIndexOrThrow("symptom_name_tagalog")),
                    c.getString(c.getColumnIndexOrThrow("question_english")),
                    c.getString(c.getColumnIndexOrThrow("question_tagalog")),
                    c.getString(c.getColumnIndexOrThrow("responses_english")),
                    c.getString(c.getColumnIndexOrThrow("responses_tagalog")),
                    c.getInt(c.getColumnIndexOrThrow("symptom_family_id")),
                    c.getInt(c.getColumnIndexOrThrow("emotion")));

            results.add(symptom);
        }

        c.close();
        return results;
    }

    /**
     * Updates the answered_flag and answer_status row to 1 of the chief complaint
     * which has an ID value of {@code chiefComplaintId}.
     *
     * @param chiefComplaintId ID of the chief complaint to be updated.
     */
    public void updateAnsweredStatusSymptomFamily(int chiefComplaintId) {

        ContentValues values = new ContentValues();
        values.put("answered_flag", 1);
        values.put("answer_status", 1);

        int count = getBetterDb.update(SYMPTOM_FAMILY, values, "related_chief_complaint_id = " + chiefComplaintId, null);

        //Log.d("updated rows symptom family flags", count + "");
    }

    /**
     * Like {@link #getSymptoms(int)} this method gets the symptoms
     * that are related to the specified impression. However only the symptoms that
     * haven't been answered will be returned.
     *
     * @param impressionId ID of the impression.
     * @return list of unanswered symptoms that is associated to the corresponding
     * impression which has an ID value of {@code impressionId}
     */
    public ArrayList<Symptom> getQuestions(int impressionId) {

        ArrayList<Symptom> results = new ArrayList<>();
        String sql = "SELECT * FROM tbl_symptom_list AS s, tbl_symptom_of_impression AS i " +
                "WHERE i.impression_id = " + impressionId + " AND s._id = i.symptom_id AND s.is_answered = 0";

        Cursor c = getBetterDb.rawQuery(sql, null);
        Log.d("query count", c.getCount() + "");
        while (c.moveToNext()) {
            Symptom symptom = new Symptom(c.getInt(c.getColumnIndexOrThrow("_id")),
                    c.getString(c.getColumnIndexOrThrow("symptom_name_english")),
                    c.getString(c.getColumnIndexOrThrow("symptom_name_tagalog")),
                    c.getString(c.getColumnIndexOrThrow("question_english")),
                    c.getString(c.getColumnIndexOrThrow("question_tagalog")),
                    c.getString(c.getColumnIndexOrThrow("responses_english")),
                    c.getString(c.getColumnIndexOrThrow("responses_tagalog")),
                    c.getInt(c.getColumnIndexOrThrow("symptom_family_id")),
                    c.getInt(c.getColumnIndexOrThrow("emotion")));

            results.add(symptom);
        }

        c.close();
        return results;
    }

    /**
     * Returns whether the symptom family with the specified ID is answered.
     *
     * @param symptomFamilyId ID of the symptom family to be queried.
     * @return true if the symptom family is answered, else false.
     */
    public boolean symptomFamilyIsAnswered(int symptomFamilyId) {

        Log.d("id", symptomFamilyId + "");
        String sql = "SELECT answered_flag FROM tbl_symptom_family WHERE _id = " + symptomFamilyId;

        Cursor c = getBetterDb.rawQuery(sql, null);
        c.moveToFirst();


        if (c.getCount() == 0) {
            c.close();
            return true;
        } else {
            if (c.getInt(c.getColumnIndexOrThrow("answered_flag")) == 1) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        }
    }

    /**
     * Gets the symptom family which has an ID value of {@code symptomFamilyId}
     *
     * @param symptomFamilyId ID of the symptom family to be searched
     * @return symptom family with the corresponding ID
     */
    public SymptomFamily getGeneralQuestion(int symptomFamilyId) {

        String sql = "SELECT * FROM tbl_symptom_family WHERE _id = " + symptomFamilyId;

        Cursor c = getBetterDb.rawQuery(sql, null);
        c.moveToFirst();

        SymptomFamily generalQuestion;
        generalQuestion = new SymptomFamily(c.getInt(c.getColumnIndexOrThrow("_id")),
                c.getString(c.getColumnIndexOrThrow("symptom_family_name_english")),
                c.getString(c.getColumnIndexOrThrow("symptom_family_name_tagalog")),
                c.getString(c.getColumnIndexOrThrow("general_question_english")),
                c.getString(c.getColumnIndexOrThrow("responses_english")),
                c.getInt(c.getColumnIndexOrThrow("related_chief_complaint_id")));

        c.close();
        return generalQuestion;
    }


    /**
     * Updates the is_answered row to 1 of the symptom
     * which has an ID value of {@code symptomId}.
     *
     * @param symptomId ID of the symptom to be updated.
     */
    public void updateAnsweredFlagPositive(int symptomId) {

        ContentValues values = new ContentValues();
        values.put("is_answered", 1);
        int count = getBetterDb.update(SYMPTOM_LIST, values, "_id = " + symptomId, null);

        //Log.d("update rows flag positive", count + "");
    }

    /**
     * Updates the answer_flag row to 1 of the symptom family
     * which has an ID value of {@code symptomId}. Also
     * updates the value of answer_status row of the symptom family
     * to the value of {@code answer}.
     *
     * @param symptomFamilyId ID of the symptom to be updated.
     * @param answer          of the user to the symptom family question.
     */
    public void updateAnsweredStatusSymptomFamily(int symptomFamilyId, int answer) {

        String sql = "UPDATE tbl_symptom_family SET answered_flag = 1, answer_status = " + answer +
                " WHERE _id = " + symptomFamilyId;

        getBetterDb.execSQL(sql);
    }

    /**
     * Gets the symptoms that are associated as hard symptoms to the
     * which has an ID value of {@code impressionId}
     *
     * @param impressionId ID of the impression to be queried.
     * @return hard symptoms of the specified impression
     */
    public ArrayList<String> getHardSymptoms(int impressionId) {

        ArrayList<String> results = new ArrayList<>();

        String sql = "SELECT s.symptom_name_english AS symptom_name_english FROM tbl_symptom_list AS s, " +
                "tbl_symptom_of_impression AS i WHERE i.impression_id = " + impressionId +
                " AND i.hard_symptom = 1 AND i.symptom_id = s._id";
        Cursor c = getBetterDb.rawQuery(sql, null);


        while (c.moveToNext()) {
            results.add(c.getString(c.getColumnIndexOrThrow("symptom_name_english")));
        }

        c.close();
        return results;
    }

    /**
     * Returns whether the symptom family with the specified ID row answer_status is equal to 1.
     *
     * @param symptomFamilyId ID of the symptom family to be queried.
     * @return true if the symptom family row answer_status is equal to 1, else false.
     */
    public boolean symptomFamilyAnswerStatus(int symptomFamilyId) {

        Log.d("symptom family id", symptomFamilyId + "");
        String sql = "SELECT answer_status FROM tbl_symptom_family WHERE _id = " + symptomFamilyId;

        Cursor c = getBetterDb.rawQuery(sql, null);
        c.moveToFirst();

        if (c.getCount() == 0) {
            c.close();
            return false;
        } else {
            if (c.getInt(c.getColumnIndexOrThrow("answer_status")) == 1) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        }
    }

    /**
     * Gets the English phrase of the chief complaint which
     * has an ID value of {@code chiefComplaintIds}.
     *
     * @param chiefComplaintIds ID of the chief complaint to be queried.
     * @return english phrase of the chief complaint.
     */
    public String getChiefComplaints(int chiefComplaintIds) {

        String result = "";
        String sql = "SELECT chief_complaint_english FROM tbl_chief_complaint WHERE _id = " + chiefComplaintIds;
        Cursor c = getBetterDb.rawQuery(sql, null);

        c.moveToFirst();
        result = c.getString(c.getColumnIndexOrThrow("chief_complaint_english"));

        //Log.d("result", result);
        c.close();
        return result;
    }

    /**
     * Gets the symptoms that the user answered positively (Yes) to.
     * The symptoms are returned as PositiveResults objects.
     *
     * @param patientAnswers list of patient answers.
     * @return list of positive results.
     */
    public ArrayList<PositiveResults> getPositiveSymptoms(ArrayList<PatientAnswers> patientAnswers) {
        ArrayList<PositiveResults> results = new ArrayList<>();
        String delim = "";

        StringBuilder sql = new StringBuilder("Select symptom_name_english AS positiveSymptom, answer_phrase AS answerPhrase" +
                " FROM tbl_symptom_list WHERE ");

        for (PatientAnswers answer : patientAnswers) {
            if (answer.getAnswer().equals("Yes")) {
                sql.append(delim).append("_id = ").append(answer.getSymptomId());
                delim = " OR ";
            }
        }

        Log.d(TAG, "SQL Statement: " + sql);
        Cursor c = getBetterDb.rawQuery(sql.toString(), null);

        while (c.moveToNext()) {
            PositiveResults positive = new PositiveResults(c.getString(c.getColumnIndexOrThrow("positiveSymptom")),
                    c.getString(c.getColumnIndexOrThrow("answerPhrase")));

            results.add(positive);
        }

        c.close();
        return results;
    }

    /*************************
     * The succeeding code are not part of the original code created by Mike Dayupay
     */

    /**
     * Insert {@code patient} to the database.
     *
     * @param patient Patient to be added to the database.
     */
    public void insertPatient(Patient patient) {
        ContentValues values = new ContentValues();
        int row;

        values.put(Patient.C_FIRST_NAME, patient.getFirstName());
        values.put(Patient.C_LAST_NAME, patient.getLastName());
        values.put(Patient.C_BIRTHDAY, patient.getBirthday());
        values.put(Patient.C_GENDER, patient.getGender());
        values.put(Patient.C_SCHOOL_ID, patient.getSchoolId());
        values.put(Patient.C_HANDEDNESS, patient.getHandedness());
        values.put(Patient.C_REMARKS_STRING, patient.getRemarksString());
        values.put(Patient.C_REMARKS_AUDIO, patient.getRemarksAudio());

        row = (int) getBetterDb.insert(Patient.TABLE_NAME, null, values);
        Log.d(TAG, "insertPatient Result: " + row);
    }

    /**
     * Insert {@code record} to the database.
     *
     * @param record Record to be inserted to the database.
     */
    public void insertRecord(Record record) {
        ContentValues values = new ContentValues();
        int row;

        values.put(Record.C_PATIENT_ID, record.getPatient_id());
        values.put(Record.C_DATE_CREATED, record.getDateCreated());
        values.put(Record.C_HEIGHT, record.getHeight());
        values.put(Record.C_WEIGHT, record.getWeight());
        values.put(Record.C_VISUAL_ACUITY_LEFT, record.getVisualAcuityLeft());
        values.put(Record.C_VISUAL_ACUITY_RIGHT, record.getVisualAcuityRight());
        values.put(Record.C_COLOR_VISION, record.getColorVision());
        values.put(Record.C_HEARING_LEFT, record.getHearingLeft());
        values.put(Record.C_HEARING_RIGHT, record.getHearingRight());
        values.put(Record.C_GROSS_MOTOR, record.getGrossMotor());
        values.put(Record.C_FINE_MOTOR_DOMINANT, record.getFineMotorDominant());
        values.put(Record.C_FINE_MOTOR_N_DOMINANT, record.getFineMotorNDominant());
        values.put(Record.C_FINE_MOTOR_HOLD, record.getFineMotorHold());
        values.put(Record.C_VACCINATION, record.getVaccination());
        values.put(Record.C_PATIENT_PICTURE, record.getPatientPicture());
        values.put(Record.C_REMARKS_STRING, record.getRemarksString());
        values.put(Record.C_REMARKS_AUDIO, record.getRemarksAudio());

        row = (int) getBetterDb.insert(Record.TABLE_NAME, null, values);
        Log.d(TAG, "insertRecord Result: " + row);
    }

    /**
     * Insert {@code hpi} to the database.
     *
     * @param hpi HPI to be inserted to the database.
     */
    public void insertHPI(HPI hpi) {
        ContentValues values = new ContentValues();
        int row;

        values.put(HPI.C_PATIENT_ID, hpi.getPatientId());
        values.put(HPI.C_DATE_CREATED, hpi.getDateCreated());
        values.put(HPI.C_HPI_TEXT, hpi.getHpiText());

        row = (int) getBetterDb.insert(HPI.TABLE_NAME, null, values);
        Log.d(TAG, "insertHPI Result: " + row);
    }

    /**
     * Get all the schools in the database
     *
     * @return list of school retrieved from the database.
     */
    public ArrayList<School> getAllSchools() {
        try {
            openDatabaseForWrite();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<School> schools = new ArrayList<>();
        Cursor c = getBetterDb.rawQuery("SELECT " + School.C_SCHOOL_ID + ", s." + School.C_SCHOOLNAME + ", m." +
                Municipality.C_NAME + " AS municipalityName FROM " + School.TABLE_NAME + " AS s, " + Municipality.TABLE_NAME +
                " AS m WHERE s." + School.C_MUNICIPALITY + " = m." + Municipality.C_MUNICIPALITY_ID, null);
        if (c.moveToFirst()) {
            do {
                schools.add(new School(c.getInt(c.getColumnIndex(School.C_SCHOOL_ID)), c.getString(c.getColumnIndex(School.C_SCHOOLNAME)),
                        c.getString(c.getColumnIndex("municipalityName"))));
            } while (c.moveToNext());
        }
        c.close();
        Log.d(TAG, "Schools = " + schools.size());
        return schools;
    }

    /**
     * Get all records of the patient which has an ID value of
     * {@code patientID}.
     *
     * @param patientId ID of the patient to be queried.
     * @return list of records of the patient.
     */
    public ArrayList<Record> getRecords(int patientId) {
        ArrayList<Record> records = new ArrayList<>();
        Cursor c = getBetterDb.query(Record.TABLE_NAME, null, Record.C_PATIENT_ID + " = " + patientId, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                Record record = new Record(c.getInt(c.getColumnIndex(Record.C_RECORD_ID)), c.getInt(c.getColumnIndex(Record.C_PATIENT_ID)),
                        c.getString(c.getColumnIndex(Record.C_DATE_CREATED)), c.getDouble(c.getColumnIndex(Record.C_HEIGHT)),
                        c.getDouble(c.getColumnIndex(Record.C_WEIGHT)), c.getString(c.getColumnIndex(Record.C_VISUAL_ACUITY_LEFT)),
                        c.getString(c.getColumnIndex(Record.C_VISUAL_ACUITY_RIGHT)), c.getString(c.getColumnIndex(Record.C_COLOR_VISION)),
                        c.getString(c.getColumnIndex(Record.C_HEARING_LEFT)), c.getString(c.getColumnIndex(Record.C_HEARING_RIGHT)),
                        c.getInt(c.getColumnIndex(Record.C_GROSS_MOTOR)), c.getInt(c.getColumnIndex(Record.C_FINE_MOTOR_N_DOMINANT)),
                        c.getInt(c.getColumnIndex(Record.C_FINE_MOTOR_DOMINANT)), c.getInt(c.getColumnIndex(Record.C_FINE_MOTOR_HOLD)),
                        c.getBlob(c.getColumnIndex(Record.C_VACCINATION)), c.getBlob(c.getColumnIndex(Record.C_PATIENT_PICTURE)),
                        c.getString(c.getColumnIndex(Record.C_REMARKS_STRING)), c.getBlob(c.getColumnIndex(Record.C_REMARKS_AUDIO)));

                record.printRecord();
                records.add(record);

            } while (c.moveToNext());
        }
        c.close();
        return records;
    }

    /**
     * Get all HPI of the patient which has an ID value of
     * {@code patientID}.
     *
     * @param patientId ID of the patient to be queried.
     * @return list of HPI of the patient.
     */
    public ArrayList<HPI> getHPIs(int patientId) {
        ArrayList<HPI> HPIs = new ArrayList<>();
        Cursor c = getBetterDb.query(HPI.TABLE_NAME, null, HPI.C_PATIENT_ID + " = " + patientId, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                HPIs.add(new HPI(c.getInt(c.getColumnIndex(HPI.C_HPI_ID)), c.getInt(c.getColumnIndex(HPI.C_PATIENT_ID)),
                        c.getString(c.getColumnIndex(HPI.C_HPI_TEXT)), c.getString(c.getColumnIndex(HPI.C_DATE_CREATED))));
            } while (c.moveToNext());
        }
        c.close();
        return HPIs;
    }

    /**
     * Get all patients of the school which has an ID value of
     * {@code schoolID}.
     *
     * @param schoolID ID of the school to be queried.
     * @return list of records of the patient.
     */

    public ArrayList<Patient> getPatientsFromSchool(int schoolID) {
        ArrayList<Patient> patients = new ArrayList<>();
        Cursor c = getBetterDb.query(Patient.TABLE_NAME, null, Patient.C_SCHOOL_ID + " = " + schoolID, null, null, null, Patient.C_LAST_NAME + " ASC");
        if (c.moveToFirst()) {
            do {
                patients.add(new Patient(c.getInt(c.getColumnIndex(Patient.C_PATIENT_ID)),
                        c.getString(c.getColumnIndex(Patient.C_FIRST_NAME)),
                        c.getString(c.getColumnIndex(Patient.C_LAST_NAME)),
                        c.getString(c.getColumnIndex(Patient.C_BIRTHDAY)),
                        c.getInt(c.getColumnIndex(Patient.C_GENDER)),
                        c.getInt(c.getColumnIndex(Patient.C_SCHOOL_ID)),
                        c.getInt(c.getColumnIndex(Patient.C_HANDEDNESS)),
                        c.getString(c.getColumnIndex(Patient.C_REMARKS_STRING)),
                        c.getBlob(c.getColumnIndex(Patient.C_REMARKS_AUDIO))));
            } while (c.moveToNext());
        }
        c.close();
        return patients;
    }

    public ArrayList<Record> getAllUnsyncedRecords() {
        try {
            openDatabaseForWrite();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int[] remoteIndices;//0 - school-id   1 - patient_id
        ArrayList<Record> unsyncedRecords = new ArrayList<>();
        Cursor c = getBetterDb.rawQuery("SELECT * FROM tbl_record WHERE synced = 0", null);
//        Cursor c = getBetterDb.rawQuery("SELECT * FROM tbl_record", null);

        if (c.moveToFirst()) {
            do {
                //remoteIndices = queryRemoteDbIndices(c.getInt(c.getColumnIndex(Record.C_PATIENT_ID)));
                unsyncedRecords.add(new Record(
                        c.getInt(c.getColumnIndex(Record.C_RECORD_ID)),
                        /*remoteIndices[1],*/ c.getInt(c.getColumnIndex(Record.C_PATIENT_ID)),
                        c.getString(c.getColumnIndex(Record.C_DATE_CREATED)),
                        c.getDouble(c.getColumnIndex(Record.C_HEIGHT)),
                        c.getDouble(c.getColumnIndex(Record.C_WEIGHT)),
                        c.getString(c.getColumnIndex(Record.C_VISUAL_ACUITY_LEFT)),
                        c.getString(c.getColumnIndex(Record.C_VISUAL_ACUITY_RIGHT)),
                        c.getString(c.getColumnIndex(Record.C_COLOR_VISION)),
                        c.getString(c.getColumnIndex(Record.C_HEARING_LEFT)),
                        c.getString(c.getColumnIndex(Record.C_HEARING_RIGHT)),
                        c.getInt(c.getColumnIndex(Record.C_GROSS_MOTOR)),
                        c.getInt(c.getColumnIndex(Record.C_FINE_MOTOR_N_DOMINANT)),
                        c.getInt(c.getColumnIndex(Record.C_FINE_MOTOR_DOMINANT)),
                        c.getInt(c.getColumnIndex(Record.C_FINE_MOTOR_HOLD)),
                        c.getBlob(c.getColumnIndex(Record.C_VACCINATION)),
                        c.getBlob(c.getColumnIndex(Record.C_PATIENT_PICTURE)),
                        c.getString(c.getColumnIndex(Record.C_REMARKS_STRING)),
                        c.getBlob(c.getColumnIndex(Record.C_REMARKS_AUDIO))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return unsyncedRecords;
    }

    public int[] queryRemoteDbIndices(int localDbPatientId) {
        int localSchoolId = 0;
        final int[] remoteIndices = new int[2];//0 - school-id   1 - patient_id
        int lastSchoolId = 0;
        String patientFirstName = "";
        String patientLastName = "";
        String patientBirthday = "";
        /*GET SCHOOL NAME*/
        //get patient school_id id first
        Cursor c = getBetterDb.rawQuery("SELECT school_id FROM tbl_patient WHERE patient_id=?", new String[]{localDbPatientId + ""});
        if (c.getCount() > 0) {
            c.moveToFirst();
            localSchoolId = c.getInt(c.getColumnIndex(Patient.C_SCHOOL_ID));
        }

        //get School name to query remote database with
        c = getBetterDb.rawQuery("SELECT * FROM tablename ORDER BY column DESC LIMIT 1;", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            lastSchoolId = c.getInt(c.getColumnIndex(School.C_SCHOOLNAME));
        }

        /*GET PATIENT UNIQUE DETAILS*/
        //get patient id
        c = getBetterDb.rawQuery("SELECT patient_id FROM tbl_school WHERE school_id=?", new String[]{localSchoolId + ""});
        if (c.getCount() > 0) {
            c.moveToFirst();
            patientFirstName = c.getString(c.getColumnIndex(Patient.C_FIRST_NAME));
            patientLastName = c.getString(c.getColumnIndex(Patient.C_LAST_NAME));
            patientBirthday = c.getString(c.getColumnIndex(Patient.C_BIRTHDAY));
        }


        //query remote database for the remote school_id and patient_id
        final int finalLastSchoolId = lastSchoolId;
        final String finalPatientFirstName = patientFirstName;
        final String finalPatientLastName = patientLastName;
        final String finalPatientBirthday = patientBirthday;
        StringRequest request = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    JSONObject indicesObject;

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            indicesObject = array.getJSONObject(0);
                            remoteIndices[0] = indicesObject.getInt("school_id");
                            remoteIndices[1] = indicesObject.getInt("patient_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "request indices json error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Connection Error!", Toast.LENGTH_SHORT).show();
                        Log.e("RESOPNSE ERROR:", "IDK WHY THO");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("request", "query-remote-indices");
                params.put("school-name", Integer.toString(finalLastSchoolId));
                params.put("patient-firstname", finalPatientFirstName);
                params.put("patient-lastname", finalPatientLastName);
                params.put("patient-birthday", finalPatientBirthday);
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
        return remoteIndices;
    }


}
