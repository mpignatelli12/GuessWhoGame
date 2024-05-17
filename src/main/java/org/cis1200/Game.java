package org.cis1200;

import org.cis1200.guessWho.*;
import org.cis1200.guessWho.Character;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Game extends JFrame {
    private final GameServer server;
    private final Player player;
    private final JPanel gameBoardPanel;
    private final JPanel controlPanel;
    private final JButton askQuestionButton;
    private final JButton makeGuessButton;
    private final JButton viewOpponentsSecretButton;
    private final JLabel gameOverLabel;
    private Player currentPlayer;

    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class of your choosing and runs it. IMPORTANT: Do NOT delete! You MUST
     * include a main method in your final submission.
     */
    public static void main(String[] args) {
        LoadScreen loadScreen = new LoadScreen(
                (isHumanGame) -> SwingUtilities.invokeLater(
                        () -> {
                            // Initialize the game based on the load screen selection
                            initializeGame(isHumanGame);
                        }
                )
        );

        loadScreen.setVisible(true);
        showInstructionsWindow();
    }

    public Game(GameServer server, Player player) {
        this.server = server;
        this.player = player;
        this.currentPlayer = player;

        this.gameBoardPanel = new JPanel(new GridLayout(4, 4));
        this.controlPanel = new JPanel();
        this.askQuestionButton = new JButton("Ask Question");
        this.makeGuessButton = new JButton("Make Guess");
        this.viewOpponentsSecretButton = new JButton("Reveal Secret Character");

        this.gameOverLabel = new JLabel("", SwingConstants.CENTER);

        askQuestionButton.setEnabled(false);
        makeGuessButton.setEnabled(false);

        setTitle("Guess Who? - " + player.toString());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        initUI();
        startGameLoop();
    }


    // Initializes the UI components and starts the game loop
    private void initUI() {
        getContentPane().setLayout(new BorderLayout());

        for (Character character : server.getCharacters()) {
            JPanel characterPanel = new JPanel(new BorderLayout());

            // Load image for character
            ImageIcon icon = loadImage(character.toString() + ".png");
            JLabel characterLabel = new JLabel(icon);
            characterPanel.add(characterLabel, BorderLayout.CENTER);

            // Add character name below the image
            JLabel nameLabel = new JLabel(character.toString(), SwingConstants.CENTER);
            characterPanel.add(nameLabel, BorderLayout.SOUTH);

            gameBoardPanel.add(characterPanel);
        }

        getContentPane().add(gameBoardPanel, BorderLayout.CENTER);

        askQuestionButton.addActionListener(e -> {
            if (player.equals(server.getCurrentPlayer())) {
                Question.Trait[] traitsToShow = getTraits();

                Question.Trait trait = (Question.Trait) JOptionPane.showInputDialog(
                        null,
                        "Select a trait to ask about:",
                        "Choose Trait",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        traitsToShow,
                        traitsToShow[0]
                );

                if (trait != null) {
                    String value = JOptionPane.showInputDialog("Enter desired value for trait:");

                    if (value != null) {
                        // Create a concrete question subclass based on the selected trait
                        Question question;
                        switch (trait) {
                            case AGE -> question = new Question.AgeQuestion(
                                    Character.Age.valueOf(value.toUpperCase())
                            );
                            case HAIR_COLOR -> question = new Question.HairColorQuestion(
                                    Character.HairColor.valueOf(value.toUpperCase())
                            );
                            case HAT -> question = new Question.HatQuestion(
                                    Boolean.parseBoolean(value)
                            );
                            case GENDER -> question = new Question.GenderQuestion(
                                    HumanCharacter.Gender.valueOf(value.toUpperCase())
                            );
                            case EYE_COLOR -> question = new Question.EyeColorQuestion(
                                    HumanCharacter.EyeColor.valueOf(value.toUpperCase())
                            );
                            case GLASSES -> question = new Question.GlassesQuestion(
                                    Boolean.parseBoolean(value)
                            );
                            case SPECIES -> question = new Question.SpeciesQuestion(
                                    PetCharacter.Species.valueOf(value.toUpperCase())
                            );
                            case TOY -> question = new Question.ToyQuestion(
                                    Boolean.parseBoolean(value)
                            );
                            default -> throw new IllegalArgumentException(
                                    "Unexpected value: " + trait
                            );
                        }

                        boolean answer = isTraitTrueForSecretCharacter(trait, value);
                        // Filter characters based on the question
                        filterCharactersByTrait(question, answer);
                        server.advanceToNextPlayer();
                        updateUI();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "It's not your turn to ask a question.");
            }
        });

        makeGuessButton.addActionListener(e -> {
            if (player.equals(server.getCurrentPlayer())) {
                String characterName = JOptionPane
                        .showInputDialog("Enter character name to guess:");

                if (characterName != null) {
                    boolean found = false;
                    for (int i = 0; i < server.getCharacters().size(); i++) {
                        if (server.getCharacters().get(i).toString()
                                .equalsIgnoreCase(characterName)) {
                            found = true;
                            // Check if guess is correct
                            if (server.isSecretCharacter(player, server.getCharacters().get(i))) {
                                JOptionPane.showMessageDialog(
                                        null, "Congratulations! You guessed correctly."
                                );
                                server.endGame();
                                updateUI();
                            } else {
                                JOptionPane.showMessageDialog(
                                        null, "Sorry, that's not the correct character."
                                );
                                removeCharacterFromGUI(server.getCharacters().get(i));
                            }
                            break;
                        }
                    }

                    if (!found) {
                        JOptionPane.showMessageDialog(null, "Character not found.");
                    }

                    if (!server.getGameOver()) {
                        server.advanceToNextPlayer();
                    }

                    updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "It's not your turn to make a guess.");
            }
        });

        gameOverLabel.setVisible(false);
        controlPanel.add(askQuestionButton);
        controlPanel.add(makeGuessButton);

        getContentPane().add(gameBoardPanel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        // Add a JLabel to display the chosen character
        JLabel chosenCharacterLabel = new JLabel("Chosen Character: ");
        controlPanel.add(chosenCharacterLabel);
        chosenCharacterLabel.setText("");

        JComboBox<Character> secretCharacterDropdown = new JComboBox<>();
        for (Character character : server.getCharacters()) {
            secretCharacterDropdown.addItem(character);
        }

        viewOpponentsSecretButton.addActionListener(e -> {
            // Get the secret character chosen by the opponent
            Character opponentSecretCharacter = server.getOpponentSecretCharacter(player);

            if (opponentSecretCharacter != null) {
                // Display a pop-up message showing the opponent's secret character
                JOptionPane.showMessageDialog(
                        null, "Chosen Secret Character: " + opponentSecretCharacter
                );
            } else {
                // Handle case where opponent's secret character is not yet chosen, should never
                // be reached
                JOptionPane.showMessageDialog(null, "Secret Character has not been chosen yet.");
            }
        });

        // Remove the JComboBox declaration and initialization
        JButton chooseSecretCharacterButton = getjButton(secretCharacterDropdown);

        controlPanel.add(secretCharacterDropdown);
        controlPanel.add(chooseSecretCharacterButton);

    }

    private Question.Trait[] getTraits() {
        Question.Trait[] humanTraits = { Question.Trait.AGE, Question.Trait.HAIR_COLOR,
            Question.Trait.HAT, Question.Trait.GENDER, Question.Trait.EYE_COLOR,
            Question.Trait.GLASSES };
        Question.Trait[] petTraits = { Question.Trait.AGE, Question.Trait.HAIR_COLOR,
            Question.Trait.HAT, Question.Trait.SPECIES, Question.Trait.TOY };

        Question.Trait[] traitsToShow;
        if (server.getHumanGame()) {
            traitsToShow = humanTraits;
        } else {
            traitsToShow = petTraits;
        }
        return traitsToShow;
    }

    private JButton getjButton(JComboBox<Character> secretCharacterDropdown) {
        JLabel secretCharacterLabel = new JLabel("Selected Secret Character: ");
        JButton chooseSecretCharacterButton = new JButton("Choose Secret Character");
        chooseSecretCharacterButton.addActionListener(e -> {
            Character selectedCharacter = (Character) secretCharacterDropdown.getSelectedItem();

            server.setOpponentSecretCharacter(player, selectedCharacter);
            // Display the selected character's name
            assert selectedCharacter != null;
            secretCharacterLabel
                    .setText("Selected Secret Character: " + selectedCharacter);
            // Remove the dropdown and button
            controlPanel.remove(secretCharacterDropdown);
            controlPanel.remove(chooseSecretCharacterButton);
            controlPanel.add(viewOpponentsSecretButton);

            controlPanel.revalidate();
            controlPanel.repaint();
            // Display a message indicating the chosen character
            JOptionPane.showMessageDialog(
                    null,
                    "You have chosen " + selectedCharacter + " as your secret character."
            );

            // Update the flags based on the player
            if (player.equals(server.getPlayers().get(0))) {
                server.setPlayer1SecretCharacterChosen(true);
            } else if (player.equals(server.getPlayers().get(1))) {
                server.setPlayer2SecretCharacterChosen(true);
            }
        });
        return chooseSecretCharacterButton;
    }

    private static void showInstructionsWindow() {
        InstructionsWindow instructionsWindow = new InstructionsWindow();
        instructionsWindow.setVisible(true);
    }

    private static Character[] getHumanCharacters() {
        return new Character[] {
            new HumanCharacter(
                    "John", Character.Age.YOUNG, Character.HairColor.BROWN, false,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.BROWN, false
            ),
            new HumanCharacter(
                    "Emily", Character.Age.MIDDLE, Character.HairColor.BLOND, true,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.BLUE, true
            ),
            new HumanCharacter(
                    "Michael", Character.Age.OLD, Character.HairColor.GRAY, true,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.GREEN, false
            ),
            new HumanCharacter(
                    "Sophia", Character.Age.YOUNG, Character.HairColor.BLACK, false,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.BROWN, true
            ),
            new HumanCharacter(
                    "William", Character.Age.YOUNG, Character.HairColor.BLOND, false,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.BLUE, false
            ),
            new HumanCharacter(
                    "Olivia", Character.Age.MIDDLE, Character.HairColor.BROWN, true,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.GREEN, true
            ),
            new HumanCharacter(
                    "James", Character.Age.OLD, Character.HairColor.GRAY, true,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.BROWN, true
            ),
            new HumanCharacter(
                    "Emma", Character.Age.YOUNG, Character.HairColor.RED, true,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.BLUE, false
            ),
            new HumanCharacter(
                    "Daniel", Character.Age.MIDDLE, Character.HairColor.BLACK, false,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.GREEN, true
            ),
            new HumanCharacter(
                    "Isabella", Character.Age.YOUNG, Character.HairColor.BLOND, true,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.BROWN, false
            ),
            new HumanCharacter(
                    "Alexander", Character.Age.MIDDLE, Character.HairColor.BROWN, true,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.BLUE, true
            ),
            new HumanCharacter(
                    "Ava", Character.Age.YOUNG, Character.HairColor.BROWN, false,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.BROWN, true
            ),
            new HumanCharacter(
                    "Ethan", Character.Age.OLD, Character.HairColor.GRAY, true,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.GREEN, true
            ),
            new HumanCharacter(
                    "Mia", Character.Age.YOUNG, Character.HairColor.BLOND, true,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.BLUE, true
            ),
            new HumanCharacter(
                    "Liam", Character.Age.MIDDLE, Character.HairColor.BROWN, true,
                    HumanCharacter.Gender.MAN, HumanCharacter.EyeColor.BROWN, false
            ),
            new HumanCharacter(
                    "Charlotte", Character.Age.YOUNG, Character.HairColor.RED, false,
                    HumanCharacter.Gender.WOMAN, HumanCharacter.EyeColor.GREEN, true
            )
        };
    }

    private static Character[] getPetCharacters() {
        return new Character[] {
            new PetCharacter(
                    "Buddy", Character.Age.PUPPY, Character.HairColor.BROWN, false,
                    PetCharacter.Species.DOG, true
            ),
            new PetCharacter(
                    "Whiskers", Character.Age.OLD, Character.HairColor.GRAY, false,
                    PetCharacter.Species.CAT, false
            ),
            new PetCharacter(
                    "Tweety", Character.Age.YOUNG, Character.HairColor.YELLOW, false,
                    PetCharacter.Species.BIRD, false
            ),
            new PetCharacter(
                    "Goldie", Character.Age.YOUNG, Character.HairColor.ORANGE, false,
                    PetCharacter.Species.FISH, true
            ),
            new PetCharacter(
                    "Rex", Character.Age.PUPPY, Character.HairColor.BLACK, true,
                    PetCharacter.Species.DOG, false
            ),
            new PetCharacter(
                    "Fluffy", Character.Age.YOUNG, Character.HairColor.WHITE, false,
                    PetCharacter.Species.CAT, true
            ),
            new PetCharacter(
                    "Polly", Character.Age.MIDDLE, Character.HairColor.GREEN, true,
                    PetCharacter.Species.BIRD, false
            ),
            new PetCharacter(
                    "Fido", Character.Age.OLD, Character.HairColor.BROWN, true,
                    PetCharacter.Species.DOG, false
            ),
            new PetCharacter(
                    "Smokey", Character.Age.OLD, Character.HairColor.GRAY, false,
                    PetCharacter.Species.CAT, true
            ),
            new PetCharacter(
                    "Chirpy", Character.Age.OLD, Character.HairColor.RED, false,
                    PetCharacter.Species.BIRD, true
            ),
            new PetCharacter(
                    "Bubbles", Character.Age.MIDDLE, Character.HairColor.BLUE, false,
                    PetCharacter.Species.FISH, false
            ),
            new PetCharacter(
                    "Max", Character.Age.MIDDLE, Character.HairColor.BLACK, true,
                    PetCharacter.Species.DOG, true
            ),
            new PetCharacter(
                    "Mittens", Character.Age.YOUNG, Character.HairColor.WHITE, false,
                    PetCharacter.Species.CAT, false
            ),
            new PetCharacter(
                    "Piper", Character.Age.YOUNG, Character.HairColor.ORANGE, false,
                    PetCharacter.Species.BIRD, true
            ),
            new PetCharacter(
                    "Cannoli", Character.Age.OLD, Character.HairColor.BROWN, true,
                    PetCharacter.Species.DOG, false
            ),
            new PetCharacter(
                    "Remo", Character.Age.YOUNG, Character.HairColor.YELLOW, true,
                    PetCharacter.Species.FISH, false
            )
        };
    }

    private static void showGameUI(GameServer server, Player player1, Player player2) {
        Game gui1 = new Game(server, player1);
        Game gui2 = new Game(server, player2);

        gui1.setVisible(true);
        gui2.setVisible(true);

        gui1.startGameLoop();
        gui2.startGameLoop();
    }

    private static void initializeGame(boolean isHumanGame) {
        List<Character> characterList;
        if (isHumanGame) {
            characterList = Arrays.asList(getHumanCharacters());
        } else {
            characterList = Arrays.asList(getPetCharacters());
        }

        Player player1 = new Player("Player 1", characterList);
        Player player2 = new Player("Player 2", characterList);
        Player[] players = { player1, player2 };
        List<Player> playerList = Arrays.asList(players);
        GameServer server = new GameServer(12345, playerList, characterList);
        server.setHumanGame(isHumanGame);
        showGameUI(server, player1, player2);
    }

    private ImageIcon loadImage(String filename) {
        try {
            // Load image from resources directory
            File file = new File("src/main/resources/" + filename);
            if (!file.exists()) {
                throw new IOException("File not found: " + filename);
            }
            BufferedImage image = ImageIO.read(file);

            // Resize the image
            Image resizedImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            // Return a placeholder image if loading fails
            return new ImageIcon();
        }
    }

    private void removeCharacterFromGUI(Character character) {
        for (Component component : gameBoardPanel.getComponents()) {
            if (component instanceof JPanel characterPanel) {
                JLabel nameLabel = (JLabel) characterPanel.getComponent(1);
                String characterName = nameLabel.getText();
                if (characterName.equals(character.toString())) {
                    gameBoardPanel.remove(characterPanel);
                    break;
                }
            }
        }
        revalidate();
        repaint();
    }

    private boolean isTraitTrueForSecretCharacter(Question.Trait trait, String value) {
        Character secretCharacter = player.getSecretCharacter();
        if (secretCharacter == null) {
            // Handle null secret character
            return false;
        }
        switch (trait) {
            case AGE -> {
                return (secretCharacter).getAge().equals(
                        Character.Age.valueOf(value.toUpperCase())
                );
            }
            case HAIR_COLOR -> {
                return (secretCharacter).getHairColor().equals(
                        Character.HairColor.valueOf(value.toUpperCase())
                );
            }
            case HAT -> {
                return (secretCharacter).hasHat() == Boolean.parseBoolean(value);
            }
            case GENDER -> {
                if (secretCharacter instanceof HumanCharacter) {
                    return ((HumanCharacter) secretCharacter).getGender().equals(
                            HumanCharacter.Gender.valueOf(value.toUpperCase())
                    );
                }
            }
            case EYE_COLOR -> {
                if (secretCharacter instanceof HumanCharacter) {
                    return ((HumanCharacter) secretCharacter).getEyeColor().equals(
                            HumanCharacter.EyeColor.valueOf(value.toUpperCase())
                    );
                }
            }
            case GLASSES -> {
                if (secretCharacter instanceof HumanCharacter) {
                    return ((HumanCharacter) secretCharacter).hasGlasses() == Boolean
                            .parseBoolean(value);
                }
            }
            case SPECIES -> {
                if (secretCharacter instanceof PetCharacter) {
                    return ((PetCharacter) secretCharacter).getSpecies().equals(
                            PetCharacter.Species.valueOf(value.toUpperCase())
                    );
                }
            }
            case TOY -> {
                if (secretCharacter instanceof PetCharacter) {
                    return ((PetCharacter) secretCharacter).hasToy() == Boolean.parseBoolean(value);
                }
            }
            default -> throw new IllegalArgumentException(
                    "Unexpected value: " + trait
            );
        }
        // Default to false if trait doesn't match/if secret character is not of expected type
        return false;
    }

    private void filterCharactersByTrait(Question question, boolean answer) {
        currentPlayer = server.getCurrentPlayer();
        Iterator<Character> iterator = currentPlayer.getPlayerCharacters().iterator();
        while (iterator.hasNext()) {
            Character character = iterator.next();
            boolean matchesTrait = question.evaluateTrait(character);
            if ((matchesTrait && !answer) || (!matchesTrait && answer)) {
                // Remove character from player's character list
                iterator.remove();
                // Remove character from GUI
                removeCharacterFromGUI(character);
                // Remove character from currentPlayer
                currentPlayer.removePlayerCharacter(character);
            }
        }
        revalidate();
        repaint();
    }

    private void updateUI() {
        if (server.getGameOver()) {
            // Disable buttons and display winner message
            askQuestionButton.setEnabled(false);
            makeGuessButton.setEnabled(false);
            viewOpponentsSecretButton.setEnabled(false);
            Player winner = currentPlayer;
            gameOverLabel.setText(
                    "Game Over! " +
                            (winner != null ? winner + " wins!" : "It's a draw!")
            );
            gameOverLabel.setVisible(true);

            // Set font size and style
            gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
            getContentPane().remove(gameBoardPanel);
            getContentPane().remove(askQuestionButton);
            getContentPane().remove(makeGuessButton);
            getContentPane().add(gameOverLabel, BorderLayout.CENTER);
        } else {
            // Enable or disable buttons based on the current player's turn
            if (server.isPlayer1SecretCharacterChosen() &&
                    server.isPlayer2SecretCharacterChosen()) {
                askQuestionButton.setEnabled(currentPlayer.equals(player));
                makeGuessButton.setEnabled(currentPlayer.equals(player));
            } else {
                // Disable buttons if both players haven't chosen their secret characters
                askQuestionButton.setEnabled(false);
                makeGuessButton.setEnabled(false);
            }
        }
    }

    // Update the startGameLoop method to check for a winner after each turn
    private void startGameLoop() {
        SwingUtilities.invokeLater(() -> {
            if (!server.getGameOver()) {
                currentPlayer = server.getCurrentPlayer();
                updateUI(); // Update UI based on the current player
                startGameLoop();
            } else {
                // End the game if it's over
                updateUI();
            }
        });
    }

}
