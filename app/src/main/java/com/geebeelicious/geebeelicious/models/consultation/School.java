package com.geebeelicious.geebeelicious.models.consultation;

/**
 * Created by mgmalana on 24/03/2016.
 * The School class represents a School
 * containing the school name, schoolID, and
 * municipality to which the school belongs.
 *
 * @author Mary Grace Malana
 */
public class School {

    /**
     * Database table name for school.
     */
    public static final String TABLE_NAME = "tbl_school";

    /**
     * Database column name for storing {@link #schoolName}.
     */
    public static final String C_SCHOOLNAME = "name";

    /**
     * Database column name for storing {@link #schoolId}.
     */
    public static final String C_SCHOOL_ID = "school_id";

    /**
     * Database column name for storing {@link #municipality}.
     */
    public static final String C_MUNICIPALITY = "municipality";

    /**
     * Database column name for storing {@link #schoolId}.
     */
    private int schoolId;

    /**
     * Database column name for storing {@link #schoolName}.
     */
    private String schoolName;

    /**
     * Database column name for storing {@link #municipality}.
     */
    private String municipality;

    /**
     * Constructor.
     *
     * @param schoolId     {@link #schoolId}
     * @param schoolName   {@link #schoolName}
     * @param municipality {@link #municipality}
     */
    public School(int schoolId, String schoolName, String municipality) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.municipality = municipality;
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
     * Gets {@link #schoolName}
     *
     * @return {@link #schoolName}
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * Gets {@link #municipality}
     *
     * @return {@link #municipality}
     */
    public String getMunicipality() {
        return municipality;
    }
}
