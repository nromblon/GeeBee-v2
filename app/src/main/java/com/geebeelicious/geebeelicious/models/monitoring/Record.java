package com.geebeelicious.geebeelicious.models.monitoring;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * The Record class represents a health record
 * containing information from the monitoring module
 * performed by the patient.
 *
 * @author Mary Grace Malana
 * @since 24/03/2016
 *
 * edited: added silhouette
 */
public class Record implements Parcelable {
    /**
     * Used to identify the source of a log message
     */
    public final static String TAG = "Record";

    /**
     * Database table name for Record.
     */
    public final static String TABLE_NAME = "tbl_record";

    /**
     * Database column name for storing {@link #recordID}.
     */
    public final static String C_RECORD_ID = "record_id";

    /**
     * Database column name for storing {@link #patient_id}.
     */
    public final static String C_PATIENT_ID = "patient_id";

    /**
     * Database column name for storing {@link #dateCreated}.
     */
    public final static String C_DATE_CREATED = "date_created";

    /**
     * Database column name for storing {@link #height}.
     */
    public final static String C_HEIGHT = "height";

    /**
     * Database column name for storing {@link #weight}.
     */
    public final static String C_WEIGHT = "weight";

    /**
     * Database column name for storing {@link #silhouette}.
     */
    public final static String C_SILHOUETTE = "silhouette";

    /**
     * Database column name for storing {@link #visualAcuityLeft}.
     */
    public final static String C_VISUAL_ACUITY_LEFT = "visual_acuity_left";

    /**
     * Database column name for storing {@link #visualAcuityRight}.
     */
    public final static String C_VISUAL_ACUITY_RIGHT = "visual_acuity_right";

    /**
     * Database column name for storing {@link #colorVision}.
     */
    public final static String C_COLOR_VISION = "color_vision";

    /**
     * Database column name for storing {@link #hearingLeft}.
     */
    public final static String C_HEARING_LEFT = "hearing_left";

    /**
     * Database column name for storing {@link #hearingRight}.
     */
    public final static String C_HEARING_RIGHT = "hearing_right";

    /**
     * Database column name for storing {@link #grossMotor}.
     */
    public final static String C_GROSS_MOTOR = "gross_motor";

    /**
     * Database column name for storing {@link #fineMotorDominant}.
     */
    public final static String C_FINE_MOTOR_DOMINANT = "fine_motor_dominant";

    /**
     * Database column name for storing {@link #fineMotorNDominant}.
     */
    public final static String C_FINE_MOTOR_N_DOMINANT = "fine_motor_n_dominant";

    /**
     * Database column name for storing {@link #fineMotorHold}.
     */
    public final static String C_FINE_MOTOR_HOLD = "fine_motor_hold";

    /**
     * Database column name for storing {@link #vaccination}.
     */
    public final static String C_VACCINATION = "vaccination";

    /**
     * Database column name for storing {@link #patientPicture}.
     */
    public final static String C_PATIENT_PICTURE = "patient_picture";

    /**
     * Database column name for storing {@link #remarksString}.
     */
    public final static String C_REMARKS_STRING = "remarks_string";

    /**
     * Database column name for storing {@link #remarksAudio}.
     */
    public final static String C_REMARKS_AUDIO = "remarks_audio";

    /**
     * ID of the record.
     */
    private int recordID;

    /**
     * ID of the patient that owns the record
     */
    private int patient_id;

    /**
     * Date the record is created.
     */
    private String dateCreated;

    /**
     * Height of the patient.
     */
    private double height;

    /**
     * Weight of the patient.
     */
    private double weight;

    /**
     * Silhouette of the patient.
     */
    private byte[] silhouette;

    /**
     * Visual acuity test result for the left eye
     */
    private String visualAcuityLeft;

    /**
     * Visual acuity test result for the right eye
     */
    private String visualAcuityRight;

    /**
     * Color vision test result.
     */
    private String colorVision;

    /**
     * Hearing test result for the left ear.
     */
    private String hearingLeft;

    /**
     * Hearing test result for the right eye
     */
    private String hearingRight;

    /**
     * Grossmotor test result. Pass = 0, Fail = 1, NA = 2
     */
    private int grossMotor;

    /**
     * Grossmotorremakr grossMotorRemark
     */
    private String grossMotorRemark;

    /**
     * Finemotor test result for the dominant hand. Pass = 0, Fail = 1.
     */
    private int fineMotorDominant;

    /**
     * Finemotor test result for the non-dominant hand. Pass = 0, Fail = 1.
     */
    private int fineMotorNDominant;

    /**
     * Finemotor test result for holding the pen. Pass = 0, Fail = 1.
     */
    private int fineMotorHold;

    /**
     * Vaccination of the patient
     */
    private byte[] vaccination;

    /**
     * Picture of the patient.
     */
    private byte[] patientPicture;

    /**
     * Remarks regarding the record
     */
    private String remarksString;

    /**
     * Audio recording of the remark
     * regarding the record.
     */
    private byte[] remarksAudio;

    /**
     * Constructor.
     */
    public Record() {

    }

    /**
     * Constructor.
     *
     * @param recordID           {@link #recordID}
     * @param patient_id         {@link #patient_id}
     * @param dateCreated        {@link #dateCreated}
     * @param height             {@link #height}
     * @param weight             {@link #weight}
     * @param silhouette         {@link #silhouette}
     * @param visualAcuityLeft   {@link #visualAcuityLeft}
     * @param visualAcuityRight  {@link #visualAcuityRight}
     * @param colorVision        {@link #colorVision}
     * @param hearingLeft        {@link #hearingLeft}
     * @param hearingRight       {@link #hearingRight}
     * @param grossMotor         {@link #grossMotor}
     * @param fineMotorNDominant {@link #fineMotorNDominant}
     * @param fineMotorDominant  {@link #fineMotorDominant}
     * @param fineMotorHold      {@link #fineMotorHold}
     * @param vaccination        {@link #vaccination}
     * @param patientPicture     {@link #patientPicture}
     * @param remarksString      {@link #remarksString}
     * @param remarksAudio       {@link #remarksAudio}
     */
    public Record(int recordID, int patient_id, String dateCreated, double height, double weight, byte[] silhouette,
                  String visualAcuityLeft, String visualAcuityRight, String colorVision, String hearingLeft,
                  String hearingRight, int grossMotor, /*String grossMotorRemark,*/ int fineMotorNDominant, int fineMotorDominant,
                  int fineMotorHold, byte[] vaccination, byte[] patientPicture, String remarksString, byte[] remarksAudio) {
        this.recordID = recordID;
        this.patient_id = patient_id;
        this.dateCreated = dateCreated;
        this.height = height;
        this.weight = weight;
        this.silhouette = silhouette;
        this.visualAcuityLeft = visualAcuityLeft;
        this.visualAcuityRight = visualAcuityRight;
        this.colorVision = colorVision;
        this.hearingLeft = hearingLeft;
        this.hearingRight = hearingRight;
        this.grossMotor = grossMotor;
        this.fineMotorNDominant = fineMotorNDominant;
        this.fineMotorDominant = fineMotorDominant;
        this.fineMotorHold = fineMotorHold;
        this.vaccination = vaccination;
        this.patientPicture = patientPicture;
        this.remarksString = remarksString;
        this.remarksAudio = remarksAudio;
    }

    /**
     * Constructor.
     *
     * @param in parcel to be read.
     */
    protected Record(Parcel in) {
        readParcel(in);
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     */
    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    /**
     * Gets {@link #recordID}.
     *
     * @return {@link #recordID}
     */
    public int getRecordID() {
        return recordID;
    }

    /**
     * Gets {@link #patient_id}.
     *
     * @return {@link #patient_id}
     */
    public int getPatient_id() {
        return patient_id;
    }

    /**
     * Gets {@link #dateCreated}.
     *
     * @return {@link #dateCreated}
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * Gets {@link #height}.
     *
     * @return {@link #height}
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets {@link #weight}.
     *
     * @return {@link #weight}
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets {@link #silhouette}.
     *
     * @return {@link #silhouette}
     */
    public byte[] getSilhouette() {
        return silhouette;
    }

    /**
     * Gets {@link #visualAcuityLeft}.
     *
     * @return {@link #visualAcuityLeft}
     */
    public String getVisualAcuityLeft() {
        return visualAcuityLeft;
    }

    /**
     * Gets {@link #visualAcuityRight}.
     *
     * @return {@link #visualAcuityRight}
     */
    public String getVisualAcuityRight() {
        return visualAcuityRight;
    }

    /**
     * Gets {@link #colorVision}.
     *
     * @return {@link #colorVision}
     */
    public String getColorVision() {
        return colorVision;
    }

    /**
     * Gets {@link #hearingLeft}.
     *
     * @return {@link #hearingLeft}
     */
    public String getHearingLeft() {
        return hearingLeft;
    }

    /**
     * Gets {@link #hearingRight}.
     *
     * @return {@link #hearingRight}
     */
    public String getHearingRight() {
        return hearingRight;
    }

    /**
     * Gets {@link #grossMotor}.
     *
     * @return {@link #grossMotor}
     */
    public int getGrossMotor() {
        return grossMotor;
    }

    /**
     * Gets {@link #fineMotorDominant}.
     *
     * @return {@link #fineMotorDominant}
     */
    public int getFineMotorDominant() {
        return fineMotorDominant;
    }

    /**
     * Gets {@link #fineMotorNDominant}.
     *
     * @return {@link #fineMotorNDominant}
     */
    public int getFineMotorNDominant() {
        return fineMotorNDominant;
    }

    /**
     * Gets {@link #fineMotorHold}.
     *
     * @return {@link #fineMotorHold}
     */
    public int getFineMotorHold() {
        return fineMotorHold;
    }

    /**
     * Gets {@link #vaccination}.
     *
     * @return {@link #vaccination}
     */
    public byte[] getVaccination() {
        return vaccination;
    }

    /**
     * Gets {@link #patientPicture}.
     *
     * @return {@link #patientPicture}
     */
    public byte[] getPatientPicture() {
        return patientPicture;
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
     * Sets {@link #recordID}.
     *
     * @param recordID new value
     */
    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    /**
     * Sets {@link #patient_id}.
     *
     * @param patient_id new value
     */
    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    /**
     * Sets {@link #dateCreated}.
     *
     * @param dateCreated new value
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Sets {@link #height}.
     *
     * @param height new value
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Sets {@link #weight}.
     *
     * @param weight new value
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Sets {@link #silhouette}.
     *
     * @param silhouette new value
     */
    public void setSilhouette(byte[] silhouette) {
        this.silhouette = silhouette;
    }

    /**
     * Sets {@link #visualAcuityLeft}.
     *
     * @param visualAcuityLeft new value
     */
    public void setVisualAcuityLeft(String visualAcuityLeft) {
        this.visualAcuityLeft = visualAcuityLeft;
    }

    /**
     * Sets {@link #visualAcuityRight}.
     *
     * @param visualAcuityRight new value
     */
    public void setVisualAcuityRight(String visualAcuityRight) {
        this.visualAcuityRight = visualAcuityRight;
    }

    /**
     * Sets {@link #colorVision}.
     *
     * @param colorVision new value
     */
    public void setColorVision(String colorVision) {
        this.colorVision = colorVision;
    }

    /**
     * Sets {@link #hearingLeft}.
     *
     * @param hearingLeft new value
     */
    public void setHearingLeft(String hearingLeft) {
        this.hearingLeft = hearingLeft;
    }

    /**
     * Sets {@link #hearingRight}.
     *
     * @param hearingRight new value
     */
    public void setHearingRight(String hearingRight) {
        this.hearingRight = hearingRight;
    }

    /**
     * Sets {@link #grossMotor}.
     *
     * @param grossMotor new value
     */
    public void setGrossMotor(int grossMotor) {
        this.grossMotor = grossMotor;
    }

    public String getGrossMotorRemark() {
        return grossMotorRemark;
    }

    public void setGrossMotorRemark(String grossMotorRemark) {
        this.grossMotorRemark = grossMotorRemark;
    }

    /**
     * Sets {@link #fineMotorDominant}.
     *
     * @param fineMotorDominant new value
     */
    public void setFineMotorDominant(int fineMotorDominant) {
        this.fineMotorDominant = fineMotorDominant;
    }

    /**
     * Sets {@link #fineMotorNDominant}.
     *
     * @param fineMotorNDominant new value
     */
    public void setFineMotorNDominant(int fineMotorNDominant) {
        this.fineMotorNDominant = fineMotorNDominant;
    }

    /**
     * Sets {@link #fineMotorHold}.
     *
     * @param fineMotorHold new value
     */
    public void setFineMotorHold(int fineMotorHold) {
        this.fineMotorHold = fineMotorHold;
    }

    /**
     * Sets {@link #vaccination}.
     *
     * @param vaccination new value
     */
    public void setVaccination(byte[] vaccination) {
        this.vaccination = vaccination;
    }

    /**
     * Sets {@link #patientPicture}.
     *
     * @param patientPicture new value
     */
    public void setPatientPicture(byte[] patientPicture) {
        this.patientPicture = patientPicture;
    }

    /**
     * Sets {@link #remarksString}.
     *
     * @param remarksString new value
     */
    public void setRemarksString(String remarksString) {
        this.remarksString = remarksString;
    }

    /**
     * Sets {@link #remarksAudio}.
     *
     * @param remarksAudio new value
     */
    public void setRemarksAudio(byte[] remarksAudio) {
        this.remarksAudio = remarksAudio;
    }

    /**
     * Prints the contents of the Record object.
     */
    public void printRecord() {
        Log.d(TAG, getCompleteRecordInfo());
    }

    /**
     * Gets the contents of the Record object in string form.
     *
     * @return contents of the record.
     */
    public String getCompleteRecordInfo() {
        return "recordID: " + recordID + ", patientID: " + patient_id + ", dateCreated: " + dateCreated +
                ", height: " + height + ", weight: " + weight + ", silhouette: " + silhouette + ", visualAcuityLeft: " + visualAcuityLeft +
                ", visualAcuityRight: " + visualAcuityRight + ", colorVision: " + colorVision +
                ", hearingLeft: " + hearingLeft + ", hearingRight: " + hearingRight +
                ", grossMotor: " + grossMotor + ", grossMotorRemark: " + grossMotorRemark +
                ", fineMotorDominant: " + fineMotorDominant + ", fineMotorNonDominant: " + fineMotorNDominant +
                ", fineMotorPen: " + fineMotorHold + ", vaccination: " + vaccination +
                ", patientPicture: " + patientPicture + ", remarksString: " + remarksString +
                ", remarksAudio: " + remarksAudio;
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
        dest.writeInt(recordID);
        dest.writeInt(patient_id);
        dest.writeString(dateCreated);
        dest.writeDouble(height);
        dest.writeDouble(weight);
        dest.writeByteArray(silhouette);
        dest.writeString(visualAcuityLeft);
        dest.writeString(visualAcuityRight);
        dest.writeString(colorVision);
        dest.writeString(hearingLeft);
        dest.writeString(hearingRight);
        dest.writeInt(grossMotor);
        dest.writeString(grossMotorRemark);
        dest.writeInt(fineMotorDominant);
        dest.writeInt(fineMotorNDominant);
        dest.writeInt(fineMotorHold);
        dest.writeByteArray(vaccination);
        dest.writeByteArray(patientPicture);
        dest.writeString(remarksString);
        dest.writeByteArray(remarksAudio);
    }

    /**
     * Reads back each field in the order that it was written to the parcel.
     *
     * @param in parcel to be read
     */
    public void readParcel(Parcel in) {
        recordID = in.readInt();
        patient_id = in.readInt();
        dateCreated = in.readString();
        height = in.readDouble();
        weight = in.readDouble();
        silhouette = in.createByteArray();
        visualAcuityLeft = in.readString();
        visualAcuityRight = in.readString();
        colorVision = in.readString();
        hearingLeft = in.readString();
        hearingRight = in.readString();
        grossMotor = in.readInt();
        grossMotorRemark = in.readString();
        fineMotorDominant = in.readInt();
        fineMotorNDominant = in.readInt();
        fineMotorHold = in.readInt();
        vaccination = in.createByteArray();
        patientPicture = in.createByteArray();
        remarksString = in.readString();
        remarksAudio = in.createByteArray();
    }
}
