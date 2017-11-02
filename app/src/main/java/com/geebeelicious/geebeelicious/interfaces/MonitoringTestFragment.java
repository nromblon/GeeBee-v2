package com.geebeelicious.geebeelicious.interfaces;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * MonitoringTestFragment is an abstract class
 * used in MonitoringMainActivity.
 * This class are implemented by monitoring test fragments that has intro and results.
 *
 * @author Mary Grace Malana
 */
public abstract class MonitoringTestFragment extends Fragment {

    /**
     * String resource of the intro regarding the monitoring test.
     */
    protected int intro;

    /**
     * String resource of the result remarks regarding the monitoring test.
     */
    protected int endStringResource;

    /**
     * How long in terms of milliseconds that the result remarks are uttered by the ECA.
     */
    protected int endTime;

    /**
     * End emotion to be expressed by the ECA about the result. If true, happy, else false.
     */
    protected boolean isEndEmotionHappy; //true happy, false concern

    /**
     * If true the fragment has instruction prior to starting the test, else it has no
     * instruction prior to starting the test.
     *
     * @see com.geebeelicious.geebeelicious.fragments.VisualAcuityFragment
     * @see com.geebeelicious.geebeelicious.activities.MonitoringMainActivity#runTransition(int, String, Fragment, boolean)
     */
    protected boolean hasEarlyInstruction;

    /**
     * Main view of the fragment
     */
    protected View view;

    /**
     * Gets {@link #intro}
     *
     * @return {@link #intro}
     */
    public int getIntro() {
        return intro;
    }

    /**
     * Gets {@link #endStringResource}
     *
     * @return {@link #endStringResource}
     */
    public int getEndStringResource() {
        return endStringResource;
    }

    /**
     * Gets {@link #endTime}
     *
     * @return {@link #endTime}
     */
    public int getEndTime() {
        return endTime;
    }

    /**
     * Gets {@link #isEndEmotionHappy}
     *
     * @return {@link #isEndEmotionHappy}
     */
    public boolean isEndEmotionHappy() {
        return isEndEmotionHappy;
    }

    /**
     * Gets {@link #hasEarlyInstruction}
     *
     * @return {@link #hasEarlyInstruction}
     */
    public boolean hasEarlyInstruction() {
        return hasEarlyInstruction;
    }

    /**
     * Hides the {@link #view}
     */
    public void hideFragmentMainView() {
        view.setVisibility(View.GONE);
    }
}
