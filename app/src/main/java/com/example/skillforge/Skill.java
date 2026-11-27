package com.example.skillforge;

public class Skill {

    int id;
    String skillName;
    String skillLevel;

    public Skill(int id, String skillName, String skillLevel) {
        this.id = id;
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    public int getId() {
        return id;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    // Setters
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    @Override
    public String toString() {
        return skillName + " - " + skillLevel;
    }
}