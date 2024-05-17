package org.cis1200.guessWho;

public abstract class Question {

    public abstract boolean evaluateTrait(Character character);

    public enum Trait {
        AGE,
        HAIR_COLOR,
        GENDER,
        EYE_COLOR,
        GLASSES,
        HAT,
        SPECIES,
        TOY
    }

    public static class AgeQuestion extends Question {
        private final Character.Age desiredAge;

        public AgeQuestion(Character.Age desiredAge) {
            super();
            this.desiredAge = desiredAge;
        }

        @Override
        public boolean evaluateTrait(Character character) {
            return desiredAge.equals(character.getAge());
        }
    }

    public static class HairColorQuestion extends Question {
        private final Character.HairColor desiredHairColor;

        public HairColorQuestion(Character.HairColor desiredHairColor) {
            super();
            this.desiredHairColor = desiredHairColor;
        }

        @Override
        public boolean evaluateTrait(Character character) {
            return desiredHairColor.equals(character.getHairColor());
        }
    }

    public static class HatQuestion extends Question {
        private final boolean desiredHatState;

        public HatQuestion(boolean desiredHatState) {
            super();
            this.desiredHatState = desiredHatState;
        }

        @Override
        public boolean evaluateTrait(Character character) {
            return desiredHatState == character.hasHat();
        }
    }

    public static class GenderQuestion extends Question {
        private final HumanCharacter.Gender desiredGender;

        public GenderQuestion(HumanCharacter.Gender desiredGender) {
            super();
            this.desiredGender = desiredGender;
        }

        @Override
        public boolean evaluateTrait(Character character) {
            // Check if the character is a HumanCharacter or PetCharacter
            if (character instanceof HumanCharacter) {
                return desiredGender.equals(((HumanCharacter) character).getGender());
            } else if (character instanceof PetCharacter) {
                // Handle PetCharacter age comparison if needed
                return false;
            }
            return false;
        }
    }

    public static class EyeColorQuestion extends Question {
        private final HumanCharacter.EyeColor desiredEyeColor;

        public EyeColorQuestion(HumanCharacter.EyeColor desiredEyeColor) {
            super();
            this.desiredEyeColor = desiredEyeColor;
        }

        public boolean evaluateTrait(Character character) {
            // Check if the character is a HumanCharacter or PetCharacter
            if (character instanceof HumanCharacter) {
                return desiredEyeColor.equals(((HumanCharacter) character).getEyeColor());
            } else if (character instanceof PetCharacter) {
                // Handle PetCharacter age comparison if needed
                return false;
            }
            return false;
        }
    }

    public static class GlassesQuestion extends Question {
        private final boolean desiredGlassesState;

        public GlassesQuestion(boolean desiredGlassesState) {
            super();
            this.desiredGlassesState = desiredGlassesState;
        }

        public boolean evaluateTrait(Character character) {
            // Check if the character is a HumanCharacter or PetCharacter
            if (character instanceof HumanCharacter) {
                return desiredGlassesState == ((HumanCharacter) character).hasGlasses();
            } else if (character instanceof PetCharacter) {
                // Handle PetCharacter age comparison if needed
                return false;
            }
            return false;
        }
    }

    public static class SpeciesQuestion extends Question {
        private final PetCharacter.Species desiredSpecies;

        public SpeciesQuestion(PetCharacter.Species desiredSpecies) {
            super();
            this.desiredSpecies = desiredSpecies;
        }

        public boolean evaluateTrait(Character character) {
            // Check if the character is a HumanCharacter or PetCharacter
            if (character instanceof PetCharacter) {
                return desiredSpecies.equals(((PetCharacter) character).getSpecies());
            } else if (character instanceof HumanCharacter) {
                // Handle PetCharacter age comparison if needed
                return false;
            }
            return false;
        }
    }

    public static class ToyQuestion extends Question {
        private final boolean desiredToyState;

        public ToyQuestion(boolean desiredToyState) {
            super();
            this.desiredToyState = desiredToyState;
        }

        public boolean evaluateTrait(Character character) {
            // Check if the character is a HumanCharacter or PetCharacter
            if (character instanceof PetCharacter) {
                return desiredToyState == ((PetCharacter) character).hasToy();
            } else if (character instanceof HumanCharacter) {
                // Handle PetCharacter age comparison if needed
                return false;
            }
            return false;
        }
    }

}
