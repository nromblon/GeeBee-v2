package com.geebeelicious.geebeelicious.fragments;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.interfaces.MonitoringTestFragment;
import com.geebeelicious.geebeelicious.interfaces.OnMonitoringFragmentInteractionListener;
import com.geebeelicious.geebeelicious.models.monitoring.Record;
import com.geebeelicious.geebeelicious.models.visualacuity.ChartHelper;
import com.geebeelicious.geebeelicious.models.visualacuity.DistanceCalculator;
import com.geebeelicious.geebeelicious.models.visualacuity.VisualAcuityResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The VisualAcuityFragment serves as the fragment
 * for the visual acuity test. It uses the ChartHelper,
 * DistanceCalculator, and VisualAcuityResult classes
 * to perform the visual acuity test.
 *
 * @author Katrina Lacsamana
 */

public class VisualAcuityFragment extends MonitoringTestFragment {

    /**
     * Used for interacting with the Activity this fragment is attached to.
     */
    private OnMonitoringFragmentInteractionListener fragmentInteraction;

    /**
     * Used for updating the records of the patient.
     */
    private Record record;

    /**
     * where the eye chart is shown
     */
    private ImageView chartView;

    /**
     * Constructor.
     *
     * @see MonitoringTestFragment#intro
     * @see MonitoringTestFragment#hasEarlyInstruction
     */
    public VisualAcuityFragment() {
        this.intro = R.string.visualAcuity_intro;
        this.hasEarlyInstruction = true;
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
        view = inflater.inflate(R.layout.fragment_visual_acuity, container, false);

        chartView = (ImageView) view.findViewById(R.id.chartLine);
        final ChartHelper chartHelper = new ChartHelper(chartView, getChartPreference());
        Button yesButton = (Button) view.findViewById(R.id.YesButton);
        Button noButton = (Button) view.findViewById(R.id.NoButton);
        Typeface chalkFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DJBChalkItUp.ttf");
        yesButton.setTypeface(chalkFont);
        noButton.setTypeface(chalkFont);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartHelper.goToNextLine();
                if (chartHelper.isDone() && !chartHelper.isBothTested()) {
                    updateResults(chartHelper);
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartHelper.setResult();
                if (chartHelper.isDone() && !chartHelper.isBothTested()) {
                    updateResults(chartHelper);
                }
            }
        });
        chartHelper.startTest();

        fragmentInteraction.showTransitionTextLayout();

        return view;
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

    /**
     * Overrides method. Initializes the distance calculator and
     * sends the resulting instruction to the activity
     * this fragment is attached to.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //distance calculator
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        float distance = distanceCalculator.getUserDistance(getActivity(), chartView);
        String instructions = "Move " + String.format("%.2f", distance) +
                " meters away from the tablet. " + getString(R.string.visualAcuity_instruction_left) + " Then tell me what you see.";
        record = fragmentInteraction.getRecord();
        fragmentInteraction.appendTransitionIntructions(instructions);
    }

    /**
     * Gets the result from the {@code chartHelper} and sends it to the
     * activity that this fragment is attached to.
     *
     * @param chartHelper
     */
    private void updateResults(ChartHelper chartHelper) {
        VisualAcuityResult rightEyeResult = null;
        VisualAcuityResult leftEyeResult = null;

        if (!chartHelper.isRightTested() && rightEyeResult == null) {
            rightEyeResult = new VisualAcuityResult("Right", chartHelper.getResult());
            chartHelper.setIsRightTested();
            chartHelper.startTest();
            displayResults(rightEyeResult);
            record.setVisualAcuityRight(rightEyeResult.getVisualAcuity());

            fragmentInteraction.setInstructions(R.string.visualAcuity_instruction_right);
        } else if (!chartHelper.isLeftTested() && leftEyeResult == null) {
            leftEyeResult = new VisualAcuityResult("Left", chartHelper.getResult());
            chartHelper.setIsLeftTested();
            displayResults(leftEyeResult);
            record.setVisualAcuityLeft(leftEyeResult.getVisualAcuity());

            updateTestEndRemark(leftEyeResult.getLineNumber(), rightEyeResult.getLineNumber());

            fragmentInteraction.doneFragment();
        }
    }

    /**
     * Displays the results to the user.
     *
     * @param result visual acuity test result.
     */
    private void displayResults(VisualAcuityResult result) {
        String resultString = "";
        resultString += (result.getEye().toUpperCase() + "\nLine Number: " +
                result.getLineNumber() + "\nVisual Acuity: " +
                result.getVisualAcuity());
        fragmentInteraction.setResults(resultString);
    }

    /**
     * Gets the chart preference from the device storage.
     *
     * @return the chartPreference. If IOException is thrown, returns 1 as the
     * default chartPreference.
     */
    private int getChartPreference() {
        int chartPreference = 0; //default is 0 for Snellen chart
        byte[] byteArray = new byte[4];
        try {
            FileInputStream fis = getActivity().getApplicationContext().openFileInput("VisualAcuityChartPreferences");
            fis.read(byteArray, 0, 4);
            fis.close();
        } catch (IOException e) {
            return 1;
        }

        ByteBuffer b = ByteBuffer.wrap(byteArray);
        chartPreference = b.getInt();

        return chartPreference;
    }

    /**
     * Updates the end test attributes of the test fragment namely
     * {@link #isEndEmotionHappy}, {@link #endStringResource},
     * and {@link #endTime} depending on the result of the test.
     *
     * @param lineNumberLeft  line number in the eye chart that the user is able to read correctly last using
     *                        just her left eye.
     * @param lineNumberRight line number in the eye chart that the user is able to read correctly last using
     *                        just her right eye.
     * @see MonitoringTestFragment
     */
    private void updateTestEndRemark(String lineNumberLeft, String lineNumberRight) {
        int lineNumLeft = Integer.parseInt(lineNumberLeft);
        int lineNumRight = Integer.parseInt(lineNumberRight);

        if (lineNumLeft < 8 || lineNumRight < 8) {
            this.isEndEmotionHappy = false;
            this.endStringResource = R.string.visual_acuity_fail;
            this.endTime = 6000;
        } else {
            this.isEndEmotionHappy = true;
            this.endStringResource = R.string.visual_acuity_pass;
            this.endTime = 3000;
        }
    }
}
