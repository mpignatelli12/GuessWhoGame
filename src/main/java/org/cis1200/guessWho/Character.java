package org.cis1200.guessWho;

public class Character {

    private final String name;
    private final Age age;
    private final HairColor hairColor;
    private final boolean hat;

    public Character(String name, Age age, HairColor hairColor, boolean hat) {
        this.name = name;
        this.age = age;
        this.hairColor = hairColor;
        this.hat = hat;
    }

    public Age getAge() {
        return age;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public boolean hasHat() {
        return hat;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public enum Age {
        OLD,
        YOUNG,
        MIDDLE,
        PUPPY
    }

    public enum HairColor {
        BROWN,
        BLACK,
        BLOND,
        GRAY,
        RED,
        ORANGE,
        YELLOW,
        GREEN,
        WHITE,
        BLUE
    }

}
