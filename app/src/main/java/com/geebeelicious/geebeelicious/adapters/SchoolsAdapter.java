package com.geebeelicious.geebeelicious.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.models.consultation.School;

import java.util.ArrayList;

/**
 * The SchoolsAdapter class extends the ArrayAdapter class to allow
 * a customized ListView to display School objects.
 *
 * @author Katrina Lacsamana
 * @since 03/30/2016.
 */
public class SchoolsAdapter extends ArrayAdapter<School> {

    /**
     * Used as font for the different UI properties.
     */
    private Typeface chalkFont;

    /**
     * Constructs a School ArrayAdapter using the {@code context} and {@code schools}.
     *
     * @param context   current context.
     * @param schools   all schools from the database
     * @param chalkFont font to be used by the adapter
     */
    public SchoolsAdapter(Context context, ArrayList<School> schools, Typeface chalkFont) {
        super(context, 0, schools);
        this.chalkFont = chalkFont;
    }

    /**
     * {@inheritDoc}
     * Sets the font of the view as {@link #chalkFont}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        School school = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_school_list, parent, false);
        }

        TextView schoolName = (TextView) convertView.findViewById(R.id.school_name_list);
        TextView schoolMunicipality = (TextView) convertView.findViewById(R.id.school_municipality_list);
        schoolName.setTypeface(chalkFont);
        schoolMunicipality.setTypeface(chalkFont);

        schoolName.setText(school.getSchoolName());
        schoolMunicipality.setText(school.getMunicipality());

        return convertView;
    }

    /**
     * {@inheritDoc}
     * Sets the font of the view as {@link EyeChartsAdapter#chalkFont}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        School school = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_school_list, parent, false);
        }

        TextView schoolName = (TextView) convertView.findViewById(R.id.school_name_list);
        TextView schoolMunicipality =  (TextView)convertView.findViewById(R.id.school_municipality_list);
        schoolName.setTypeface(chalkFont);
        schoolMunicipality.setTypeface(chalkFont);

        schoolName.setText(school.getSchoolName());
        schoolMunicipality.setText(school.getMunicipality());

        return convertView;
    }

}
