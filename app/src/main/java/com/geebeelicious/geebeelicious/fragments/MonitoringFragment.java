package com.geebeelicious.geebeelicious.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Interpolator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.activities.MonitoringMainActivity;
import com.geebeelicious.geebeelicious.interfaces.MonitoringTestFragment;
import com.geebeelicious.geebeelicious.interfaces.OnMonitoringFragmentInteractionListener;
import com.geebeelicious.geebeelicious.models.bmi.BMICalculator;
import com.geebeelicious.geebeelicious.models.monitoring.Record;

import java.util.Random;

/**
 * The MonitoringFragment class serves as the first fragment
 * that users will encounter when they choose to perform a
 * monitoring activity. The fragment will allow users to
 * input updates for certain health determinants such as
 * height and weight.
 * <p>
 * Default height and weight were chosen using the data from the World Health Organization.
 *
 * @author Katrina Lacsamana
 */

public class MonitoringFragment extends MonitoringTestFragment {

    /**
     * Used to identify the source of a log message
     */
    private final static String TAG = "MonitoringFragment";

    /**
     * Used to show the monitoring question
     */
    private TextView questionView;

    /**
     * Used for getting the weight and height of the patient.
     */
    private NumberPicker numberPicker;

    /**
     * Used to show the unit of measurement to be entered.
     */
    private TextView unitView;

    /**
     * List of string resource of the questions to be shown in {@link #questionView}
     */
    private final int[] questions = {R.string.monitoring_height, R.string.monitoring_weight};

    /**
     * List of string resource of the unit of measurement to be shown in {@link #unitView}
     */
    private final int[] questionUnit = {R.string.centimeters, R.string.kilograms};

    /**
     * List of default values for the {@link #numberPicker} for each monitoring question.
     */
    private final int[] defaultValues = {132, 28};

    /**
     * Number of questions to be asked to the user.
     */
    private final int numberOfQuestions = 2;

    /**
     * Serves as the index counter for the question asked to the user
     */
    private int questionsCounter = 0;

    /**
     * Used for updating the records of the patient.
     */
    private Record record;

    /**
     * Used for interacting with the Activity this fragment is attached to.
     */
    private OnMonitoringFragmentInteractionListener fragmentInteraction;

    /**
     * Initializes views and other fragment objects.
     *
     * @see android.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_monitoring, container, false);

        questionView = (TextView) view.findViewById(R.id.questionView);
        unitView = (TextView) view.findViewById(R.id.unitView);
        numberPicker = (NumberPicker) view.findViewById(R.id.monitoringNumberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(250);

        setQuestion(questions[questionsCounter]);
        numberPicker.setValue(defaultValues[questionsCounter]);

        Typeface chalkFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DJBChalkItUp.ttf");
        questionView.setTypeface(chalkFont);
        unitView.setTypeface(chalkFont);

        unitView.setText(questionUnit[questionsCounter]);

        Button saveButton = (Button) view.findViewById(R.id.saveAnswerButton);
        saveButton.setTypeface(chalkFont);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("numberPicker: " + numberPicker.getValue() + " " + new Integer(numberPicker.getValue()).doubleValue());
                switch (questionsCounter) {
                    case 0:
                        record.setHeight(new Integer(numberPicker.getValue()).doubleValue());
                        numberPicker.setMaxValue(100);
                        break;
                    case 1:
                        record.setWeight(new Integer(numberPicker.getValue()).doubleValue());
                        break;
                }
                questionsCounter++;
                if (questionsCounter < numberOfQuestions) {
                    setQuestion(questions[questionsCounter]);
                    numberPicker.setValue(defaultValues[questionsCounter]);
                    unitView.setText(questionUnit[questionsCounter]);
                } else {
                    endMonitoring();
                }
            }
        });

        Button useAppButton = (Button) view.findViewById(R.id.useIPAppButton);
        useAppButton.setTypeface(chalkFont);
        useAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setComponent(new ComponentName("ph.edu.dlsu.reanna_lim.imageprocessingmodule","ph.edu.dlsu.reanna_lim.imageprocessingmodule.MainActivityTablet"));
                intent.setComponent(new ComponentName("com.geebeelicious.capture","com.geebeelicious.capture.CaptureActivityTablet"));
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                String str_height = data.getExtras().getString("height");
                String str_weight = data.getExtras().getString("weight");
                byte[] silhouette = data.getExtras().getByteArray("silhouette");

                record.setHeight(Double.parseDouble(str_height));
                record.setWeight(Double.parseDouble(str_weight));
                record.setSilhouette(silhouette);

                endMonitoring();
            }
            if(resultCode == Activity.RESULT_CANCELED) {
                super.onResume();
            }
        }
    }

    /**
     * Sets the question shown to the user and sent to the activity
     * this fragment is attached to.
     *
     * @param resID string resource of the question.
     * @see OnMonitoringFragmentInteractionListener#setInstructions(int)
     */
    private void setQuestion(int resID) {
        questionView.setText(resID);
        fragmentInteraction.setInstructions(resID);
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
     * Overrides method. Gets a reference of the record of
     * the patient from the Activity this fragment
     * is attached to.
     *
     * @see android.app.Fragment#onActivityCreated(Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        record = fragmentInteraction.getRecord();
    }

    /**
     * Ends the monitoring session.
     */
    private void endMonitoring() {
        questionsCounter = 0;
        updateTestEndRemark();
        fragmentInteraction.doneFragment();
    }

    /**
     * Updates the end test attributes of the test fragment namely
     * {@link #isEndEmotionHappy}, {@link #endStringResource},
     * and {@link #endTime} depending on the BMI result, and ends the test.
     *
     * @see MonitoringTestFragment
     */
    private void updateTestEndRemark() {
        boolean isGirl = fragmentInteraction.isGirl();
        int age = fragmentInteraction.getAge();
        float bmi = BMICalculator.computeBMIMetric((int) record.getHeight(), (int) record.getWeight());
        int bmiResult = BMICalculator.getBMIResult(isGirl, age, bmi);
        Random randomizer = new Random();
        int randomNum = randomizer.nextInt(3) + 1;
        String resourseString;

        switch (bmiResult) {
            case 0:
                this.isEndEmotionHappy = false;
                resourseString = "monitoring_remark_below";
                break;
            case 1:
                this.isEndEmotionHappy = true;
                resourseString = "monitoring_remark_normal";
                break;
            default:
                this.isEndEmotionHappy = false;
                resourseString = "monitoring_remark_above";
                break;
        }

        Log.d(TAG, "Patient BMI: " + bmiResult);

        resourseString += randomNum;
        this.endStringResource = getResources().getIdentifier(resourseString, "string", getActivity().getPackageName());
        this.endTime = 3000;
    }
}
