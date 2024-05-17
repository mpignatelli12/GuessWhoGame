package org.cis1200.guessWho;

import java.util.*;

public class GuessWhoGame {
    private final List<Player> players;
    private final List<Character> characters;
    private final Map<Player, Character> opponentSecretCharacters = new HashMap<>();
    private boolean gameOver = false;
    private boolean humanGame = true;

    public GuessWhoGame(List<Player> players, List<Character> characters) {
        this.players = players;
        this.characters = characters;
    }

    public boolean isCorrectGuess(Player player, String guess) {
        return getSecretCharacter(player) != null &&
                getSecretCharacter(player).toString().trim().equalsIgnoreCase(guess.trim());
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Character getSecretCharacter(Player player) {
        return opponentSecretCharacters.get(player);
    }

    public void setOpponentSecretCharacter(Player opponent, Character character) {
        opponentSecretCharacters.put(opponent, character);
        opponent.setSecretCharacter(character);
    }

    private boolean isSecretCharacter(Player player, Character character) {
        return character.equals(player.getSecretCharacter());
    }

    public Player getWinner() {
        for (Player player : players) {
            if (isSecretCharacterGuessed(player)) {
                return player;
            }
        }
        return null;
    }

    public boolean isSecretCharacterGuessed(Player player) {
        Character secretCharacter = getSecretCharacter(player);
        if (secretCharacter == null) {
            return false; // Player hasn't selected a secret character yet
        }
        for (Character character : characters) {
            if (isSecretCharacter(player, character) && !characters.contains(character)) {
                return true; // Player guessed correctly and the character is eliminated
            }
        }
        return false; // Player hasn't guessed correctly or the character is still in the game
    }

    public void endGame() {
        gameOver = true;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    // Setter method to update isHumanGame
    public void setHumanGame(boolean humanGame) {
        this.humanGame = humanGame;
    }

    // Getter method to retrieve the value of isHumanGame
    public boolean isHumanGame() {
        return humanGame;
    }
}
