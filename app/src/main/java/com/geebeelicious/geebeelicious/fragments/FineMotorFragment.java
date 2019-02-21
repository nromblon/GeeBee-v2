package com.geebeelicious.geebeelicious.fragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.interfaces.MonitoringTestFragment;
import com.geebeelicious.geebeelicious.interfaces.OnMonitoringFragmentInteractionListener;
import com.geebeelicious.geebeelicious.models.finemotor.FineMotorHelper;
import com.geebeelicious.geebeelicious.models.monitoring.Record;

/**
 * The FineMotorFragment class serves as the main fragment
 * for the fine motor test. It uses the FineMotorHelper class
 * to perform the test.
 *
 * @author Mary Grace Malana
 */

public class FineMotorFragment extends MonitoringTestFragment {

    /**
     * Used for interacting with the Activity this fragment is attached to.
     */
    private OnMonitoringFragmentInteractionListener fragmentInteraction;

    /**
     * Used to identify the source of a log message
     */
    private static final String TAG = "FineMotorActivity";

    /**
     * Container for the path image used for the test.
     */
    private ImageView imageViewPathToTrace;

    //Set the color for the start and end of the path

    /**
     * Color of the starting point of the path.
     * If path image color is updated, this must also
     * be updated.
     */
    private final int START_COLOR = Color.parseColor("#09BCD4");

    /**
     * Color of the end point of the path.
     * If path image color is updated, this must also
     * be updated.
     */
    private final int END_COLOR = Color.parseColor("#E71E63");

    /**
     * Keeps track of which test the user is currently taking.
     */
    private int currentTest; //0 for non dominant, dominant, ask assistance

    /**
     * Serves as the flag whether the test is on going or not.
     */
    private boolean isTestOngoing = true;

    /**
     * Serves as the flag whether the test has started or not
     */
    private boolean hasStarted = false;

    /**
     * Contains the helper class of the activity
     */
    private FineMotorHelper fineMotorHelper;

    /**
     * Constructor.
     *
     * @see MonitoringTestFragment#intro
     */
    public FineMotorFragment() {
        this.intro = R.string.finemotor_intro;
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
        view = inflater.inflate(R.layout.fragment_fine_motor, container, false);

        imageViewPathToTrace = (ImageView) view.findViewById(R.id.imageViewPathToTrace);

        currentTest = 0;
        fineMotorHelper = new FineMotorHelper(getActivity(), imageViewPathToTrace);
        fragmentInteraction.setInstructions(fineMotorHelper.setInstructions(0));

        imageViewPathToTrace.setOnTouchListener(image_Listener);
        initializeButtons();

        return view;
    }

    /**
     * Handle onTouch events for tracing the path during the test.
     */
    public View.OnTouchListener image_Listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Bitmap bitmap = ((BitmapDrawable) imageViewPathToTrace.getDrawable()).getBitmap();
            float eventX = event.getX();
            float eventY = event.getY();
            int[] xY = fineMotorHelper.getBitMapCoordinates(bitmap, event.getX(), event.getY());
            int pixel = bitmap.getPixel(xY[0], xY[1]);

            if (hasStarted) { //if user have pressed start_color
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP: //go back to start if finger is lifted
                        hasStarted = false;
                        fragmentInteraction.setInstructions(fineMotorHelper.doIfTouchIsUp());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (pixel == END_COLOR) { //if user done
                            hasStarted = false;

                            if (currentTest == 0 || currentTest == 1) {
                                fragmentInteraction.setInstructions(fineMotorHelper.doNextTest(currentTest++));
                            }
                            if (currentTest == 2) {
                                showAnswerButtons();
                            }

                        } else if (pixel == 0) { //if touch is outside path
                            fineMotorHelper.doIfOutSideThePath();
                        } else {
                            fineMotorHelper.doIfWithinPath();
                        }
                        break;
                    default:
                        return false;
                }
                Log.d(TAG, "Touch event position: " + eventX + ", " + eventY + "\n" +
                        "Pixel: " + pixel);
                return true;
            } else {
                if (pixel == START_COLOR && (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)) {
                    hasStarted = true;
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                return false;
            }
        }
    };

    /**
     * Defines button attributes (Typeface and OnClickListener).
     */
    private void initializeButtons() {
        Button buttonYes = (Button) view.findViewById(R.id.YesButton);
        Button buttonNo = (Button) view.findViewById(R.id.NoButton);

        Typeface chalkFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DJBChalkItUp.ttf");
        buttonYes.setTypeface(chalkFont);
        buttonNo.setTypeface(chalkFont);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineMotorHelper.setResult(2, true);
                sendResults();
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineMotorHelper.setResult(2, false);
                sendResults();
            }
        });
    }

    /**
     * Sends results to the activity this fragment is attached to.
     */
    private void sendResults() {
        String resultString;
        boolean[] result = fineMotorHelper.getResults();

        if (isTestOngoing) { //this is to avoid double clicking
            isTestOngoing = false;
            Record record = fragmentInteraction.getRecord();

            resultString = "Non dominant hand: " + result[0] +
                    "\nDominant hand: " + result[1] +
                    "\nUsing pen: " + result[2];

            record.setFineMotorNDominant(result[0] ? 0 : 1);
            record.setFineMotorDominant(result[1] ? 0 : 1);
            record.setFineMotorHold(result[2] ? 0 : 1);
            fragmentInteraction.setResults(resultString);
            imageViewPathToTrace.setBackgroundColor(Color.WHITE);
        }

        updateTestEndRemark(fineMotorHelper.getResults());
        fragmentInteraction.doneFragment();
    }

    /**
     * Updates the end test attributes of the test fragment namely
     * {@link #isEndEmotionHappy}, {@link #endStringResource},
     * and {@link #endTime} depending on the result of the test.
     *
     * @param results list of results from the different rounds of the finemotor test.
     * @see MonitoringTestFragment
     */
    private void updateTestEndRemark(boolean[] results) {
        int numPass = 0;

        for (boolean result : results) {
            if (result) {
                numPass++;
            }
        }

        if (numPass < 2) {
            this.isEndEmotionHappy = false;
            this.endStringResource = R.string.finemotor_fail;
            this.endTime = 4000;
        } else {
            this.isEndEmotionHappy = true;
            this.endStringResource = R.string.finemotor_pass;
            this.endTime = 3000;
        }
    }

    /**
     * Shows answer buttons for the assistant to see.
     */
    private void showAnswerButtons() {
        LinearLayout answers = (LinearLayout) view.findViewById(R.id.linearLayoutYesNo);
        for (int j = 0; j < answers.getChildCount(); j++) {
            View view = answers.getChildAt(j);
            view.setEnabled(true);
            view.setVisibility(View.VISIBLE);
        }
        answers.setVisibility(View.VISIBLE);

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
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            fragmentInteraction = (OnMonitoringFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMonitoringFragmentInteractionListener");
        }
    }
}
