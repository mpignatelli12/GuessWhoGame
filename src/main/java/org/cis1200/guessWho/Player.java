package org.cis1200.guessWho;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private Character secretCharacter;
    private final List<Character> playerCharacters;

    public Player(String name, List<Character> characters) {
        this.name = name;
        this.playerCharacters = new ArrayList<>(characters);
    }

    public String toString() {
        return name;
    }

    public Character getSecretCharacter() {
        return secretCharacter;
    }

    public void setSecretCharacter(Character secretCharacter) {
        this.secretCharacter = secretCharacter;
    }

    public List<Character> getPlayerCharacters() {
        return playerCharacters;
    }

    public void removePlayerCharacter(Character character) {
        playerCharacters.remove(character);
    }

}
