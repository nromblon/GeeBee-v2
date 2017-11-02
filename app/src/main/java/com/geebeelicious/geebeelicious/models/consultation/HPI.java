package com.geebeelicious.geebeelicious.models.consultation;

/**
 * The HPI class represents a history of present illness (HPI).
 * It contains the HPI ID, the patient ID to which the HPI is for,
 * the text content of the HPI, and the date of creation of the HPI.
 *
 * @author Mary Grace Malana
 * @since 24/03/2016
 */
public class HPI {

    /**
     * Database table name for HPI.
     */
    public final static String TABLE_NAME = "tbl_hpi";

    /**
     * Database column name for storing {@link #hpi_id}.
     */
    public final static String C_HPI_ID = "hpi_id";

    /**
     * Database column name for storing {@link #patientId}.
     */
    public final static String C_PATIENT_ID = "patient_id";

    /**
     * Database column name for storing {@link #hpiText}.
     */
    public final static String C_HPI_TEXT = "hpi";

    /**
     * Database column name for storing {@link #dateCreated}.
     */
    public final static String C_DATE_CREATED = "date_created";

    /**
     * ID of the HPI.
     */
    private int hpi_id;

    /**
     * ID of the patient with the HPI.
     */
    private int patientId;

    /**
     * HPI of the patient.
     */
    private String hpiText;

    /**
     * Date when the HPI was created.
     */
    private String dateCreated;

    /**
     * Constructor. HPI constructor used after getting
     * the HPI from the database
     *
     * @param hpi_id      {@link #hpi_id}
     * @param patientId   {@link #patientId}
     * @param hpiText     {@link #hpiText}
     * @param dateCreated {@link #dateCreated}
     */
    public HPI(int hpi_id, int patientId, String hpiText, String dateCreated) {
        this.hpi_id = hpi_id;
        this.patientId = patientId;
        this.hpiText = hpiText;
        this.dateCreated = dateCreated;
    }

    /**
     * Constructor. HPI constructor used for inserting the HPI
     * to the database.
     *
     * @param patientId   {@link #patientId}
     * @param hpiText     {@link #hpiText}
     * @param dateCreated {@link #dateCreated}
     */
    public HPI(int patientId, String hpiText, String dateCreated) {
        this.patientId = patientId;
        this.hpiText = hpiText;
        this.dateCreated = dateCreated;
    }

    /**
     * Gets {@link #hpi_id}.
     *
     * @return {@link #hpi_id}
     */
    public int getHpi_id() {
        return hpi_id;
    }

    /**
     * Gets {@link #patientId}.
     *
     * @return {@link #patientId}
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Gets {@link #hpiText}.
     *
     * @return {@link #hpiText}
     */
    public String getHpiText() {
        return hpiText;
    }

    /**
     * Gets {@link #dateCreated}.
     *
     * @return {@link #hpi_id}
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * Gets string that contains the {@link HPI} attributes and
     * corresponding value.
     *
     * @return HPI info string
     */
    public String getCompleteHPIRecord() {
        return "hpiID: " + hpi_id + ", patientID: " + patientId +
                ", hpiText: " + hpiText + ", dateCreated: " + dateCreated;
    }
}
