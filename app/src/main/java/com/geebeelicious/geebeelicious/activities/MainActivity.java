package com.geebeelicious.geebeelicious.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.database.DatabaseAdapter;
import com.geebeelicious.geebeelicious.interfaces.ECAActivity;

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

    /**
     * Initializes views and other activity objects.
     *
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_activity_main);
        ImageButton startButton = (ImageButton) findViewById(R.id.startButton_re);
//        Typeface chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
//        startButton.setTypeface(chalkFont);
        ImageView settingsButton = (ImageView) findViewById(R.id.settingsButton);

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
