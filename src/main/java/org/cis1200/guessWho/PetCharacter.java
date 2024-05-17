package org.cis1200.guessWho;

public class PetCharacter extends Character {

    private final Species species;
    private final boolean toy;

    public PetCharacter(
            String name, Age age, HairColor hairColor,
            boolean hat, Species species, boolean toy
    ) {
        super(name, age, hairColor, hat);
        this.species = species;
        this.toy = toy;
    }

    public Species getSpecies() {
        return species;
    }

    public boolean hasToy() {
        return toy;
    }

    public enum Species {
        DOG,
        CAT,
        BIRD,
        FISH
    }

}
