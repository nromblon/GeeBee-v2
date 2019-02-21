package com.geebeelicious.geebeelicious.models.grossmotor;

/**
 * The GrossMotorSkill class represents a
 * gross motor skill. It contains the name
 * of the skill, the type of skill,
 * instructions to complete the skill, and
 * the duration for which the skill will
 * be performed.
 *
 * @author Katrina Lacsmaana
 * @since 03/21/2016
 */
public class GrossMotorSkill {

    /**
     * Name of the skill
     */
    private String skillName;

    /**
     * Skill type
     */
    private String type;

    /**
     * Instructions to do the skill
     */
    private String instruction;

    /**
     * Duration of the skill to be done in milliseconds (1s = 1000ms).
     */
    private int duration;

    /**
     * Image of the skill
     */
    private int skillResImage;

    /**
     * Results of the test
     */
    private String assessment;

    /**
     * Whether the skill was already tested or not.
     */
    private boolean isTested;

    /**
     * Constructor.
     *
     * @param skillName     {@link #skillName}
     * @param type          {@link #type}
     * @param instruction   {@link #instruction}
     * @param duration      {@link #duration}
     * @param skillResImage {@link #skillResImage}
     */
    public GrossMotorSkill(String skillName, String type, String instruction, int duration, int skillResImage) {
        this.skillName = skillName;
        this.type = type;
        this.instruction = instruction;
        this.duration = duration;
        this.skillResImage = skillResImage;
        this.isTested = false;
        this.assessment = "No Results";
    }

    /**
     * Gets {@link #skillName}
     *
     * @return {@link #skillName}
     */
    public String getSkillName() {
        return skillName;
    }

    /**
     * Gets {@link #type}
     *
     * @return {@link #type}
     */
    public String getType() {
        return type;
    }

    /**
     * Gets {@link #instruction}
     *
     * @return {@link #instruction}
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * Gets {@link #duration}
     *
     * @return {@link #duration}
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Gets {@link #skillResImage}
     *
     * @return {@link #skillResImage}
     */
    public int getSkillResImage() {
        return skillResImage;
    }

    /**
     * Gets {@link #isTested)
     *
     * @return {@link #isTested}
     */
    public boolean isTested() {
        return isTested;
    }

    /**
     * Sets {@link #isTested)
     */
    public void setTested() {
        isTested = true;
    }

    /**
     * Sets {@link #assessment) to pass
     */
    public void setSkillPassed() {
        assessment = "Pass";
    }

    /**
     * Sets {@link #isTested) to fail
     */
    public void setSkillFailed() {
        assessment = "Fail";
    }

    /**
     * Sets {@link #isTested) to n/a
     */
    public void setSkillSkipped() {
        assessment = "N/A";
    }

    /**
     * Gets {@link #assessment}.
     *
     * @return {@link #assessment}
     */
    public String getAssessment() {
        return assessment;
    }

}
