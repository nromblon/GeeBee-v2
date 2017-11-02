package com.geebeelicious.geebeelicious.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.models.consultation.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * The PatientsAdapter class extends the ArrayAdapter class to allow
 * a customized ListView to display Patient objects.
 *
 * @author Katrina Lacsamana
 * @since 03/30/2016.
 */
public class PatientsAdapter extends ArrayAdapter<Patient> {

    /**
     * Contains patients filtered based on search terms
     */
    private List<Patient> patientList = null;

    /**
     * Contains all the patients from the school specified in device storage.
     */
    private ArrayList<Patient> arrayPatientList;

    /**
     * Used as font for the different UI properties.
     */
    private Typeface chalkFont;

    /**
     * Constructs a Patient ArrayAdapter using the {@code context} and {@code patient}.
     * Copy all the patients to both {@link #patientList}
     * and {@link #arrayPatientList}
     *
     * @param context   current context.
     * @param patients  all patients from the school specified in device storage.
     * @param chalkFont font to be used by the adapter
     */
    public PatientsAdapter(Context context, ArrayList<Patient> patients, Typeface chalkFont) {
        super(context, 0, patients);
        this.patientList = patients;
        this.arrayPatientList = new ArrayList<>();
        this.arrayPatientList.addAll(patientList);
        this.chalkFont = chalkFont;
    }

    /**
     * Gets the number of patients represented by the adapter
     *
     * @return size of {@link #patientList}
     */
    @Override
    public int getCount() {
        return patientList.size();
    }

    /**
     * Gets the Patient associated with the specified position from the {@link #patientList}
     *
     * @param position position of the desired Patient in the {@link #patientList}
     * @return Patient with the given position.
     */
    @Override
    public Patient getItem(int position) {
        return patientList.get(position);
    }

    /**
     * Gets the position of the item within the adapter's data set whose row id is wanted
     *
     * @param position position of the item within the adapter's data set whose row id is wanted
     * @return {@code position}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the Patient at the specified position in the data set. A view is inflated using
     * xml file item_patient_list.
     *
     * @param position    position of the item within the adapter's data set of the item whose view is wanted
     * @param convertView to be reused if not null, else inflate a new view.
     * @param parent      The parent that the view will eventually be attached to.
     * @return A View corresponding to the Patient at the specified position.
     * @see ArrayAdapter#getView(int, View, ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Patient patient = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_patient_list, parent, false);
        }
        TextView patientName = (TextView) convertView.findViewById(R.id.patient_name_list);
        TextView patientGender = (TextView) convertView.findViewById(R.id.patient_gender_list);
        TextView patientBirthDate = (TextView) convertView.findViewById(R.id.patient_birthdate_list);
        patientName.setTypeface(chalkFont);
        patientGender.setTypeface(chalkFont);
        patientBirthDate.setTypeface(chalkFont);
        patientName.setText(patient.getFirstName() + " " + patient.getLastName());
        patientGender.setText(patient.getGenderString());
        patientBirthDate.setText(patient.getBirthday());

        return convertView;
    }

    /**
     * Filter the patientList using the specified search terms
     *
     * @param searchText search terms used for filtering
     */
    public void filter(String searchText) {
        searchText = searchText.toLowerCase();
        patientList.clear();
        if (searchText.length() == 0) {
            patientList.addAll(arrayPatientList);
        } else {
            for (Patient p : arrayPatientList) {
                if (p.getFirstName().toLowerCase().contains(searchText) ||
                        p.getLastName().toLowerCase().contains(searchText)) {
                    patientList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

}
