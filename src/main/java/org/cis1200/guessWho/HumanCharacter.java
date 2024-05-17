package org.cis1200.guessWho;

public class HumanCharacter extends Character {

    private final Gender gender;
    private final EyeColor eyeColor;
    private final boolean glasses;

    public HumanCharacter(
            String name, Age age, HairColor hairColor,
            boolean hat, Gender gender, EyeColor eyeColor, boolean glasses
    ) {
        super(name, age, hairColor, hat);
        this.gender = gender;
        this.eyeColor = eyeColor;
        this.glasses = glasses;
    }

    public Gender getGender() {
        return gender;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public boolean hasGlasses() {
        return glasses;
    }

    public enum Gender {
        MAN,
        WOMAN
    }

    public enum EyeColor {
        BROWN,
        HAZEL,
        GREEN,
        BLUE
    }

}
