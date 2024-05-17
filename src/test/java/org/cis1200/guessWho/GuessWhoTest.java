package org.cis1200.guessWho;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class GuessWhoTest {

    private GuessWhoGame game;
    private GameServer server;
    private Player player1;
    private Player player2;
    private Character character1;
    private Character character2;

    @BeforeEach
    public void setUp() {
        // Initialize game and players
        List<Player> players = new ArrayList<>();
        List<Character> characters = new ArrayList<>();

        // Create characters for testing
        character1 = new Character(
                "Character 1", Character.Age.YOUNG, Character.HairColor.BROWN, false
        );
        character2 = new Character(
                "Character 2", Character.Age.YOUNG, Character.HairColor.BROWN, false
        );

        // Add characters to the list
        characters.add(character1);
        characters.add(character2);

        // Create players with their secret characters
        player1 = new Player("Player 1", characters);
        player1.setSecretCharacter(character1);

        player2 = new Player("Player 2", characters);
        player2.setSecretCharacter(character2);

        // Add players to the list
        players.add(player1);
        players.add(player2);

        // Initialize the game
        game = new GuessWhoGame(players, characters);
        game.setOpponentSecretCharacter(player1, character1);
        game.setOpponentSecretCharacter(player2, character2);

        // Initialize the server
        server = new GameServer(12345, players, characters);
    }

    @AfterEach
    public void tearDown() {
        server.stopServer();
    }

    @Test
    public void testIsCorrectGuess() {
        // Test correct guess
        assertTrue(game.isCorrectGuess(player1, "Character 1"));

        // Test incorrect guess
        assertFalse(game.isCorrectGuess(player1, "Character 2"));
    }

    @Test
    public void testGetSecretCharacter() {
        // Test getting secret character for player 1
        assertEquals(character1, game.getSecretCharacter(player1));

        // Test getting secret character for player 2
        assertEquals(character2, game.getSecretCharacter(player2));
    }

    @Test
    public void testBoundaryCases() {
        // Test with empty player list
        GuessWhoGame emptyPlayerGame = new GuessWhoGame(
                new ArrayList<>(), Arrays.asList(character1, character2)
        );
        // No winner should be returned with an empty player list
        assertNull(emptyPlayerGame.getWinner());

        // Test with empty character list
        GuessWhoGame emptyCharacterGame = new GuessWhoGame(
                Arrays.asList(player1, player2), new ArrayList<>()
        );

        // No winner should be returned with an empty character list
        assertNull(emptyCharacterGame.getWinner());
    }

    @Test
    public void testSelectSecretCharacter() {
        // Test selecting secret character for player 1
        game.setOpponentSecretCharacter(player1, character1);
        assertEquals(character1, game.getSecretCharacter(player1));

        // Test selecting secret character for player 2
        game.setOpponentSecretCharacter(player2, character2);
        assertEquals(character2, game.getSecretCharacter(player2));
    }

    @Test
    public void testGameOverSameSecretCharacter() {
        // Test game over condition when all players have the same secret character
        game.setOpponentSecretCharacter(player1, character1);
        game.setOpponentSecretCharacter(player2, character1);
        assertFalse(server.isGameOver()); // All players can have the same secret character
    }

    @Test
    public void testCharacterEliminationIncorrectGuess() {
        // Test character elimination when a player guesses incorrectly
        game.isCorrectGuess(player1, "Character 2");
        player1.removePlayerCharacter(character2);
        // Character should be eliminated
        assertFalse(player1.getPlayerCharacters().contains(character2));
    }

    @Test
    public void testSameCharacterSelection() {
        // Test character elimination when all players select the same character
        game.setOpponentSecretCharacter(player1, character1);
        game.setOpponentSecretCharacter(player2, character1);
        assertFalse(server.isGameOver()); // All players can have the same secret character
        assertEquals(character1, player1.getSecretCharacter());
        assertEquals(character1, player2.getSecretCharacter());
    }

    @Test
    public void testPlayerRotation() {
        // Test that the game correctly rotates to the next player after each turn
        assertEquals(player1, server.getCurrentPlayer());
        server.advanceToNextPlayer();
        assertEquals(player2, server.getCurrentPlayer());

        // Test that the game rotates back to the first player after the last player
        // takes a turn
        server.advanceToNextPlayer();
        assertEquals(player1, server.getCurrentPlayer());
    }

    @Test
    public void testInvalidGuesses() {
        // Test guessing a character that doesn't exist
        assertFalse(game.isCorrectGuess(player1, "Non-existent Character"));

        // Test guessing with empty input
        assertFalse(game.isCorrectGuess(player1, ""));
    }

    @Test
    public void testGameStart() {
        // Ensure that the game is not over at the start
        assertFalse(server.isGameOver());

        // Ensure that both players have their secret characters selected
        assertNotNull(game.getSecretCharacter(player1));
        assertNotNull(game.getSecretCharacter(player2));
    }

    @Test
    public void testPlayerInitialization() {
        // Ensure that each player has the correct set of characters
        assertEquals(2, player1.getPlayerCharacters().size());
        assertEquals(2, player2.getPlayerCharacters().size());
    }

    @Test
    public void testCharacterInitialization() {
        // Test character attributes
        assertEquals("Character 1", character1.toString());
        assertEquals(Character.Age.YOUNG, character1.getAge());
        assertEquals(Character.HairColor.BROWN, character1.getHairColor());
        assertNotEquals(Character.HairColor.GRAY, character1.getHairColor());
    }

    @Test
    public void testCharacterEliminationOnIncorrectGuess() {
        // Simulate incorrect guess by player 1
        assertFalse(game.isCorrectGuess(player1, "Character 2"));
        player1.removePlayerCharacter(character2);
        // Ensure character 2 is eliminated from player 1's list of characters
        assertFalse(player1.getPlayerCharacters().contains(character2));
    }

    @Test
    public void testGameOverOnCorrectGuess() {
        // Simulate correct guess by player 1
        assertTrue(game.isCorrectGuess(player1, "Character 1"));
        game.endGame();
        assertTrue(game.getGameOver()); // Game should be over
    }

    @Test
    public void testGameOverWithSameSecretCharacters() {
        // Set the same secret character for both players
        game.setOpponentSecretCharacter(player1, character1);
        game.setOpponentSecretCharacter(player2, character1);

        // Ensure the game is not over since all players can have the same secret
        // character
        assertFalse(server.isGameOver());
    }

    @Test
    public void testGameInitializationWithEmptyLists() {
        // Test initializing the game with an empty player list
        GuessWhoGame emptyPlayerGame = new GuessWhoGame(
                new ArrayList<>(), Arrays.asList(character1, character2)
        );
        // No winner should be returned with an empty player list
        assertNull(emptyPlayerGame.getWinner());

        // Test initializing the game with an empty character list
        GuessWhoGame emptyCharacterGame = new GuessWhoGame(
                Arrays.asList(player1, player2), new ArrayList<>()
        );
        // No winner should be returned with an empty character list
        assertNull(emptyCharacterGame.getWinner());
    }

}
