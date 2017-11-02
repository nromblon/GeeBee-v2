package com.geebeelicious.geebeelicious.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.interfaces.MonitoringTestFragment;
import com.geebeelicious.geebeelicious.interfaces.OnMonitoringFragmentInteractionListener;
import com.geebeelicious.geebeelicious.models.colorvision.IshiharaHelper;
import com.geebeelicious.geebeelicious.models.monitoring.Record;

/**
 * The ColorVisionMainFragment serves as the main fragment for
 * the color vision test. The activity utilizes the
 * IshiharaHelper class to perform the test.
 *
 * @author Katrina Lacsamana
 */
public class ColorVisionFragment extends MonitoringTestFragment {

    /**
     * Used for interacting with the Activity this fragment is attached to.
     */
    private OnMonitoringFragmentInteractionListener fragmentInteraction;

    /**
     * Serves as the flag whether the test is on going or not.
     */
    private boolean isTestOngoing;

    /**
     * Constructor.
     *
     * @see MonitoringTestFragment#intro
     */
    public ColorVisionFragment() {
        this.intro = R.string.colorVision_intro;
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
        view = inflater.inflate(R.layout.fragment_color_vision, container, false);

        ImageButton option1 = (ImageButton) view.findViewById(R.id.cvt_option1);
        ImageButton option2 = (ImageButton) view.findViewById(R.id.cvt_option2);
        ImageButton option3 = (ImageButton) view.findViewById(R.id.cvt_option3);
        ImageButton option4 = (ImageButton) view.findViewById(R.id.cvt_option4);
        ImageButton option5 = (ImageButton) view.findViewById(R.id.cvt_option5);
        final ImageButton[] buttonList = {option1, option2, option3, option4, option5};
        final IshiharaHelper ishiharaHelper = new IshiharaHelper((ImageView) view.findViewById(R.id.ishiharaPlate), buttonList);

        isTestOngoing = true;

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishiharaHelper.answerQuestion(0);
                updateResults(ishiharaHelper);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishiharaHelper.answerQuestion(1);
                updateResults(ishiharaHelper);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishiharaHelper.answerQuestion(2);
                updateResults(ishiharaHelper);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishiharaHelper.answerQuestion(3);
                updateResults(ishiharaHelper);
            }
        });

        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishiharaHelper.answerQuestion(4);
                updateResults(ishiharaHelper);
            }
        });

        ishiharaHelper.startTest();

        return view;
    }

    /**
     * Allows test to either go to the next question and save results if the test is done.
     *
     * @param ishiharaHelper helper class of the fragment.
     */
    private synchronized void updateResults(IshiharaHelper ishiharaHelper) {
        ishiharaHelper.goToNextQuestion();
        if (ishiharaHelper.isDone() && isTestOngoing) {
            isTestOngoing = false;
            Record record = fragmentInteraction.getRecord();
            record.setColorVision(ishiharaHelper.getResult());
            displayResults(ishiharaHelper.getScore());

            updateTestEndRemark(ishiharaHelper.isNormal());
            fragmentInteraction.doneFragment();
        }
    }

    /**
     * Updates the end test attributes of the test fragment namely
     * {@link #isEndEmotionHappy}, {@link #endStringResource},
     * and {@link #endTime}  on the result of the test.
     *
     * @param normal test result of the patient. true if normal, false if not normal.
     * @see MonitoringTestFragment
     */
    private void updateTestEndRemark(boolean normal) {
        if (normal) {
            this.isEndEmotionHappy = true;
            this.endStringResource = R.string.color_vision_pass;
            this.endTime = 3000;
        } else {
            this.isEndEmotionHappy = false;
            this.endStringResource = R.string.color_vision_fail;
            this.endTime = 7000;
        }
    }

    /**
     * Sends the test results to the activity that this fragment is attached to.
     *
     * @param score patient's final test score.
     */
    private void displayResults(int score) {
        String resultString = "SCORE: " + score;

        if (score >= 10) {
            resultString += "\nYou have NORMAL color vision.";
        } else {
            resultString += "\nYou scored lower than normal.";
        }
        fragmentInteraction.setResults(resultString);

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

        try {
            fragmentInteraction = (OnMonitoringFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMonitoringFragmentInteractionListener");
        }
    }

}
