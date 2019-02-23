package com.geebeelicious.geebeelicious.activities;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.database.DatabaseAdapter;
import com.geebeelicious.geebeelicious.fragments.ColorVisionFragment;
import com.geebeelicious.geebeelicious.fragments.ECAFragment;
import com.geebeelicious.geebeelicious.fragments.FineMotorFragment;
import com.geebeelicious.geebeelicious.fragments.GrossMotorFragment;
import com.geebeelicious.geebeelicious.fragments.HearingMainFragment;
import com.geebeelicious.geebeelicious.fragments.MonitoringFragment;
import com.geebeelicious.geebeelicious.fragments.PatientPictureFragment;
import com.geebeelicious.geebeelicious.fragments.RemarksFragment;
import com.geebeelicious.geebeelicious.fragments.VaccinationFragment;
import com.geebeelicious.geebeelicious.fragments.VisualAcuityFragment;
import com.geebeelicious.geebeelicious.interfaces.ECAActivity;
import com.geebeelicious.geebeelicious.interfaces.MonitoringTestFragment;
import com.geebeelicious.geebeelicious.interfaces.OnMonitoringFragmentInteractionListener;
import com.geebeelicious.geebeelicious.models.consultation.Patient;
import com.geebeelicious.geebeelicious.models.monitoring.Record;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The MonitoringMainActivity serves as the main activity for all the monitoring activities
 * Each test are executed through this activity.
 *
 * @author Mary Grace Malana
 */

public class MonitoringMainActivity extends ECAActivity implements OnMonitoringFragmentInteractionListener,
        RemarksFragment.OnFragmentInteractionListener {
    /**
     * Used to identify the source of a log message
     */
    private final static String TAG = "MonitoringMainActivity";

    /**
     * Record of the patient
     */
    private Record record;

    /**
     * TextView to show the instructions during the specific monitoring test
     *
     * @see #ecaTransitionText
     */
    private TextView ecaText;

    /**
     * TextView to show the results of the test
     */
    private TextView resultsText;

    /**
     * Contains the {@link ECAFragment}
     */
    private FrameLayout ecaFragmentLayout;

    /**
     * Clicked by the assitant when the child is ready to take the specific monitoring test
     */
    private Button readyButton;

    /**
     * TextView to show the instructions before the specific monitoring test
     *
     * @see #ecaText
     */
    private TextView ecaTransitionText;

    /**
     * Contains the fragment names of the monitoring tests and other module fragments
     */
    private String[] fragments;

    /**
     * Serves as the index counter for the {@link #fragments}
     */
    private int currentFragmentIndex;

    /**
     * Contains the fragmentManager of the activity
     */
    private FragmentManager fragmentManager;

    /**
     * Contains the information of current patient having the monitoring activity
     */
    private Patient patient;

    /**
     * Initializes views and other activity objects.
     *
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_main);

        ecaText = (TextView) findViewById(R.id.placeholderECAText);
        resultsText = (TextView) findViewById(R.id.placeholderResults);
        ecaFragmentLayout = (FrameLayout) findViewById(R.id.placeholderECA);
        readyButton = (Button) findViewById(R.id.readyButton);
        ecaTransitionText = (TextView) findViewById(R.id.ecaTransitionTextView);

        Typeface chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
        ecaText.setTypeface(chalkFont);
        resultsText.setTypeface(chalkFont);
        readyButton.setTypeface(chalkFont);
        ecaTransitionText.setTypeface(chalkFont);

        resultsText.setMovementMethod(new ScrollingMovementMethod());

        //so that the fragments can be dynamically initialized
        fragments = new String[]{ //does not include the initial fragment
                MonitoringFragment.class.getName(),
                PatientPictureFragment.class.getName(),
                VaccinationFragment.class.getName(),
                VisualAcuityFragment.class.getName(),
                ColorVisionFragment.class.getName(),
                HearingMainFragment.class.getName(),
                GrossMotorFragment.class.getName(),
                FineMotorFragment.class.getName(),
                RemarksFragment.class.getName()
        };

        fragmentManager = getSupportFragmentManager();


        if (savedInstanceState == null) { //if first launch
            Bundle patientRecord = getIntent().getExtras();

            currentFragmentIndex = 0;

            patient = patientRecord.getParcelable("patient");
            record = new Record();
            record.setDateCreated(patientRecord.getString("currentDate"));
            record.setPatient_id(patient.getPatientID());
        } else {
            currentFragmentIndex = savedInstanceState.getInt("fragmentIndex");
            patient = savedInstanceState.getParcelable("patient");
            record = savedInstanceState.getParcelable("record");
        }

        integrateECA();
        initializeOldFragment();
    }

    /**
     * Saves {@link #record}, {@link #patient},
     * and {@link #currentFragmentIndex} inside {@code outState}
     *
     * @see android.app.Activity#onSaveInstanceState(Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("record", record);
        outState.putParcelable("patient", patient);
        outState.putInt("fragmentIndex", currentFragmentIndex);
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Calls onBackPressed of current monitoring test and ends the current activity.
     */
    @Override
    public void onBackPressed() {
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragments[currentFragmentIndex]);

        if (currentFragment instanceof GrossMotorFragment) {
            ((GrossMotorFragment) currentFragment).onBackPressed();
        } else if (currentFragment instanceof HearingMainFragment) {
            ((HearingMainFragment) currentFragment).onBackPressed();
        }

        finish();
    }

    /**
     * @return {@link #record}
     */
    @Override
    public Record getRecord() {
        return record;
    }

    /**
     * Sets text of {@link #ecaText} and sends instructions to ECA.
     *
     * @param instructions instructions to be sent.
     */
    @Override
    public void setInstructions(String instructions) {
        ecaText.setText(instructions);
        ecaFragment.sendToECAToSpeak(instructions);
    }

    /**
     * Sets text of {@link #ecaText} and sends instructions to ECA.
     *
     * @param resID String resource ID of the instructions to be sent.
     */
    @Override
    public void setInstructions(int resID) {
        ecaText.setText(resID);
        ecaFragment.sendToECAToSPeak(resID);
    }

    /**
     * Appends text in {@link #resultsText}.
     *
     * @param results coming from the monitoring test.
     */
    @Override
    public void setResults(String results) {
        resultsText.append("\n" + results);
    }

    /**
     * Gets called by the fragments when done.
     * Handles the changing of fragments depending on the next fragment.
     */
    @Override
    public void doneFragment() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment currentFragment = fragmentManager.findFragmentByTag(fragments[currentFragmentIndex]);

                if (currentFragmentIndex + 1 >= fragments.length) { //if last fragment
                    endActivity(currentFragment);
                } else {
                    clearTextViews();
                    try {
                        currentFragmentIndex++;
                        Fragment nextFragment = (Fragment) Class.forName(fragments[currentFragmentIndex]).newInstance();
                        if (currentFragment instanceof MonitoringTestFragment) { //if the current has intro
                            doTransitionWithResult((MonitoringTestFragment) currentFragment, nextFragment);
                        } else if (doesNextHasIntro(nextFragment)) { //if the next has intro
                            runTransition(100, "", nextFragment, true);
                        } else {
                            replaceFragment(nextFragment);
                        }

                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        Log.e(TAG, "Error in initializing the fragment", e);
                    }
                }
            }
        });
    }

    /**
     * Gets the int value of the result depending from the given string result.
     * Returns 0 if “Pass”, 1 if “Fail”, else 2.
     *
     * @see OnMonitoringFragmentInteractionListener#getIntResults(String)
     */
    @Override
    public int getIntResults(String result) {
        switch (result) {
            case "Pass":
                return 0;
            case "Fail":
                return 1;
            default:
                return 2;
        }
    }

    /**
     * Computes for the age of the patient using the birthdate of the patient
     *
     * @return age of patient on the current day
     */
    @Override
    public int getAge() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date dateOfBirth = dateFormat.parse(patient.getBirthday());
            Calendar dob = Calendar.getInstance();
            dob.setTime(dateOfBirth);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
                age--;
            Log.d(TAG, "Patient age: " + age);
            return age;
        } catch (ParseException e) {
            Log.d(TAG, "Error in reading birthday", e);
        }
        return 0;
    }

    /**
     * Gets the patient's gender.
     *
     * @return True if patient is female, else False.
     */
    @Override
    public boolean isGirl() {
        return patient.getGender() == 1;
    }

    /**
     * Shows {@link #ecaTransitionText} parent view
     */
    @Override
    public void showTransitionTextLayout() {
        ((View) ecaTransitionText.getParent()).setVisibility(View.VISIBLE);
    }

    /**
     * Appends the instructions of the monitoring test with the ready instructions.
     * Also sends the complete instructions to the {@link #ecaFragment}.
     *
     * @param instructions to before the monitoring test starts.
     */
    @Override
    public void appendTransitionIntructions(String instructions) {
        instructions = " " + instructions + " " + getString(R.string.monitoring_ready);
        ecaTransitionText.append(instructions);
        ecaFragment.sendToECAToSpeak(ecaTransitionText.getText().toString());
    }

    /**
     * Clears the {@link #ecaText} and {@link #resultsText}
     * after the monitoring test.
     */
    private void clearTextViews() {
        ecaText.setText("");
        resultsText.setText("");
    }

    /**
     * Iniitializes the current or the first fragment for the activity depending
     * on the {@link #currentFragmentIndex}.
     */
    private void initializeOldFragment() {
        Fragment oldFragment = fragmentManager.findFragmentByTag(fragments[currentFragmentIndex]);
        try {
            if (oldFragment == null) {
                oldFragment = (Fragment) Class.forName(fragments[0]).newInstance();
                replaceFragment(oldFragment);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            Log.e(TAG, "Error in initializing the fragment", e);
        }
    }

    /**
     * Replaces the old fragment by the next fragment.
     *
     * @param fragment to be attached to monitoringFragmentContainer.
     */
    private void replaceFragment(final Fragment fragment) {
        shortcutForHearingfragment(fragment); //this is only used for testing

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.monitoringFragmentContainer, fragment, fragments[currentFragmentIndex]);
        if (!isFinishing()) {
            transaction.commit();
        }
    }

    private void shortcutForHearingfragment(Fragment newFragment) {
        /*******
         * TODO: [Testing Code] Remove this if no longer testing.
         * this is for the shortcut for the hearing fragment
         */
        final LinearLayout ecaLayout = (LinearLayout) findViewById(R.id.linearLayoutECA);
        Fragment hearingFragment = fragmentManager.findFragmentByTag(HearingMainFragment.class.getName());

        if (newFragment instanceof HearingMainFragment) {
            ecaLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HearingMainFragment hearingFragment = (HearingMainFragment) fragmentManager.findFragmentByTag(HearingMainFragment.class.getName());
                    hearingFragment.endTestShortCut();
                }
            });
        } else if (hearingFragment != null) {
            ecaLayout.setClickable(false);
        }
    }

    /**
     * Sets the ECA frame to full screen.
     */
    private void maximizeToFullScreenECAFragment() {
        LinearLayout ecaLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutECA);


        View parent = (View) ecaLinearLayout.getParent();
        final int mToHeight = parent.getHeight();
        final int mToWidth = parent.getWidth();
        ecaFragmentLayout.setLayoutParams(new LinearLayout.LayoutParams(mToWidth, mToHeight));
    }

    /**
     * Sets the ECA frame to full height and adjust the width as equal to the new height.
     */
    private void maximizeToBigECAFragment() {
        LinearLayout ecaLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutECA);

        View parent = (View) ecaLinearLayout.getParent();
        final int mToHeight = parent.getHeight();
        ecaFragmentLayout.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.activity_eca_big),
                mToHeight));
    }

    /**
     * Sets the ECA back to its original size
     */
    private void minimizeECAFragment() {
        ecaFragmentLayout.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.activity_eca_small),
                getResources().getDimensionPixelSize(R.dimen.activity_eca_small)));
    }

    /**
     * Runs the transition between the fragments.
     * <p>
     * If the {@code} has an early instruction, the instruction is shown
     * at the same as the ready button.
     *
     * @param time         milliseconds on which the transition is to be run before the start
     *                     of next fragment or showing of {@link #ecaTransitionText}
     *                     parent view.
     * @param ecaText      initial string sent to ECA to speak.
     * @param nextFragment the next Fragment to be run.
     * @param isHappy      whether the ecaText has happy emotion or not.
     */
    private void runTransition(int time, String ecaText, final Fragment nextFragment, final boolean isHappy) {
        CountDownTimer timer;

        if (doesNextHasIntro(nextFragment)) { //if next is a monitoring test and has intro
            maximizeToBigECAFragment();
        } else {
            maximizeToFullScreenECAFragment();
        }

        if (!isHappy) {
            ecaFragment.sendToECAToEmote(ECAFragment.Emotion.CONCERN, 1);
        }

        ecaFragment.sendToECAToSpeak(ecaText);

        timer = new CountDownTimer(time, 10000) { //timer for the transition
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!isHappy) {
                    ecaFragment.sendToECAToEmote(ECAFragment.Emotion.HAPPY, 2);
                }

                if (nextFragment != null) {
                    if (doesNextHasIntro(nextFragment)) { //if next is a monitoring test and has intro
                        final LinearLayout ecaTransitionTextLayout = (LinearLayout) findViewById(R.id.ecaTextTransitionLayout);
                        String ecaIntroText = getString(((MonitoringTestFragment) nextFragment).getIntro());

                        if (((MonitoringTestFragment) nextFragment).hasEarlyInstruction()) { //has an early instruction
                            ecaTransitionText.setText(ecaIntroText);
                            replaceFragment(nextFragment);
                            readyButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ecaTransitionTextLayout.setVisibility(View.GONE);
                                    minimizeECAFragment();
                                }
                            });
                        } else {
                            ecaIntroText += " " + getString(R.string.monitoring_ready);
                            ecaTransitionText.setText(ecaIntroText);
                            ecaFragment.sendToECAToSpeak(ecaIntroText);
                            ecaTransitionTextLayout.setVisibility(View.VISIBLE);
                            readyButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ecaTransitionTextLayout.setVisibility(View.GONE);
                                    transitionToNextFragment(nextFragment);
                                }
                            });
                        }
                    } else {
                        transitionToNextFragment(nextFragment);
                    }
                } else {
                    finish();
                }
            }
        };
        timer.start();

    }

    /**
     * Calls {@link #minimizeECAFragment()}
     * and {@link #replaceFragment(Fragment)}.
     *
     * @param nextFragment the next Fragment to be run.
     */
    private void transitionToNextFragment(Fragment nextFragment) {
        minimizeECAFragment();
        replaceFragment(nextFragment);
    }

    /**
     * Gets whether the nextFragment has an intro or not.
     *
     * @param nextFragment nextFragment to be run.
     * @return true if nextFragment has intro, else false.
     */
    private boolean doesNextHasIntro(Fragment nextFragment) {
        return nextFragment instanceof MonitoringTestFragment;
    }

    /**
     * Runs transition with result from the current fragment.
     *
     * @param currentFragment fragment of the current test.
     * @param nextFragment    fragment of the next test.
     */
    private void doTransitionWithResult(MonitoringTestFragment currentFragment, Fragment nextFragment) {
        String ecaText = tryGettingStringResource(currentFragment.getEndStringResource());
        int time = currentFragment.getEndTime();
        boolean isHappy = currentFragment.isEndEmotionHappy();

        currentFragment.hideFragmentMainView();
        runTransition(time, ecaText, nextFragment, isHappy);

    }

    /**
     * Runs conclusion segment of the activity, then ends the activity.
     * If the currentFragment has results, it sends the results to the ECA.
     *
     * @param currentFragment fragment of the current test
     */
    private void endActivity(Fragment currentFragment) {
        DatabaseAdapter db = new DatabaseAdapter(this);
        String ecaText = "";
        int time = 0;
        try {
            db.openDatabaseForRead();
            record.printRecord();

            db.insertRecord(record);
        } catch (SQLException e) {
            Log.e(TAG, "Database error");
        }

        if (currentFragment instanceof MonitoringTestFragment) {
            ecaText = tryGettingStringResource(((MonitoringTestFragment) currentFragment).getEndStringResource());
            time = ((MonitoringTestFragment) currentFragment).getEndTime();
        }
        ecaText += " " + getString(R.string.monitoring_end);
        time += 3000;
        runTransition(time, ecaText, null, true);
    }

    /**
     * Tries to get string, given resource ID. If invalid returns "",
     * else returns string corresponding to the string resource ID.
     *
     * @param res resource id of the string.
     * @return String resource
     */
    private String tryGettingStringResource(int res) {
        try {
            return getString(res);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Resource not found", e);
        }
        return "";
    }

    /**
     * Implemented from the RemarksFragment.OnFragmentInteractionListener interface.
     * Sets the {@link Record#remarksString} and {@link Record#remarksAudio} of the patient.
     * calls {@link  #doneFragment()}
     *
     * @see RemarksFragment.OnFragmentInteractionListener#onDoneRemarks(String remarkString, byte[] remarkAudio).
     */
    @Override
    public void onDoneRemarks(String remarkString, byte[] remarkAudio) {
        record.setRemarksString(remarkString);
        record.setRemarksAudio(remarkAudio);
        doneFragment();
    }

    /**
     * Implemented from the RemarksFragment.OnFragmentInteractionListener interface.
     * Calls {@link  #doneFragment()}
     *
     * @see RemarksFragment.OnFragmentInteractionListener#onDoneRemarks(String remarkString, byte[] remarkAudio).
     */
    @Override
    public void onDoneRemarks() {
        doneFragment();
    }

    /**
     * Implemented from the RemarksFragment.OnFragmentInteractionListener interface.
     * Sends the question to the ECA
     *
     * @see RemarksFragment.OnFragmentInteractionListener#setRemarksQuestion()
     */
    @Override
    public void setRemarksQuestion() {
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragments[currentFragmentIndex]);

        if (currentFragment instanceof RemarksFragment) {
            int question = R.string.remarks_monitoring;

            ((RemarksFragment) currentFragment).setRemarkQuestion(question);
            ecaFragment.sendToECAToSPeak(question);

        }
    }
}
