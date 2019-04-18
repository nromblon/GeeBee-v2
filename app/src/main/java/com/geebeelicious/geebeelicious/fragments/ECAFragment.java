package com.geebeelicious.geebeelicious.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.geebeelicious.geebeelicious.R;

import edu.usc.ict.vhmobile.VHMobileLib;
import edu.usc.ict.vhmobile.VHMobileMain;
import edu.usc.ict.vhmobile.VHMobileSurfaceView;

/**
 * The ECAFragment serves as the fragment that contains the
 * ECA. This fragment uses VHMobile library to implement the ECA
 *
 * @author Mary Grace Malana
 */
public class ECAFragment extends Fragment {
    /**
     * Used to identify the source of a log message
     */
    private static final String TAG = "ECAFragment";

    /**
     * Activity this fragment is attached to
     */
    private Activity activity;

    /**
     * Latest string the ECA has spoken.
     */
    private String ecaString;

    /**
     * Used for initializing the ECA
     */
    protected VHMobileMain vhmain = null;

    /**
     * View that contains the ECA character
     */
    protected VHMobileSurfaceView _VHview = null;

    /**
     * The enum Emotion is a list of possible emotions
     * that the ECA can emote.
     */
    public enum Emotion {
        HAPPY, CONCERN
    }

    public ECAFragment() {
        // Required empty public constructor
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
        final View view = inflater.inflate(R.layout.fragment_eca, container, false);
        final Button replayButton = (Button) view.findViewById(R.id.replayButton);

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                // its possible that the layout is not complete in which case
                // we will get all zero values for the positions, so ignore the event
                if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                    return;
                }

                int viewHeight = view.getHeight() / 10;
                ViewGroup.LayoutParams params = replayButton.getLayoutParams();
                params.width = viewHeight;
                params.height = viewHeight;

                replayButton.setLayoutParams(params);
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ecaString != null) {
                    sendToECAToSpeak(ecaString);
                }
            }
        });


        //ECA integration
        VHMobileMain.setupVHMobile();

        Log.d(TAG, "The onCreate() event");

        vhmain = new VHMobileMain(activity);
        vhmain.init();
        _VHview = (VHMobileSurfaceView) view.findViewById(R.id.vhview);
        return view;
    }

    /**
     * Overrides method. Keeps a reference of the activity
     * that the fragment is attached to in {@link #activity}
     *
     * @param activity activity that the fragment is attached to.
     * @see Fragment#onAttach(Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    /**
     * Overrides method. Frees the reference of the activity.
     *
     * @see Fragment#onDetach()
     */
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    /**
     * Overrides method. Prints a log on start.
     *
     * @see Fragment#onStart()
     */
    @Override
    public void onStart() {
        Log.d(TAG, "The onStart() event");
        super.onStart();
    }

    /**
     * Overrides method. Prints a log on resume.
     *
     * @see Fragment#onResume() ()
     */
    @Override
    public void onResume() {
        super.onResume();
        /*
         * The activity must call the GL surface view's
         * onResume() on activity onResume().
         */

        if (_VHview != null)
            _VHview.onResume();
        Log.d(TAG, "The onResume() event");

    }

    /**
     * Overrides method. Prints a log on pause.
     *
     * @see Fragment#onPause() ()
     */
    @Override
    public void onPause() {
        super.onPause();
        /*
         * The activity must call the GL surface view's
         * onResume() on activity onResume().
         */
        if (_VHview != null)
            _VHview.onPause();
        Log.d(TAG, "The onPause() event");

    }

    /**
     * Overrides method. Prints a log on stop.
     *
     * @see Fragment#onStop()
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "The onStop() event");
    }

    /**
     * Overrides method. Prints a log on destroy.
     *
     * @see Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "The onDestroy() event");
    }

    /**
     * Sends the parameter {@code sentence} to {@link VHMobileLib} to be executed.
     *
     * @param sentence sentence to be spoken by the ECA
     * @see VHMobileLib#executeSB(String)
     */
    public void sendToECAToSpeak(String sentence) {
        ecaString = sentence;

        Log.d(TAG, "ECA speaks: " + sentence);
        VHMobileLib.executeSB("saySomething(characterName, \"" + sentence + "\")");
    }

    /**
     * Sends the string resource with ID {@code resID} to {@link VHMobileLib} to be executed.
     *
     * @param resID string resource ID of the sentence to be spoken by the ECA.
     */
    public void sendToECAToSPeak(int resID) {
        String sentence = getString(resID);
        sendToECAToSpeak(sentence);
    }

    /**
     * Sends the emotion and intensity to {@link VHMobileLib} to be executed.
     * The higher the intensity, the more prominent the emotion is.
     *
     * @param emotion type of emotion to be executed by the ECA
     * @param i       intensity of {@code emotion}
     */
    public void sendToECAToEmote(Emotion emotion, int i) {
        switch (emotion) {
            case HAPPY:
                Log.d(TAG, "setToHappy");
                VHMobileLib.executeSB("setToHappy(characterName, " + i + ")");
                break;
            case CONCERN:
                Log.d(TAG, "setToConcern");
                VHMobileLib.executeSB("setToConcern(characterName, " + i + ")");
                break;
        }
    }
}
