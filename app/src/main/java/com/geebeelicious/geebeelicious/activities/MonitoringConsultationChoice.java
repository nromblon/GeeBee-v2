package com.geebeelicious.geebeelicious.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.interfaces.ECAActivity;
import com.geebeelicious.geebeelicious.models.consultation.Patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The MonitoringConsultationChoice class serves as the activity
 * allowing the user to choose between the monitoring or
 * consultation modules.
 *
 * @author Katrina Lacsamana
 */

public class MonitoringConsultationChoice extends ECAActivity {
    /**
     * Used as a flag whether the ECA has spoken.
     */
    private boolean hasSpoken;

    /**
     * keeps the string format for the date
     */
    private DateFormat dateFormat;

    /**
     * Initializes views and other activity objects.
     *
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_activity_monitoring_consultation_choice);

        Button mButton = (Button) findViewById(R.id.monitoringButton);
        Button cButton =(Button)  findViewById(R.id.consultationButton);
        TextView questionView = (TextView) findViewById(R.id.questionMonitoringConsultationChoice);

        Typeface chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
        mButton.setTypeface(chalkFont);
        cButton.setTypeface(chalkFont);
        questionView.setTypeface(chalkFont);

        final Patient patient = getIntent().getParcelableExtra("patient");

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        if (savedInstanceState == null) {
            hasSpoken = false;
        } else {
            hasSpoken = savedInstanceState.getBoolean("hasSpoken");
        }

        integrateECA();

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonitoringConsultationChoice.this, MonitoringMainActivity.class);
                intent.putExtra("patient", patient);
                intent.putExtra("currentDate", dateFormat.format(new Date()));
                startActivity(intent);
            }
        });

        cButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonitoringConsultationChoice.this, ConsultationActivity.class);
                intent.putExtra("currentDate", dateFormat.format(new Date()));
                intent.putExtra("patient", patient);
                startActivity(intent);
            }
        });
    }

    /**
     * Called when the current Window of the activity gains or loses focus.
     *
     * @param hasFocus whether the window of this activity has focus
     * @see android.app.Activity#onWindowFocusChanged(boolean)
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!hasSpoken) {
                ecaFragment.sendToECAToSPeak(R.string.monitoring_consultation_choice);
                hasSpoken = true;
            }
        }

    }

    /**
     * Saves {@link #hasSpoken} inside {@code outState}
     *
     * @see android.app.Activity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasSpoken", hasSpoken);
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Starts {@link PatientListActivity} and ends the current activity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MonitoringConsultationChoice.this, PatientListActivity.class);
        startActivity(intent);
        finish();
    }
}
