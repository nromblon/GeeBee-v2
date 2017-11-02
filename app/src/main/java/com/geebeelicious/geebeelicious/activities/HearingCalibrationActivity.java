package com.geebeelicious.geebeelicious.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.models.hearing.Calibrator;

import org.w3c.dom.Text;

/**
 * The HearingCalibrationActivity serves as the activity
 * for the hearing test calibration module. It uses the
 * Calibrator class to perform the calibration.
 *
 * @author Katrina Lacsamana
 */

public class HearingCalibrationActivity extends ActionBarActivity {
    /**
     * Used for calibrating the volume of the sounds used for the hearing test
     */
    private Calibrator calibrator;

    /**
     * Runs the calibration process
     */
    private Thread calibrationThread;

    /**
     * Used as font for the different UI properties.
     */
    private Typeface chalkFont;

    /**
     * Initializes views and other activity objects.
     *
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing_calibration);

        chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
        TextView calibratingTV = (TextView) findViewById(R.id.calibrationInProgressTextView);
        calibratingTV.setTypeface(chalkFont);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 9, 0);

        calibrator = new Calibrator(getApplicationContext());
        calibrationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                calibrator.calibrate();
                endCalibration();
            }
        });
        calibrationThread.start();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Stops the {@link #calibrator}from calibrating. Interrupts {@link #calibrationThread}.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Calibrator.stopThread();
        calibrationThread.interrupt();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Stops the {@link #calibrator}from calibrating. Interrupts {@link #calibrationThread}.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Calibrator.stopThread();
        calibrationThread.interrupt();
    }

    /**
     * If calibration is successful, it ends the calibration and starts SettingsActivity,
     * else it loops the calibration process until successful.
     */
    private void endCalibration() {
        Calibrator.stopThread();
        calibrationThread.interrupt();
        if (calibrator.isDone() && calibrator.isValid()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.calibrationProgressBar);
                    TextView textView = (TextView) findViewById(R.id.calibrationInProgressTextView);
                    chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
                    textView.setTypeface(chalkFont);
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setText("Calibration Done!");
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.calibrationProgressBar);
                    TextView textView = (TextView) findViewById(R.id.calibrationInProgressTextView);
                    chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
                    textView.setTypeface(chalkFont);
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setText("Calibration failed. Let's try again. Please move your earphones 2cm from the mic.");
                }
            });
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HearingCalibrationActivity.this, SettingsActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }, 5000);
            }
        });
    }

}
