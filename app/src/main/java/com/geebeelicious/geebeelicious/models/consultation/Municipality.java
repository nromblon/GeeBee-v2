package com.geebeelicious.geebeelicious.models.consultation;

/**
 * The Municipality class represents a
 * Municipality with an municipality ID, name,
 * province, and regions
 *
 * @author Katrina Lacsamana
 * @since 03/30/2016
 */
public class Municipality {

    /**
     * Database table name for Municipality.
     */
    public final static String TABLE_NAME = "tbl_municipality";

    /**
     * Database column name for storing the municipality ID.
     */
    public final static String C_MUNICIPALITY_ID = "citymunCode";

    /**
     * Database column name for storing municipality name.
     */
    public final static String C_NAME = "citymunDesc";

    /**
     * Database column name for storing the province that
     * the municipality belongs in.
     */
    public final static String C_PROVINCE = "province";

    /**
     * Database column name for storing the region that
     * the municipality belongs in.
     */
    public final static String C_REGION_ID = "region";
}
