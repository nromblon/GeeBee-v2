package com.geebeelicious.geebeelicious.fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.interfaces.MonitoringTestFragment;
import com.geebeelicious.geebeelicious.interfaces.OnMonitoringFragmentInteractionListener;
import com.geebeelicious.geebeelicious.models.grossmotor.GrossMotorSkill;
import com.geebeelicious.geebeelicious.models.grossmotor.GrossMotorTest;
import com.geebeelicious.geebeelicious.models.monitoring.Record;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kate.
 * The GrossMotorMainFragment class serves as the main fragment
 * for the gross motor test. It uses the GrossMotorTest class
 * to perform the test.
 */

public class GrossMotorFragment extends MonitoringTestFragment {

    /**
     * Used to identify the source of a log message
     */
    private static final String TAG = "GrossMotorFragment";

    /**
     * Used for interacting with the Activity this fragment is attached to.
     */
    private OnMonitoringFragmentInteractionListener fragmentInteraction;

    /**
     * Contains the activity this fragment is attached to.
     */
    private Activity activity;

    /**
     * Contains the gross motor test that the child will
     * take.
     */
    private GrossMotorTest grossMotorTest;

    /**
     * Used for timing each skill in the test.
     */
    private CountDownTimer countDownTimer;

    /**
     * Used as font for the different UI properties.
     */
    private Typeface chalkFont;

    /**
     * Clicked if the skill cannot be performed
     */
    private Button naButton;

    /**
     * Used for showing .gif pictures of skill
     */
    private WebView gifWebView;

    /**
     * Constructor.
     *
     * @see MonitoringTestFragment#intro
     */
    public GrossMotorFragment() {
        this.intro = R.string.grossmotor_intro;
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
        view = inflater.inflate(R.layout.fragment_gross_motor, container, false);
        chalkFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DJBChalkItUp.ttf");

        Button yesButton = (Button) view.findViewById(R.id.YesButton);
        Button noButton = (Button) view.findViewById(R.id.NoButton);
        naButton = (Button) view.findViewById(R.id.NAButton);

        gifWebView = (WebView) view.findViewById(R.id.gifWebView);

        yesButton.setTypeface(chalkFont);
        noButton.setTypeface(chalkFont);
        naButton.setTypeface(chalkFont);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set skill to pass
                grossMotorTest.getCurrentSkill().setSkillPassed();
                goToNextQuestion();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set skill to fail
                grossMotorTest.getCurrentSkill().setSkillFailed();
                goToNextQuestion();
            }
        });

        naButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNAButtonClick();
            }
        });


        grossMotorTest = new GrossMotorTest(activity);
        grossMotorTest.makeTest();
        startTest();

        return view;
    }

    /**
     * Called when NAButton is clicked. Stops the current skill and starts the next.
     */
    public void onNAButtonClick() {
        TextView timerView = (TextView) view.findViewById(R.id.countdownTV);
        timerView.setTypeface(chalkFont);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        grossMotorTest.getCurrentSkill().setSkillSkipped();
        goToNextQuestion();
        grossMotorTest.skipTest(timerView);
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Ends the grossmotor test.
     */
    public void onBackPressed() {
        grossMotorTest.endTest();
    }

    /**
     * Updates the end test attributes of the test fragment namely
     * {@link #isEndEmotionHappy}, {@link #endStringResource},
     * and {@link #endTime} depending on the result of the test.
     *
     * @param result test result of the patient. 0 if passed, 1 if fail, 2 if n/a.
     * @see MonitoringTestFragment
     */
    private void updateTestEndRemark(int result) {
        switch (result) {
            case 0:
                this.isEndEmotionHappy = true;
                this.endStringResource = R.string.grossmotor_pass;
                this.endTime = 3000;
                break;
            case 1:
                this.isEndEmotionHappy = false;
                this.endStringResource = R.string.grossmotor_fail;
                this.endTime = 5000;
                break;
            default:
                this.isEndEmotionHappy = true;
                this.endStringResource = R.string.grossmotor_na;
                this.endTime = 4000;
                break;
        }
    }

    /**
     * Starts the test.
     */
    private void startTest() {
        grossMotorTest.setCurrentSkill(0);
        displaySkill(0);
    }

    /**
     * Ends the test and sends result to the
     * activity this fragment is attached to.
     */
    private void endTest() {
        String resultString = grossMotorTest.getAllResults() + "\nOverall: " + grossMotorTest.getFinalResult();
        Record record = fragmentInteraction.getRecord();

        grossMotorTest.endTest();
        hideAnswerButtons();

        fragmentInteraction.setResults(resultString);

        record.setGrossMotor(fragmentInteraction.getIntResults(grossMotorTest.getFinalResult()));

        naButton.setVisibility(View.GONE);
        gifWebView.setVisibility(View.GONE);

        updateTestEndRemark(grossMotorTest.getIntFinalResult());
        fragmentInteraction.doneFragment();
    }

    /**
     * Displays the skill as determined by the GrossMotorTest on the screen
     *
     * @param i index to know how many skill test has been done.
     */
    private void displaySkill(final int i) {
        final GrossMotorSkill gms = grossMotorTest.getCurrentSkill();
        String durationString = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(gms.getDuration()));

        fragmentInteraction.setInstructions(gms.getInstruction() + " for " + durationString + " seconds.");
        String html = getHTMLData(getResources().getResourceEntryName(gms.getSkillResImage()));

        Log.d(TAG, "Loading html to webview: " + html);

        gifWebView.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
        gifWebView.setBackgroundColor(Color.TRANSPARENT);

        countDownTimer = new CountDownTimer(6000, 1000) {
            TextView timerView = (TextView) (view.findViewById(R.id.countdownTV));

            @Override
            public void onTick(long millisUntilFinished) {
                String timerString = "" + String.format("%01d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                timerView.setTypeface(chalkFont);
                timerView.setText(timerString);
            }

            @Override
            public void onFinish() {
                grossMotorTest.performSkill(i, timerView, (LinearLayout) view.findViewById(R.id.linearLayoutYesNo), naButton);
            }
        };
        hideAnswerButtons();
        countDownTimer.start();

    }

    /**
     * Get the html code used for the {@link #gifWebView}
     *
     * @param imageURL URL of the image to be shown in the {@link #gifWebView}
     * @return html code to show the image in the {@link #gifWebView}.
     */
    private String getHTMLData(String imageURL) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto; margin : auto;}" +
                "html, body { height: 100%; margin:0; padding:0;}\n" +
                "div { position:relative;height: 100%; width:100%; }\n" +
                "div img {position:absolute; top:0; left:0; right:0; bottom:0; margin:auto; }</style></head>";
        return "<html>" + head + "<body>" +
                "<div><img src=\"" + imageURL + "\"></div>" +
                "</body></html>";
    }

    /**
     * Allows the test to move to the next question or ends the test
     */
    private void goToNextQuestion() {
        grossMotorTest.getCurrentSkill().setTested();
        int currentSkill = grossMotorTest.getCurrentSkillNumber();
        if (currentSkill < 2) {
            currentSkill++;
            grossMotorTest.setCurrentSkill(currentSkill);
            displaySkill(currentSkill);
        } else if (currentSkill == 2) {
            endTest();
        }
    }

    /**
     * Hides the yes and no buttons
     */
    private void hideAnswerButtons() {
        LinearLayout answers = (LinearLayout) view.findViewById(R.id.linearLayoutYesNo);
        for (int j = 0; j < answers.getChildCount(); j++) {
            View view = answers.getChildAt(j);
            view.setEnabled(false);
            view.setVisibility(View.GONE);
        }
        answers.setVisibility(View.GONE);
        naButton.setVisibility(View.VISIBLE);
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
                    + " must implement OnMonitoringFragmentInteractionListener and OnFragmentInteractionListener");
        }
    }
}
