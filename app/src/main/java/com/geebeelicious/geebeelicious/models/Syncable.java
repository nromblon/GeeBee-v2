package com.geebeelicious.geebeelicious.models;

import android.util.Log;

import com.geebeelicious.geebeelicious.models.consultation.HPI;
import com.geebeelicious.geebeelicious.models.consultation.Patient;
import com.geebeelicious.geebeelicious.models.consultation.School;
import com.geebeelicious.geebeelicious.models.monitoring.Record;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 A Class to encapsulate a  'syncable' list to be converted to JSON for Uploading
 */

public class Syncable {
    List<Patient> unsyncedPatients = new ArrayList<>();
    List<Record> unsyncedRecords = new ArrayList<>();
    List<School> unsyncedSchool = new ArrayList<>();
    List<HPI> unsyncedHPI = new ArrayList<>();
    //List<Municipality> unsyncedSchool = new ArrayList<>();

    public Syncable(List<Patient> unsyncedPatients, List<Record> unsyncedRecords, List<School> unsyncedSchool, List<HPI> unsyncedHPI){
        this.unsyncedPatients = unsyncedPatients;
        this.unsyncedRecords = unsyncedRecords;
        this.unsyncedSchool = unsyncedSchool;
        this.unsyncedHPI = unsyncedHPI;
    }

    public List<Patient> getUnsyncedPatients() {
        return unsyncedPatients;
    }

    public void setUnsyncedPatients(List<Patient> unsyncedPatients) {
        this.unsyncedPatients = unsyncedPatients;
    }

    public List<Record> getUnsyncedRecords() {
        return unsyncedRecords;
    }

    public void setUnsyncedRecords(List<Record> unsyncedRecords) {
        this.unsyncedRecords = unsyncedRecords;
    }

    public List<School> getUnsyncedSchool() {
        return unsyncedSchool;
    }

    public void setUnsyncedSchool(List<School> unsyncedSchool) {
        this.unsyncedSchool = unsyncedSchool;
    }

    public List<HPI> getUnsyncedHPI() {
        return unsyncedHPI;
    }

    public String getPatientJSON(){ return new GsonBuilder().serializeNulls().create().toJson(this.unsyncedPatients); }
    public String getRecordJSON(){
        return new GsonBuilder().serializeNulls().create().toJson(this.unsyncedRecords);
    }
    public String getSchoolJSON(){
        return new GsonBuilder().serializeNulls().create().toJson(this.unsyncedSchool);
    }

    public String getHPIJSON(){
        return new GsonBuilder().serializeNulls().create().toJson(this.unsyncedHPI);
    }
}
