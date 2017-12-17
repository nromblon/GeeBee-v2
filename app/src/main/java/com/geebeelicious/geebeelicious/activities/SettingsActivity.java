package com.geebeelicious.geebeelicious.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.adapters.EyeChartsAdapter;
import com.geebeelicious.geebeelicious.adapters.RecyclerViewClickListener;
import com.geebeelicious.geebeelicious.adapters.SchoolsRecyclerAdapter;
import com.geebeelicious.geebeelicious.database.DatabaseAdapter;
import com.geebeelicious.geebeelicious.database.VolleySingleton;
import com.geebeelicious.geebeelicious.models.Syncable;
import com.geebeelicious.geebeelicious.models.consultation.School;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The SettingsActivity serves as the activity containing
 * the various settings and preferences for the app.
 * These include school settings and hearing calibration.
 *
 * @author Katrina Lacsamana
 */


public class SettingsActivity extends ActionBarActivity implements RecyclerViewClickListener{
    private static final String TAG = "SettingsActivity";
    private static final String URL_SAVE_NAME = "http://128.199.205.226/server.php";
    private DatabaseAdapter geebeeDb;
    /**
     * School chosen by the user.
     */
    private School chosenSchool = null;

    /**
     * VisualAcuity chart chosen by the user. Can either be Snellen or Tumbling E eye chart.
     */
    private int chosenVisualAcuityChart;

    /**
     * Contains all the schools from the database.
     */
    private ArrayList<School> schools;

    /**
     * Used as font for the different UI properties.
     */
    private Typeface chalkFont;

    private SchoolsRecyclerAdapter schoolsAdapter;
    private RecyclerView schoolsRecyclerView;
    private RecyclerView.LayoutManager schoolLayoutManager;
    private Button addSchoolButton;
    private Button removeSchoolButton;

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

    /**
     * Initializes views and other activity objects.
     *
     * @see android.app.Activity#onCreate(Bundle)
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        geebeeDb = new DatabaseAdapter(getApplicationContext());

        chalkFont = Typeface.createFromAsset(getAssets(), "fonts/DJBChalkItUp.ttf");

        TextView schoolNameTV = (TextView) findViewById(R.id.schoolnameTV);
        schoolNameTV.setTypeface(chalkFont);

        TextView chartNameTV = (TextView) findViewById(R.id.visualacuitychartNameTV);
        chartNameTV.setTypeface(chalkFont);

        TextView calibrateHearingTV = (TextView) findViewById(R.id.calibrateHearingTV);
        calibrateHearingTV.setTypeface(chalkFont);

        addChooseSchoolSetting();
        addVisualAcuityChartSetting();
        addCalibrationSetting();

        Button saveButton = (Button) findViewById(R.id.saveSettingsButton);
        saveButton.setTypeface(chalkFont);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVisualAcuityChart();
                if(saveSchool())
                    finish();
            }
        });
    }

    /**
     * Adds option to select a school to the Settings screen.
     */
    private void addChooseSchoolSetting() {
        final DatabaseAdapter getBetterDb = new DatabaseAdapter(SettingsActivity.this);
        try {
            getBetterDb.openDatabaseForRead();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        schools = getBetterDb.getAllSchools();
        getBetterDb.closeDatabase();
        schools = new ArrayList<>();
        schoolsAdapter = new SchoolsRecyclerAdapter(this, schools, this);
        schoolsRecyclerView = (RecyclerView) findViewById(R.id.schoolList);
        schoolLayoutManager = new LinearLayoutManager(this);
        schoolsRecyclerView.setLayoutManager(schoolLayoutManager);
        schoolsRecyclerView.setAdapter(schoolsAdapter);
        updateRecyclerView();

        addSchoolButton = (Button) findViewById(R.id.addSchoolButton);
        removeSchoolButton = (Button) findViewById(R.id.removeSchoolButton);

        addSchoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                LinearLayout layout = new LinearLayout(SettingsActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                String regions[] = geebeeDb.getAllRegions().toArray(new String[0]);

                ArrayAdapter<String> regionAdapter= new ArrayAdapter<>(SettingsActivity.this,android.R.layout.simple_spinner_item, regions);
                regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//                final EditText schoolInput = new EditText(SettingsActivity.this);
//                final EditText municipalityInput = new EditText(SettingsActivity.this);
//                final TextView provinceText = new TextView(SettingsActivity.this);
//                final Spinner provinceInput = new Spinner(SettingsActivity.this);
                final TextView regionText = new TextView(SettingsActivity.this);
                final Spinner regionInput = new Spinner(SettingsActivity.this);

                regionInput.setAdapter(regionAdapter);

                alertDialog.setTitle("Add School");



                regionText.setText("Region");
                regionText.setTextColor(Color.BLACK);



//                layout.addView(schoolInput);
//                layout.addView(municipalityInput);
//                layout.addView(provinceText);
                //layout.addView(provinceInput);
                layout.addView(regionText);
                layout.addView(regionInput);

                alertDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {// province
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int regionId = geebeeDb.getRegionId(regionInput.getSelectedItem().toString());
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                        LinearLayout layout = new LinearLayout(SettingsActivity.this);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        alertDialog.setTitle("Add School");

                        final TextView provinceText = new TextView(SettingsActivity.this);
                        final Spinner provinceInput = new Spinner(SettingsActivity.this);

                        String provinces[] = geebeeDb.getAllProvincesFrom(regionId).toArray(new String[0]);
                        ArrayAdapter<String> provinceAdapter= new ArrayAdapter<>(SettingsActivity.this,android.R.layout.simple_spinner_item, provinces);
                        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        provinceText.setText("Provinces");
                        provinceText.setTextColor(Color.BLACK);
                        provinceInput.setAdapter(provinceAdapter);
                        layout.addView(provinceText);
                        layout.addView(provinceInput);

                        alertDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {//municipality
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int provinceId = geebeeDb.getProvinceId(provinceInput.getSelectedItem().toString());
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                                LinearLayout layout = new LinearLayout(SettingsActivity.this);
                                layout.setOrientation(LinearLayout.VERTICAL);

                                alertDialog.setTitle("Add School");
                                final TextView municipalityText = new TextView(SettingsActivity.this);
                                final Spinner municipalityInput = new Spinner(SettingsActivity.this);
                                String municipalities[] = geebeeDb.getAllMunicipalitiesFrom(provinceId).toArray(new String[0]);
                                ArrayAdapter<String> municipilityAdapter= new ArrayAdapter<>(SettingsActivity.this,android.R.layout.simple_spinner_item, municipalities);
                                municipilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                municipalityInput.setAdapter(municipilityAdapter);
                                municipalityText.setText("Municipality");
                                municipalityText.setTextColor(Color.BLACK);
                                layout.addView(municipalityText);
                                layout.addView(municipalityInput);

                                alertDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {//municipality
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
                                        LinearLayout layout = new LinearLayout(SettingsActivity.this);
                                        alertDialog.setTitle("Add School");
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        final TextView schoolText = new TextView(SettingsActivity.this);
                                        final EditText schoolInput = new EditText(SettingsActivity.this);
                                        schoolInput.setHint("School Name");
                                        schoolInput.setTextColor(Color.BLACK);
                                        layout.addView(schoolText);
                                        layout.addView(schoolInput);
                                        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                geebeeDb.insertSchool(new School(schoolInput.getText().toString(), municipalityInput.getSelectedItem().toString()));
                                                updateRecyclerView();
                                            }
                                        });
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                        layout.setLayoutParams(lp);
                                        alertDialog.setView(layout); // uncomment this line
                                        alertDialog.show();
                                    }
                                });
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                layout.setLayoutParams(lp);
                                alertDialog.setView(layout); // uncomment this line
                                alertDialog.show();

                            }
                        });
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        layout.setLayoutParams(lp);
                        alertDialog.setView(layout); // uncomment this line
                        alertDialog.show();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingsActivity.this, "Add School Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                layout.setLayoutParams(lp);
                alertDialog.setView(layout); // uncomment this line
                alertDialog.show();
            }
        });
        removeSchoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexToRemove =  schoolsAdapter.getSelectedIndex();
                if(indexToRemove < 0 ){
                    Toast.makeText(SettingsActivity.this, "Please select a School to remove first.", Toast.LENGTH_SHORT).show();
                }
                else{
                    geebeeDb.removeSchool(schools.get(indexToRemove).getSchoolId());
                    updateRecyclerView();

                }
            }
        });

        //Upload all data present
        Button uploadAllPatientData = (Button) findViewById(R.id.UploadDataButton);
        uploadAllPatientData.setTypeface(chalkFont);
        uploadAllPatientData.setOnClickListener(new View.OnClickListener() {
            public boolean isConnected()
            {
                String command = "ping -c 1 google.com";
                try {
                    return (Runtime.getRuntime().exec (command).waitFor() == 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return  false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return  false;
                }
            }
            @Override
            public void onClick(View v) {
                if (uploadAllData() && isConnected()) {
                    Toast.makeText(getApplicationContext(), "Uploaded All Data Successfully.", Toast.LENGTH_LONG).show();
                    //setSynced();


                } else
                    Toast.makeText(getApplicationContext(), "Failed to Upload Data.", Toast.LENGTH_LONG).show();
            }
//             private void setSynced(){
//                 geebeeDb.setToSynced();
//             }

            public  void writeToFile(String body)
            {
                FileOutputStream fos = null;

                try {
                    final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/folderName/" );

                    if (!dir.exists())
                    {
                        if(!dir.mkdirs()){
                            Log.e("ALERT","could not create the directories");
                        }
                    }

                    final File myFile = new File(dir, "json.txt");

                    if (!myFile.exists())
                    {
                        myFile.createNewFile();
                    }

                    fos = new FileOutputStream(myFile);

                    fos.write(body.getBytes());
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            private boolean uploadAllData() {
                boolean successful = true;
                Syncable syncableToUpload = geebeeDb.getAllUnsyncedRows();

                if(syncableToUpload.getUnsyncedPatients().size() == 0 && syncableToUpload.getUnsyncedSchool().size() == 0
                        && syncableToUpload.getUnsyncedRecords().size() == 0 && syncableToUpload.getUnsyncedHPI().size() == 0 )
                    return successful;

                //writeToFile(syncableToUpload.getRecordJSON());
//                Log.d(TAG, syncableToUpload.getSchoolJSON());

                if(postRequestUpload("upload-school", syncableToUpload.getSchoolJSON()))
                    getBetterDb.setSchoolSynced();
                else return false;

                if(postRequestUpload("upload-patient", syncableToUpload.getPatientJSON()))
                    getBetterDb.setPatientSynced();
                else return false;

                if(postRequestUpload("upload-record", syncableToUpload.getRecordJSON()))
                    getBetterDb.setRecordSynced();
                else return false;

                if(postRequestUpload("upload-hpi", syncableToUpload.getHPIJSON()))
                    getBetterDb.setHPISynced();
                else return false;
                return successful;
            }

            private boolean postRequestUpload(final String typeRequest, final String jsonToSend) {
                final boolean[] success = {true};

                final StringRequest request = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (typeRequest.equals("upload-school") && response.equals("SUCCESS-SCHOOL"))
                                    Toast.makeText(SettingsActivity.this, "Uploaded Schools.", Toast.LENGTH_SHORT).show();
                                else if(typeRequest.equals("upload-patient") && response.equals("SUCCESS-PATIENT"))
                                    Toast.makeText(SettingsActivity.this, "Uploaded Patients.", Toast.LENGTH_SHORT).show();
                                else if(typeRequest.equals("upload-record") && response.equals("SUCCESS-RECORD"))
                                    Toast.makeText(SettingsActivity.this, "Uploaded Records.", Toast.LENGTH_SHORT).show();
                                else if(typeRequest.equals("upload-hpi") && response.equals("SUCCESS-HPI"))
                                    Toast.makeText(SettingsActivity.this, "Uploaded HPI.", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(SettingsActivity.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
                                    success[0] = false;
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SettingsActivity.this, "Upload Error. Bad Response.", Toast.LENGTH_SHORT).show();
                        success[0] = false;
                    }

                }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("request", typeRequest);
                        params.put("json_upload", jsonToSend);
                        return params;
                    }
                };
                VolleySingleton.getInstance(SettingsActivity.this).addToRequestQueue(request);
                return success[0];
            }
        });

    }

    private void updateRecyclerView() {
        schools.clear();
        schools.addAll(geebeeDb.getAllSchools());
        Log.d(TAG, "School Count: " +schools.size() );
        this.schoolsAdapter.notifyDataSetChanged();
    }




    /**
     * Saves schoolID of preferred school to device storage
     */
    private boolean saveSchool() {
        ByteBuffer b = ByteBuffer.allocate(4);
        try{
            chosenSchool = schools.get(schoolsAdapter.getSelectedIndex());
        }catch (ArrayIndexOutOfBoundsException e){
            Toast.makeText(this, "Error getting chosen school.", Toast.LENGTH_SHORT).show();
        }

        //deleteFile("SchoolIDPreferences");
        try {
            if (schoolsAdapter.getSelectedIndex() < 0) {
                throw new IndexOutOfBoundsException();
            } else {
                b.putInt(chosenSchool.getSchoolId());
                byte[] byteArray = b.array();
                Toast.makeText(this, "School Saved: " + chosenSchool.getSchoolName(), Toast.LENGTH_SHORT).show();
                try {
                    FileOutputStream fos = openFileOutput("SchoolIDPreferences", Context.MODE_PRIVATE);
                    try {
                        fos.write(byteArray);
                        fos.close();
                    } catch (IOException ioe) {
                        Toast.makeText(this, "Save School Failed. Can't write file.", Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException fe) {
                    Toast.makeText(this, "Save School Failed. No save file found.", Toast.LENGTH_LONG).show();
                }
            }
        }catch (IndexOutOfBoundsException e){
            Toast.makeText(this, "Select a School to save.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    /**
     * Add option to select visual acuity chart to be used in test
     */
    private void addVisualAcuityChartSetting() {
        ArrayList<String> chartNames = new ArrayList<>();
        chartNames.add("Snellen Eye Chart");
        chartNames.add("Tumbling E Eye Chart");
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SettingsActivity.this, R.array.visualacuity_chart_names, R.layout.item_eyechart_list);w

        EyeChartsAdapter adapter = new EyeChartsAdapter(this, chartNames, chalkFont);
        adapter.setDropDownViewResource(R.layout.item_eyechart_list);
        Spinner chartSpinner = (Spinner) findViewById(R.id.visualacuitychartSpinner);
        chartSpinner.setAdapter(adapter);
        chartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenVisualAcuityChart = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chosenVisualAcuityChart = 0;
            }
        });
    }

    /**
     * Save visual acuity chart preference
     */
    private void saveVisualAcuityChart() {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(chosenVisualAcuityChart);
        byte[] byteArray = b.array();

        try {
            FileOutputStream fos = openFileOutput("VisualAcuityChartPreferences", Context.MODE_PRIVATE);
            try {
                fos.write(byteArray);
                fos.close();
            } catch (IOException ioe) {

            }
        } catch (FileNotFoundException fe) {

        }
    }

    /**
     * Adds option to calibrate hearing test
     */
    private void addCalibrationSetting() {
        Button calibrateButton = (Button) findViewById(R.id.calibrateHearingTestButton);
        calibrateButton.setTypeface(chalkFont);
        calibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HearingCalibrationActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }



}
