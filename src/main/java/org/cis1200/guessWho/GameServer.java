package org.cis1200.guessWho;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private ServerSocket serverSocket;
    private GuessWhoGame game;
    private List<Player> players;
    private boolean player1SecretCharacterChosen = false;
    private boolean player2SecretCharacterChosen = false;
    private int currentPlayerIndex = 0;

    public GameServer(int port, List<Player> players, List<Character> characters) {
        try {
            serverSocket = new ServerSocket(port);
            // Initialize the game with players and characters
            game = new GuessWhoGame(players, characters);
            this.players = players;
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
            // Close server socket if it's open
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    System.err.println("Error closing server socket: " + ex.getMessage());
                }
            }
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Updates the index of the current player
    private void updateCurrentPlayerIndex() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    // Ensures proper encapsulation when updating player index
    public void advanceToNextPlayer() {
        updateCurrentPlayerIndex();
    }

    public List<Character> getCharacters() {
        return game.getCharacters();
    }

    public boolean isSecretCharacter(Player player, Character character) {
        Character secretCharacter = game.getSecretCharacter(player);
        return secretCharacter != null && secretCharacter.equals(character);
    }

    public void setOpponentSecretCharacter(Player currentPlayer, Character character) {
        for (Player opponent : players) {
            if (!opponent.equals(currentPlayer)) {
                opponent.setSecretCharacter(character);
                game.setOpponentSecretCharacter(opponent, character);
                break;
            }
        }
    }

    public Character getOpponentSecretCharacter(Player currentPlayer) {
        for (Player opponent : players) {
            if (!opponent.equals(currentPlayer)) {
                return opponent.getSecretCharacter();
            }
        }
        return null;
    }

    public Player getWinner() {
        for (Player player : players) {
            if (game.isSecretCharacterGuessed(player)) {
                return player; // Return the player who guessed the secret character correctly
            }
        }
        return null; // No winner yet
    }

    public boolean isGameOver() {
        return getWinner() != null;
    }

    public void endGame() {
        game.endGame();
    }

    public boolean getGameOver() {
        return game.getGameOver();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isPlayer1SecretCharacterChosen() {
        return player1SecretCharacterChosen;
    }

    public void setPlayer1SecretCharacterChosen(boolean player1SecretCharacterChosen) {
        this.player1SecretCharacterChosen = player1SecretCharacterChosen;
    }

    public boolean isPlayer2SecretCharacterChosen() {
        return player2SecretCharacterChosen;
    }

    public void setPlayer2SecretCharacterChosen(boolean player2SecretCharacterChosen) {
        this.player2SecretCharacterChosen = player2SecretCharacterChosen;
    }

    public void setHumanGame(boolean humanGame) {
        game.setHumanGame(humanGame);
    }

    public boolean getHumanGame() {
        return game.isHumanGame();
    }

    public void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
            // Close server socket if it's open
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    System.err.println("Error closing server socket: " + ex.getMessage());
                }
            }
        }
    }

}