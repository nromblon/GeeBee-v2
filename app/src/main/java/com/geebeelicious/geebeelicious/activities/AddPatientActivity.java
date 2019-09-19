package com.geebeelicious.geebeelicious.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.database.DatabaseAdapter;
import com.geebeelicious.geebeelicious.fragments.RemarksFragment;
import com.geebeelicious.geebeelicious.interfaces.ECAActivity;
import com.geebeelicious.geebeelicious.models.consultation.Patient;

import java.sql.SQLException;
import java.util.Date;

/**
 * The AddPatientActivity serves as the activity containing
 * functionality for adding new patients.
 *
 * @author Katrina Lacsamana
 */

public class AddPatientActivity extends ECAActivity implements RemarksFragment.OnFragmentInteractionListener {
    /**
     * First name of the patient
     */
    private String firstName = null;

    /**
     * Last name of the patient
     */
    private String lastName = null;

    /**
     * Birthdate of the patient
     */
    private String birthDate = null;

    /**
     * Gender of the patient. Gender = 0, else Gender = 1
     */
    private int gender;

    /**
     * Handedness of the patient.
     */
    private int handedness;

    /**
     * Patient that will be created and added to the database
     */
    private Patient patient = null;

    /**
     * Textview for the questions that is shown to the user
     */
    private TextView questionView;

    /**
     * Where the user enters {@link AddPatientActivity#firstName} and {@link AddPatientActivity#lastName}
     */
    private EditText editText;

    /**
     * Where the user enters {@link AddPatientActivity#birthDate}
     */
    private DatePicker datePicker;

    /**
     * Serves as the index counter for questions
     */
    private int questionCounter;

    /**
     * contains string resource of questions shown in {@link AddPatientActivity#questionView}
     */
    private final int[] questions = {R.string.first_name, R.string.last_name, R.string.birthdate,
            R.string.gender, R.string.handedness};

    /**
     * contains the fragment that asks for remarks from the assistant
     */
    RemarksFragment remarksFragment = null;

    /**
     * Initializes views and other activity objects.
     *
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_activity_add_patient);

        questionView = (TextView) findViewById(R.id.questionView);
        editText = (EditText) findViewById(R.id.newPatientStringInput);
        datePicker = (DatePicker) findViewById(R.id.newPatientDatePicker);
        datePicker.setMaxDate(new Date().getTime());
        final RadioButton radioButton0 = (RadioButton) findViewById(R.id.radioButton1);
        final RadioButton radioButton1 = (RadioButton) findViewById(R.id.radioButton2);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.newPatientRadioChoice);

        integrateECA();
        Typeface chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");
        questionView.setTypeface(chalkFont);
        radioButton0.setTypeface(chalkFont);
        radioButton1.setTypeface(chalkFont);
        editText.setTypeface(chalkFont);

        if (savedInstanceState != null) {
            questionCounter = savedInstanceState.getInt("questionCounter");
        } else {
            questionCounter = 0;
        }

        setQuestion(questions[questionCounter]);
        editText.setVisibility(View.VISIBLE);

        Button cancelButton = (Button) findViewById(R.id.cancelNewPatientButton);
        cancelButton.setTypeface(chalkFont);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient = null;
                questionCounter = 0;
                Intent intent = new Intent(AddPatientActivity.this, PatientListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveNewPatientButton);
        saveButton.setTypeface(chalkFont);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (questionCounter) {
                    case 0:
                        firstName = getEditText();
                        editText.setText("");
                        break;
                    case 1:
                        lastName = getEditText();
                        editText.setVisibility(View.GONE);
                        datePicker.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        birthDate = getSelectedDate();
                        datePicker.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.VISIBLE);
                        radioButton0.setText(R.string.male);
                        radioButton1.setText(R.string.female);
                        break;
                    case 3:
                        int selectedID = radioGroup.getCheckedRadioButtonId();
                        if (selectedID == radioButton0.getId()) {
                            gender = 0;
                        } else if (selectedID == radioButton1.getId()) {
                            gender = 1;
                        }
                        radioGroup.check(R.id.radioButton1);
                        radioButton0.setText(R.string.right_handed);
                        radioButton1.setText(R.string.left_handed);
                        radioButton0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handedness = 0;
                            }
                        });
                        radioButton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handedness = 1;
                            }
                        });
                        break;
                    case 4:
                        selectedID = radioGroup.getCheckedRadioButtonId();
                        if (selectedID == radioButton0.getId()) {
                            handedness = 0;
                        } else if (selectedID == radioButton1.getId()) {
                            handedness = 1;
                        }
                        radioGroup.setVisibility(View.GONE);
                        patient = new Patient(firstName, lastName, birthDate, gender, getIntent().getIntExtra("schoolID", 1), handedness);
                        String patientDetails = "First Name: " + patient.getFirstName() +
                                "\nLast Name: " + patient.getLastName() +
                                "\nBirthdate: " + patient.getBirthday() +
                                "\nGender: " + patient.getGenderString() +
                                "\nHandedness: " + patient.getHandednessString();
                        questionView.setText(patientDetails);

                        ecaFragment.sendToECAToSPeak(R.string.add_patient_confirm);
                        break;
                    case 5:
                        RelativeLayout saveCancelLayout = (RelativeLayout) findViewById(R.id.saveCancelLayout);
                        questionView.setVisibility(View.GONE);
                        saveCancelLayout.setVisibility(View.GONE);
                        FrameLayout remarksLayout = (FrameLayout) findViewById(R.id.remarksFragmentContainer);
                        remarksLayout.setVisibility(View.VISIBLE);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        remarksFragment = new RemarksFragment();

                        transaction.add(R.id.remarksFragmentContainer, remarksFragment, RemarksFragment.class.getName());
                        transaction.commit();
                        break;
                    default:
                        break;
                }
                questionCounter++;
                if (questionCounter < 5) {
                    setQuestion(questions[questionCounter]);
                }
            }
        });

    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Starts {@link PatientListActivity} and ends the current activity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddPatientActivity.this, PatientListActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Saves {@link #questionCounter} inside {@code outState}
     *
     * @see android.app.Activity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("questionCounter", questionCounter);
    }

    /**
     * Display question on screen based on resID parameter
     *
     * @param resID string resource ID of the question
     */
    private void setQuestion(int resID) {
        questionView.setText(resID);
        ecaFragment.sendToECAToSpeak(getResources().getString(resID));
    }

    /**
     * @return the text from {@link #editText}
     */
    private String getEditText() {
        return editText.getText().toString();
    }


    /**
     * Gets the String of the birthdate of patient in the format of MM/DD/YYYY
     *
     * @return the date from {@link #datePicker}
     */
    private String getSelectedDate() {
        return (datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear());
    }

    /**
     * Saves patient to database.
     *
     * @param patient to be saved to the database
     */
    private void savePatientToDatabase(Patient patient) {
        DatabaseAdapter getBetterDb = new DatabaseAdapter(AddPatientActivity.this);
        try {
            getBetterDb.openDatabaseForRead();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        patient.printPatient();
        getBetterDb.insertPatient(patient);

        getBetterDb.closeDatabase();
    }

    /**
     * Implemented from the RemarksFragment.OnFragmentInteractionListener interface.
     * Sets the {@link Patient#remarksString} and {@link Patient#remarksAudio} of the patient
     *
     * @see RemarksFragment.OnFragmentInteractionListener#onDoneRemarks(String remarkString, byte[] remarkAudio)
     */
    @Override
    public void onDoneRemarks(String remarkString, byte[] remarkAudio) {
        patient.setRemarksString(remarkString);
        patient.setRemarksAudio(remarkAudio);
        onDoneRemarks();
    }

    /**
     * Implemented from the RemarksFragment.OnFragmentInteractionListener interface.
     * Calls {@link #savePatientToDatabase(Patient)} and starts the {@link PatientListActivity}.
     *
     * @see RemarksFragment.OnFragmentInteractionListener#onDoneRemarks()
     */
    @Override
    public void onDoneRemarks() {
        savePatientToDatabase(patient);
        Intent intent = new Intent(AddPatientActivity.this, PatientListActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Implemented from the RemarksFragment.OnFragmentInteractionListener interface.
     * Sends the question to the ECA
     *
     * @see RemarksFragment.OnFragmentInteractionListener#setRemarksQuestion()
     */
    @Override
    public void setRemarksQuestion() { //do not call this method inside the addPatientActivity
        int question = R.string.remarks_add_patient;
        remarksFragment.setRemarkQuestion(question);
        ecaFragment.sendToECAToSPeak(question);
    }
}
