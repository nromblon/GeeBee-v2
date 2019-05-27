package com.geebeelicious.geebeelicious.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.activities.SettingsActivity;
import com.geebeelicious.geebeelicious.models.consultation.School;

import java.util.ArrayList;

/**
 * The SchoolsAdapter class extends the ArrayAdapter class to allow
 * a customized ListView to display School objects.
 *
 * @author Katrina Lacsamana
 * @since 03/30/2016.
 */
public class SchoolsRecyclerAdapter extends RecyclerView.Adapter<SchoolsRecyclerAdapter.SchoolViewHolder> {

    /**
     * Used as font for the different UI properties.
     */
    private Typeface chalkFont;
    private ArrayList<School> schools;
    private Context schoolContext;
    private  RecyclerViewClickListener itemListener;
    private int selectedIndex = -1;

    public class SchoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_schoolName, tv_municipalityName;

        SchoolViewHolder(View itemView) {
            super(itemView);
            tv_schoolName = (TextView) itemView.findViewById(R.id.school_name_list);
            tv_municipalityName = (TextView) itemView.findViewById(R.id.school_municipality_list);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
            selectedIndex = this.getLayoutPosition();
            School selected = schools.get(selectedIndex);
            Toast.makeText(getContext(), selected.getSchoolName() + " selected", Toast.LENGTH_SHORT).show();
            Log.d("SchoolsRecyclerAdapter", "Selected Index: " + selectedIndex);
        }
    }

    public SchoolsRecyclerAdapter(Context context, ArrayList<School> schools,  RecyclerViewClickListener itemListener) {
        this.schools = schools;
        this.schoolContext = context;
        this.itemListener = itemListener;
    }

    @Override
    public SchoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_school_list, parent, false);
        return new SchoolViewHolder(itemView);
    }

    private Context getContext() {
        return schoolContext;
    }


    @Override
    public void onBindViewHolder(SchoolViewHolder holder, int position) {
        School school = schools.get(position);
        TextView schoolTitle = holder.tv_schoolName;
        schoolTitle.setText(school.getSchoolName());
        TextView municipalityTitle = holder.tv_municipalityName;
        municipalityTitle.setText(school.getMunicipality());
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public int getSelectedIndex(){return selectedIndex;}



}
