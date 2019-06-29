package com.geebeelicious.geebeelicious.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.fragments.ECAFragment;
import com.geebeelicious.geebeelicious.interfaces.ECAActivity;
import com.geebeelicious.geebeelicious.models.consultation.ConsultationHelper;
import com.geebeelicious.geebeelicious.models.consultation.Patient;
import com.geebeelicious.geebeelicious.models.consultation.Question;

/**
 * The ConsultationActivity class is the main activity for consultation.
 * This covers consultation with patients and the succeeding generation of HPI.
 * It allows the user to view questions and answer with Yes or No inputs.
 *
 * @author Mary Grace Malana
 */

public class ConsultationActivity extends ECAActivity {
    /**
     * Used to identify the source of a log message
     */
    private final static String TAG = "ConsultationActivity";

    /**
     * TextView to show the consultation question
     */
    private TextView ECAText;

    /**
     * Contains the helper class of the activity
     */
    private ConsultationHelper consultationHelper;

    /**
     * Serves as the flag whether the consultation is on going or not.
     */
    private boolean isOnGoingFlag;

    /**
     * Contains the information of current patient having the consultation
     */
    private Patient patient;

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
        setContentView(R.layout.activity_consultation);

        Button yesButton = (Button)findViewById(R.id.YesButton);
        Button noButton = (Button) findViewById(R.id.NoButton);

        chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
        yesButton.setTypeface(chalkFont);
        noButton.setTypeface(chalkFont);

        patient = getIntent().getExtras().getParcelable("patient");
        String dateConsultation = getIntent().getStringExtra("currentDate");

        isOnGoingFlag = true;
        consultationHelper = new ConsultationHelper(this, patient, dateConsultation);
        ECAText = (TextView) findViewById(R.id.placeholderECAText);
        ECAText.setTypeface(chalkFont);

        integrateECA();

        setQuestion(consultationHelper.getFirstQuestion());

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!consultationHelper.isConsultationDone()) { //checks if consultation is still ongoing
                    onAnswer(true); //true because yes
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!consultationHelper.isConsultationDone()) { //checks if consultation is still ongoing
                    onAnswer(false);  //false because no
                }
            }
        });
    }

    /**
     * Determines the course of action depending on user input (Yes or No)
     *
     * @param isYes user’s answer to the consultation question. If true Yes, else No
     */
    private void onAnswer(boolean isYes) {
        Question nextQuestion = consultationHelper.getNextQuestion(isYes);
        if (nextQuestion == null) {
            doWhenConsultationDone();
        } else {
            setQuestion(nextQuestion);
        }
    }

    /**
     * Performed when consultation is done; Saves generated HPI to database if patient has complaints
     */
    private synchronized void doWhenConsultationDone() {
        if (isOnGoingFlag) {
            isOnGoingFlag = false;

            String hpi = consultationHelper.getHPI();
            TextView hpiTitle = (TextView) findViewById(R.id.hpiTitle);
            TextView hpiTextView = (TextView) findViewById(R.id.hpiPlaceholder);
            hpiTitle.setTypeface(chalkFont);
            hpiTextView.setTypeface(chalkFont);

            Log.d(TAG, "HPI: " + hpi);
            consultationHelper.saveToDatabase(hpi); //closes the database after saving the hpi
            hpiTextView.setText(hpi);

            doneConsultation();
        }
    }

    /**
     * Shows the hpi to user; Ends consultation activity.
     */
    private void doneConsultation() {
//        CountDownTimer timer;
        LinearLayout hpiLayout = (LinearLayout) findViewById(R.id.hpiLayout);
        RelativeLayout choicesLayout = (RelativeLayout) findViewById(R.id.choicesLayout);

//        timer = new CountDownTimer(15000, 15000) { //timer for the transition
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                finish();
//            }
//        };

        hpiLayout.setVisibility(View.VISIBLE);
        choicesLayout.setVisibility(View.GONE);

        ecaFragment.sendToECAToSPeak(R.string.consultation_end);
//        timer.start();
    }

    /**
     * Sets the question shown to the user; Sends the question and emotion to ecaFragment.
     *
     * @param question Next question to be asked to the user
     */
    private void setQuestion(Question question) {
        String questionString = question.getQuestionstring();
        int emotion = question.getEmotion();

        ECAText.setText(questionString);
        ecaFragment.sendToECAToSpeak(questionString);

        if (emotion < 4) {
            ecaFragment.sendToECAToEmote(ECAFragment.Emotion.HAPPY, emotion - 1);
        } else if (emotion < 7) {
            ecaFragment.sendToECAToEmote(ECAFragment.Emotion.CONCERN, emotion - 4);
        }

    }
}
