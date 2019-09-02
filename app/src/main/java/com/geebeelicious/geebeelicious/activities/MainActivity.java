package com.geebeelicious.geebeelicious.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.database.DatabaseAdapter;
import com.geebeelicious.geebeelicious.interfaces.ECAActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.sql.SQLException;

/**
 * The MainActivity serves as the activity containing
 * the welcome screen and allows access to the Settings activity.
 *
 * @author Katrina Lacsamana
 */

public class MainActivity extends ECAActivity {
    /**
     * Used as a flag whether the ECA has spoken.
     */
    private boolean hasSpoken;

    public final String TAG = "MainActivity";

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    /**
     * Initializes views and other activity objects.
     *
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // OpenCV Initialization (Might want to move this to GBCapture Component)
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        // End of OpenCV Initialization

        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_activity_main);
        Button startButton = (Button) findViewById(R.id.startButton_re);
        Typeface chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
        startButton.setTypeface(chalkFont);
        ImageView settingsButton = (ImageView) findViewById(R.id.settingsButton);

        // init VHMobile
        integrateECA();


        if (savedInstanceState == null) {
            hasSpoken = false;

            DatabaseAdapter getBetterDb = new DatabaseAdapter(MainActivity.this);
            try {
                getBetterDb.createDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
                finish(); //exit app if database creation fails
            }
        } else {
            hasSpoken = savedInstanceState.getBoolean("hasSpoken");
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
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
                //Welcome message
                ecaFragment.sendToECAToSPeak(R.string.app_intro);
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
}
