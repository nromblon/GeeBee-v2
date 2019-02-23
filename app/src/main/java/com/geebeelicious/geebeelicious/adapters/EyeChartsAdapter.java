package com.geebeelicious.geebeelicious.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;

import java.util.ArrayList;

/**
 * The SchoolsAdapter class extends the ArrayAdapter class to allow
 * a customized ListView to display School objects.
 *
 * @author Katrina Lacsamana
 * @since 03/30/2016.
 */
public class EyeChartsAdapter extends ArrayAdapter<String> {
    /**
     * Used as font for the different UI properties.
     */
    private Typeface chalkFont;

    /**
     * Constructs a String ArrayAdapter using the {@code context} and {@code chartNames}.
     *
     * @param context    current context.
     * @param chartNames the names to be listed in the view
     * @param chalkFont  font to be used by the adapter
     */
    public EyeChartsAdapter(Context context, ArrayList<String> chartNames, Typeface chalkFont) {
        super(context, 0, chartNames);
        this.chalkFont = chalkFont;
    }

    /**
     * {@inheritDoc}
     * Sets the font of the view as {@link #chalkFont}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String chart = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eyechart_list, parent, false);
        }

        TextView chartName = (TextView) convertView.findViewById(R.id.eyechartlistTV);
        chartName.setTypeface(chalkFont);
        chartName.setText(chart);

        return convertView;
    }

    /**
     * {@inheritDoc}
     * Sets the font of the view as {@link #chalkFont}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String chart = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eyechart_list, parent, false);
        }

        TextView chartName = (TextView) convertView.findViewById(R.id.eyechartlistTV);
        chartName.setTypeface(chalkFont);
        chartName.setText(chart);

        return convertView;
    }

}
