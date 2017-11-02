package com.geebeelicious.geebeelicious.models.grossmotor;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geebeelicious.geebeelicious.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The GrossMotorTest class represents the
 * gross motor test that the child will
 * take. It generates a short list of skills
 * from the general list of skills, as well as
 * generates music for the test. The class
 * allows the test to be generated for each
 * user and allows the user to perform
 * the skill.
 *
 * @author Katrina Lacsamana
 * @since 03/21/2016
 */
public class GrossMotorTest {

    /**
     * List of all possible grossmotor skills to be tested.
     */
    private GrossMotorSkill[] grossMotorSkills;

    /**
     * List of grossmotor skills to be tested.
     */
    private GrossMotorSkill[] testSkills;

    /**
     * Musicplayer to be used for playing music
     * while running the skillset.
     */
    private MusicPlayer musicPlayer;

    /**
     * Index of the current skill tested from {@link #testSkills}.
     */
    private int currentSkill;

    /**
     * Used in counting down the remaining time.
     */
    private CountDownTimer countDownTimer;

    /**
     * Constructor.
     *
     * @param context current context.
     */
    public GrossMotorTest(Context context) {
        musicPlayer = new MusicPlayer(context);
        grossMotorSkills = new GrossMotorSkill[8];
        testSkills = new GrossMotorSkill[3];
        currentSkill = 0;
        grossMotorSkills[0] = new GrossMotorSkill("Jumping Jacks", "Jumping", "Do jumping jacks", 30000, R.drawable.grossmotor_jumping_jacks);
        grossMotorSkills[1] = new GrossMotorSkill("Jump in Place", "Jumping", "Jump in place", 30000, R.drawable.grossmotor_jump);
        grossMotorSkills[2] = new GrossMotorSkill("Run in Place", "Running", "Run in place", 40000, R.drawable.grossmotor_run);
        grossMotorSkills[3] = new GrossMotorSkill("Hop in Place", "Hopping", "Hop on one foot", 20000, R.drawable.grossmotor_hop);
        grossMotorSkills[4] = new GrossMotorSkill("One Foot Balance", "Balance", "Stand and balance on one foot", 15000, R.drawable.grossmotor_one_foot_balance);
        grossMotorSkills[5] = new GrossMotorSkill("Walk in Place", "Walking", "Walk in place", 60000, R.drawable.grossmotor_walk);
        grossMotorSkills[6] = new GrossMotorSkill("March in Place", "Marching", "March in place", 60000, R.drawable.grossmotor_march);
        grossMotorSkills[7] = new GrossMotorSkill("Jog in Place", "Jogging", "Jog in place", 40000, R.drawable.grossmotor_jog);
    }

    /**
     * Gets a random skill that hasnt been tested yet.
     *
     * @return skill to be tested next.
     */
    private GrossMotorSkill getRandomSkill() {
        Random random = new Random((int) System.nanoTime());
        boolean isFound = false;
        GrossMotorSkill temp = null;

        while (!isFound) {
            temp = grossMotorSkills[random.nextInt(grossMotorSkills.length - 1)];
            if (!checkSkillDuplicates(testSkills, temp)) {
                break;
            }
        }
        return temp;
    }

    /**
     * Check whether the {@code array} contains {@code key}
     *
     * @param array List of skills to be examined
     * @param key   to be searched inside the {@code array}
     * @return
     */
    private boolean checkSkillDuplicates(GrossMotorSkill[] array, GrossMotorSkill key) {
        for (GrossMotorSkill gms : array) {
            if (key == gms) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initialize the values for {@link #testSkills}.
     */
    public void makeTest() {
        for (int i = 0; i < 3; i++) {
            testSkills[i] = getRandomSkill();
        }
    }

    /**
     * Sets {@link #currentSkill}.
     *
     * @param skillNumber new value
     */
    public void setCurrentSkill(int skillNumber) {
        currentSkill = skillNumber;
    }

    /**
     * Perform the skill with index {@code skillNumber}
     *
     * @param skillNumber Index of the skill in {@link #testSkills} to be performed.
     * @param timerView   where time is shown.
     * @param answers     contains the yes and no buttons.
     * @param NAButton    to be pressed when the user cant perform skill.
     */
    public void performSkill(int skillNumber, final TextView timerView, final LinearLayout answers, final Button NAButton) {
        GrossMotorSkill skill = testSkills[skillNumber];
        musicPlayer.setRandomSong(skill.getDuration());
        countDownTimer = new CountDownTimer(skill.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timerString = "" + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                timerView.setText(timerString);
            }

            @Override
            public void onFinish() {
                timerView.setText("");
                answers.setVisibility(View.VISIBLE);
                for (int j = 0; j < answers.getChildCount(); j++) {
                    View view = answers.getChildAt(j);
                    view.setEnabled(true);
                    view.setVisibility(View.VISIBLE);
                }
                musicPlayer.stopMusic();
                NAButton.setVisibility(View.GONE);
            }
        };

        countDownTimer.start();
        musicPlayer.playMusic();
    }

    /**
     * Gets the skill at index {@link #currentSkill} in {@link #testSkills}.
     *
     * @return
     */
    public GrossMotorSkill getCurrentSkill() {
        return testSkills[currentSkill];
    }

    /**
     * Gets {@link #currentSkill}.
     *
     * @return {@link #currentSkill}
     */
    public int getCurrentSkillNumber() {
        return currentSkill;
    }

    /**
     * Get all the results of all the skills done.
     *
     * @return result string.
     */
    public String getAllResults() {
        String result = "";
        for (GrossMotorSkill gms : testSkills) {
            result += gms.getSkillName() + " : " + gms.getAssessment() + "\n";
        }
        return result;
    }

    /**
     * Gets the overall result of the test in string form.
     *
     * @return overall result.
     */
    public String getFinalResult() {
        int result = getIntFinalResult();

        switch (result) {
            case 0:
                return "Pass";
            case 1:
                return "Fail";
            default:
                return "N/A";
        }
    }

    /**
     * Gets the overall result of the test in int form.
     *
     * @return overall result.
     */
    public int getIntFinalResult() {
        int pass = 0;
        int na = 0;
        String assessment;
        for (GrossMotorSkill gms : testSkills) {
            assessment = gms.getAssessment();
            if (assessment.equals("Pass")) {
                pass++;
            } else if (assessment.equals("N/A")) {
                na++;
            }
        }
        if (pass >= 2) { //Pass
            return 0;
        } else if (na >= 2) { //N/A
            return 2;
        } else { //Fail
            return 1;
        }
    }

    /**
     * Ends the test.
     */
    public void endTest() {
        musicPlayer.stopMusic();
    }

    /**
     * Skips the test
     *
     * @param timerView shows the remaining time.
     */
    public void skipTest(TextView timerView) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerView.setText("");
        musicPlayer.stopMusic();
    }
}
