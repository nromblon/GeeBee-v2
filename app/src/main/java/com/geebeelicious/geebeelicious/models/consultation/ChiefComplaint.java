package com.geebeelicious.geebeelicious.models.consultation;

/**
 * The ChiefComplaint class represents a chief complaint
 * in the expert system. It contains a complaintID and a
 * question that will be asked to the user.
 *
 * @author Mary Grace Malana
 * @since 3/22/2016
 */

public class ChiefComplaint {

    /**
     * ID of the complaint
     */
    private int complaintID;

    /**
     * Chief complaint question to be asked to the user
     */
    private String question;

    /**
     * Constructor.
     *
     * @param complaintID {@link #complaintID}
     * @param question    {@link #question}
     */
    public ChiefComplaint(int complaintID, String question) {
        this.complaintID = complaintID;
        this.question = question;
    }

    /**
     * Gets {@link #complaintID}.
     *
     * @return {@link #complaintID}
     */
    public int getComplaintID() {
        return complaintID;
    }

    /**
     * Get. {@link #question}.
     *
     * @return {@link #question}
     */
    public String getQuestion() {
        return question;
    }
}
