package com.geebeelicious.geebeelicious.interfaces;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.geebeelicious.geebeelicious.R;
import com.geebeelicious.geebeelicious.fragments.ECAFragment;

/**
 * The ECAActivity serves as the abstract class for activities that uses the ECAFragment.
 * The activity that extends the ECAActivity must have a layout view named placeholderECA.
 *
 * @author Mary Grace Malana
 */
public abstract class ECAActivity extends ActionBarActivity {

    /**
     * Fragment that is attached to the activity's ECA placeholder.
     */
    protected ECAFragment ecaFragment;

    /**
     * Integrates {@link #ecaFragment} to the view for the ECA.
     */
    protected void integrateECA() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ecaFragment = (ECAFragment) fragmentManager.findFragmentByTag(ECAFragment.class.getName());
        if (ecaFragment == null) {
            ecaFragment = new ECAFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.placeholderECA, ecaFragment, ECAFragment.class.getName());
            transaction.commit();
        }
    }
}
