package com.geebeelicious.geebeelicious.models.consultation;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * The Patient class represents a Patient and implements Parcelable
 * to allow for the object to be passed between activities.
 * The class contains patient information.
 *
 * @author Mary Grace Malana
 * @since 24/03/2016
 */
public class Patient implements Parcelable {

    /**
     * Used to identify the source of a log message
     */
    private static final String TAG = "Patient";

    /**
     * ID of the patient
     */
    private int patientID;

    /**
     * First name of the patient
     */
    private String firstName;

    /**
     * Last name of the patient
     */
    private String lastName;

    /**
     * Birthday of the patient
     */
    private String birthday;

    /**
     * Gender of the patient. 0 for MALE, 1 for FEMALE
     */
    private int gender;

    /**
     * ID of the school of the patient
     */
    private int schoolId;

    /**
     * Handedness of the patient. 0 for RIGHT, 1 for LEFT
     */
    private int handedness;

    /**
     * Remark regarding the patient
     */
    private String remarksString;

    /**
     * Audio recording of the remark
     * regarding the patient.
     */
    private byte[] remarksAudio;

    /**
     * Database column name for storing {@link #patientID}.
     */
    public final static String C_PATIENT_ID = "patient_id";

    /**
     * Database column name for storing {@link #firstName}.
     */
    public final static String C_FIRST_NAME = "first_name";

    /**
     * Database column name for storing {@link #lastName}.
     */
    public final static String C_LAST_NAME = "last_name";

    /**
     * Database column name for storing {@link #birthday}.
     */
    public final static String C_BIRTHDAY = "birthday";

    /**
     * Database column name for storing {@link #gender}.
     */
    public final static String C_GENDER = "gender";

    /**
     * Database column name for storing {@link #schoolId}.
     */
    public final static String C_SCHOOL_ID = "school_id";

    /**
     * Database column name for storing {@link #handedness}.
     */
    public final static String C_HANDEDNESS = "handedness";

    /**
     * Database column name for storing {@link #remarksString}.
     */
    public final static String C_REMARKS_STRING = "remarks_string";

    /**
     * Database column name for storing {@link #remarksAudio}.
     */
    public final static String C_REMARKS_AUDIO = "remarks_audio";

    /**
     * Database table name for patient.
     */
    public final static String TABLE_NAME = "tbl_patient";

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Patient createFromParcel(Parcel in) {
                    return new Patient(in);
                }

                public Patient[] newArray(int size) {
                    return new Patient[size];
                }
            };

    /**
     * Constructor. Patient constructor used after getting
     * the Patient info from the database.
     *
     * @param patientID     {@link #patientID}
     * @param firstName     {@link #firstName}
     * @param lastName      {@link #lastName}
     * @param birthday      {@link #birthday}
     * @param gender        {@link #gender}
     * @param schoolId      {@link #schoolId}
     * @param handedness    {@link #handedness}
     * @param remarksString {@link #remarksString}
     * @param remarksAudio  {@link #remarksAudio}
     */
    public Patient(int patientID, String firstName, String lastName, String birthday, int gender, int schoolId, int handedness, String remarksString, byte[] remarksAudio) {
        this.patientID = patientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.schoolId = schoolId;
        this.handedness = handedness;
        this.remarksString = remarksString;
        this.remarksAudio = remarksAudio;
    }

    /**
     * Constructor. Patient constructor used for inserting the HPI
     * to the database.
     *
     * @param firstName  {@link #firstName}
     * @param lastName   {@link #lastName}
     * @param birthday   {@link #birthday}
     * @param gender     {@link #gender}
     * @param schoolId   {@link #schoolId}
     * @param handedness {@link #handedness}
     */
    public Patient(String firstName, String lastName, String birthday, int gender, int schoolId, int handedness) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.schoolId = schoolId;
        this.handedness = handedness;
    }

    /**
     * Constructor.
     *
     * @param in parcel to be read.
     */
    public Patient(Parcel in) {
        readFromParcel(in);
    }

    /**
     * @see Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes each field into the parcel.
     *
     * @see Parcelable#writeToParcel(Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(patientID);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(birthday);
        dest.writeInt(gender);
        dest.writeInt(schoolId);
        dest.writeInt(handedness);
        dest.writeString(remarksString);
        dest.writeByteArray(remarksAudio);
    }

    /**
     * Reads back each field in the order that it was written to the parcel.
     *
     * @param in parcel to be read
     */
    public void readFromParcel(Parcel in) {
        patientID = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        birthday = in.readString();
        gender = in.readInt();
        schoolId = in.readInt();
        handedness = in.readInt();
        remarksString = in.readString();
        remarksAudio = in.createByteArray();
    }

    /**
     * Gets {@link #patientID}.
     *
     * @return {@link #patientID}
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * Gets {@link #firstName}.
     *
     * @return {@link #firstName}
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets {@link #lastName}.
     *
     * @return {@link #lastName}
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets {@link #birthday}.
     *
     * @return {@link #birthday}
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * Gets {@link #gender}.
     *
     * @return {@link #gender}
     */
    public int getGender() {
        return gender;
    }

    /**
     * Gets {@link #schoolId}.
     *
     * @return {@link #schoolId}
     */
    public int getSchoolId() {
        return schoolId;
    }

    /**
     * Gets {@link #handedness}.
     *
     * @return {@link #handedness}
     */
    public int getHandedness() {
        return handedness;
    }

    /**
     * Gets string value of {@link #gender}.
     *
     * @return string value of {@link #gender}
     */
    public String getGenderString() {
        if (gender == 0) {
            return "Male";
        } else {
            return "Female";
        }
    }

    /**
     * Gets string value of {@link #handedness}.
     *
     * @return string value of {@link #handedness}
     */
    public String getHandednessString() {
        if (handedness == 0) {
            return "Right-Handed";
        } else {
            return "Left-Handed";
        }
    }

    /**
     * Gets {@link #remarksString}.
     *
     * @return {@link #remarksString}
     */
    public String getRemarksString() {
        return remarksString;
    }

    /**
     * Gets {@link #remarksAudio}.
     *
     * @return {@link #remarksAudio}
     */
    public byte[] getRemarksAudio() {
        return remarksAudio;
    }

    /**
     * Sets {@link #remarksString}
     *
     * @param remarksString new value
     */
    public void setRemarksString(String remarksString) {
        this.remarksString = remarksString;
    }

    /**
     * Sets {@link #remarksAudio}
     *
     * @param remarksAudio new value
     */
    public void setRemarksAudio(byte[] remarksAudio) {
        this.remarksAudio = remarksAudio;
    }

    /**
     * Print about the {@link Patient} attributes and
     * corresponding value.
     *
     * @return Patient info string
     */
    public void printPatient() {
        Log.d(TAG, "patientID: " + patientID + ", firstName: " + firstName +
                ", lastName: " + lastName + ", birthday: " + birthday +
                ", gender: " + gender + ", schoolId: " + schoolId +
                ", handedness: " + handedness + ", remarkString: " + remarksString +
                ", remarksAudio: " + remarksAudio);
    }
}
