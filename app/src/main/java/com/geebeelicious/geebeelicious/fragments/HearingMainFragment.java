package com.geebeelicious.geebeelicious.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.interfaces.MonitoringTestFragment;
import com.geebeelicious.geebeelicious.interfaces.OnMonitoringFragmentInteractionListener;
import com.geebeelicious.geebeelicious.models.hearing.HearingTest;
import com.geebeelicious.geebeelicious.models.monitoring.Record;

import java.util.ArrayList;

/**
 * The HearingMainFragment serves as the fragment
 * for the hearing test. It uses the HearingTest class
 * to perform the hearing test.
 *
 * @author Katrina Lacsamana
 */

public class HearingMainFragment extends MonitoringTestFragment {

    /**
     * Used for interacting with the Activity this fragment is attached to.
     */
    private OnMonitoringFragmentInteractionListener fragmentInteraction;

    /**
     * Contains the different threads used in the test namely
     * screenThread, timingThread, and testThread.
     */
    private ArrayList<Thread> threads;

    /**
     * Contains the instance of {@link HearingTest} that helps
     * manage the audiometry hearing test.
     */
    private HearingTest hearingTest;

    /**
     * Contains the activity this fragment is attached to
     */
    private Activity activity;

    /**
     * Constructor.
     *
     * @see MonitoringTestFragment#intro
     */
    public HearingMainFragment() {
        this.intro = R.string.hearing_intro;
    }

    /**
     * Initializes views and other fragment objects.
     *
     * @see android.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hearing_main, container, false);

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        hearingTest = new HearingTest();
        final double[] calibrationData = hearingTest.getCalibrationData(activity);

        Typeface chalkFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DJBChalkItUp.ttf");
        final Button yesButton = (Button) view.findViewById(R.id.YesButton);
        yesButton.setTypeface(chalkFont);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hearingTest.isGap()) { //if not pressed during gap
                    if (hearingTest.hasCheated() <= 3) { //if <=3 cheating attempt (random presses) made, consider as correct answer
                        System.out.println("setHeard");
                        hearingTest.setHeard();
                    } //if more than 3 cheat attempts, answer is no longer considered for that round (for z loop in hearing test)
                } else if (hearingTest.isGap()) { //if pressed during gap, consider as cheating attempt (max 3 allowed)
                    hearingTest.setCheated();
                }
            }
        });

        final Runnable disableTest = new Runnable() {
            @Override
            public void run() {
                yesButton.setVisibility(View.GONE);
                yesButton.setEnabled(false);

                fragmentInteraction.setResults(hearingTest.getResults());
            }
        };

        final Thread screenThread = new Thread(new Runnable() {
            public void run() {
                while (hearingTest.isInLoop()) {
                    if (!hearingTest.isRunning()) {
                        return;
                    }
                }
            }
        });

        final Thread timingThread = new Thread(new Runnable() {
            public void run() {
                while (hearingTest.isInLoop()) {
                    if (!hearingTest.isRunning()) {
                        return;
                    }
                    if (hearingTest.isHeard()) {

                    }
                }
            }
        });

        final Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                hearingTest.performTest(calibrationData);
                if (hearingTest.isDone()) {
                    activity.runOnUiThread(disableTest);
                    endTest();

                    callnextFragment();
                }
            }
        });

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 9, 0);
        threads = new ArrayList<Thread>();
        threads.add(screenThread);
        threads.add(timingThread);
        threads.add(testThread);

        CountDownTimer countDownTimer = new CountDownTimer(4000, 4000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                screenThread.start();
                timingThread.start();
                testThread.start();
            }
        };
        countDownTimer.start();

        return view;
    }

    /**
     * Updates the end test attributes of the test fragment namely
     * {@link FineMotorFragment#isEndEmotionHappy}, {@link FineMotorFragment#endStringResource},
     * and {@link FineMotorFragment#endTime} depending on the result of the test, and ends the test.
     *
     * @see MonitoringTestFragment
     */
    private void callnextFragment() {
        Record record = fragmentInteraction.getRecord();

        if (record.getHearingRight().equals("Normal Hearing") && record.getHearingLeft().equals("Normal Hearing")) {
            this.isEndEmotionHappy = true;
            this.endStringResource = R.string.hearing_pass;
            this.endTime = 3000;
        } else {
            this.isEndEmotionHappy = false;
            this.endStringResource = R.string.hearing_fail;
            this.endTime = 6000;
        }
        fragmentInteraction.doneFragment();
    }

    /**
     * Sends results to the activity this fragment is attached to.
     */
    private void endTest() {
        Record record = fragmentInteraction.getRecord();
        record.setHearingRight(hearingTest.getPureToneAverageInterpretation("Right"));
        record.setHearingLeft(hearingTest.getPureToneAverageInterpretation("Left"));
        stopTest();
    }

    /**
     * Stops the tests. Interrupts all the threads inside the
     * {@link #threads}.
     */
    private void stopTest() {
        hearingTest.setIsNotRunning();
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).interrupt();
        }
    }

    /**
     * Called by the activity to skip the hearing test.
     * Sends dummy result to the activity this fragment is attached to.
     */
    public void endTestShortCut() { //For testing purposes only
        Record record = fragmentInteraction.getRecord();
        record.setHearingRight("Mild Hearing Loss");
        record.setHearingLeft("Moderately-Severe Hearing Loss");

        stopTest();
        callnextFragment();
    }


    /**
     * Overrides method. Makes sure that the container activity
     * has implemented the callback interface {@link OnMonitoringFragmentInteractionListener}.
     * If not, it throws an exception.
     *
     * @param activity Activity this fragment is attached to.
     * @throws ClassCastException if the container activity has not implemented
     *                            the callback interface {@link OnMonitoringFragmentInteractionListener}.
     * @see android.support.v4.app.Fragment#onAttach(Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            fragmentInteraction = (OnMonitoringFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMonitoringFragmentInteractionListener");
        }
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Stops the hearing test.
     */
    public void onBackPressed() {
        stopTest();
    }
}
