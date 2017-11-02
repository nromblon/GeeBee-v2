package com.geebeelicious.geebeelicious.models.consultation;

import java.util.ArrayList;

/**
 * Getbetter.
 *
 * @author Mike Dayupay
 * @since 9/21/15
 */
public class Impressions {

    /**
     * ID of the impression
     */
    private int impressionId;

    /**
     * Impression name
     */
    private String impression;

    /**
     * Scientific name of the impression
     */
    private String scientificName;

    /**
     * Local name of the impression
     */
    private String localName;

    /**
     * Treatment Protocool for the impression
     */
    private String treatmentProtocol;

    /**
     * Remarks regarding the impression
     */
    private String remarks;

    /**
     * Symptoms that are connected to the impression.
     */
    private ArrayList<Symptom> symptoms;

    /**
     * Constructor.
     *
     * @param impressionId      {@link #impressionId}
     * @param impression        {@link #impression}
     * @param scientificName    {@link #scientificName}
     * @param localName         {@link #localName}
     * @param treatmentProtocol {@link #treatmentProtocol}
     * @param remarks           {@link #remarks}
     */
    public Impressions(int impressionId, String impression, String scientificName, String localName,
                       String treatmentProtocol, String remarks) {
        this.impressionId = impressionId;
        this.impression = impression;
        this.scientificName = scientificName;
        this.localName = localName;
        this.treatmentProtocol = treatmentProtocol;
        this.remarks = remarks;
    }

    /**
     * Gets {@link #impressionId}
     *
     * @return {@link #impressionId}
     */
    public int getImpressionId() {
        return impressionId;
    }

    /**
     * Gets {@link #impression}
     *
     * @return {@link #impression}
     */
    public String getImpression() {
        return impression;
    }


    /**
     * Sets {@link #impression}
     *
     * @param impression new value
     */
    public void setImpression(String impression) {
        this.impression = impression;
    }

    /**
     * Gets {@link #symptoms}
     *
     * @return {@link #symptoms}
     */
    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    /**
     * Sets {@link #symptoms}
     *
     * @param symptoms new value
     */
    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    /**
     * Gets {@link #scientificName}
     *
     * @return {@link #impressionId}
     */
    public String getScientificName() {
        return scientificName;
    }

    /**
     * Gets {@link #localName}
     *
     * @return {@link #localName}
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Gets {@link #treatmentProtocol}
     *
     * @return {@link #treatmentProtocol}
     */
    public String getTreatmentProtocol() {
        return treatmentProtocol;
    }

    /**
     * Gets {@link #remarks}
     *
     * @return {@link #remarks}
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Override method. Returns the hashCode.
     *
     * @see Object#hashCode()
     */

    public int hashCode() {

        return impressionId;
    }

    /**
     * Override method.
     * Objects are equal if impressionID are equal.
     *
     * @param object Object to be checked if equal
     * @return true if the {@code object} and the Impression instance
     * which called the method are equal.
     */
    @Override
    public boolean equals(Object object) {

        boolean similar = false;

        if (object != null && object instanceof Impressions) {
            similar = this.impressionId == ((Impressions) object).impressionId;
        }

        return similar;
    }
}
