package org.cis1200.guessWho;

import javax.swing.*;
import java.awt.*;

public class InstructionsWindow extends JFrame {
    public InstructionsWindow() {
        setTitle("Game Instructions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 800);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());

        JScrollPane scrollPane = getjScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            dispose(); // Close the instructions window
        });
        add(closeButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private static JScrollPane getjScrollPane() {
        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setText(
                "Instructions:\n" +
                        "1. Choose whether to play with human or pet characters.\n" +
                        "2. Each player must select a secret character from the drop-down menu " +
                        "that the other player must guess.\n" +
                        "   2.1 You cannot begin until both players select a secret character.\n" +
                        "   2.2 If you forget the character you chose, press the 'Reveal Secret " +
                        "Character' button to see." +
                        "3. Take turns guessing traits, starting with Player 1:\n" +
                        "   3.1 Select a trait from the drop-down menu.\n" +
                        "   3.2 Enter a valid trait to guess.\n" +
                        "Note: traits and valid responses are detailed below.\n" +
                        "4. Once you feel confident, take a guess as to which character " +
                        "you think the other player chose.\n" +
                        "5. The first player to correctly guess the secret character " +
                        "chosen by the other player wins.\n " +
                        "Have fun!\n\n" +
                        "Traits:\n" +
                        "Age: guess the relative age of the secret character\n" +
                        "   Responses:\n" +
                        "       Young\n" +
                        "       Middle\n" +
                        "       Old\n" +
                        "       Puppy (in pet game only)\n\n" +
                        "Hair Color: guess the hair/fur/body color of the secret character\n" +
                        "   Responses: \n" +
                        "       BROWN\n" +
                        "       BLACK\n" +
                        "       BLOND\n" +
                        "       GRAY\n" +
                        "       RED\n" +
                        "       ORANGE\n" +
                        "       YELLOW\n" +
                        "       GREEN\n" +
                        "       WHITE\n" +
                        "       BLUE\n" +
                        "   (Note: the latter colors are more relevant for a pet game)\n\n" +
                        "Hat: guess if the secret character is wearing a hat\n" +
                        "   Respond with 'true' if you believe the character is wearing a hat.\n" +
                        "   Respond with 'false' otherwise.\n\n" +
                        "(for human games only)\n" +
                        "Gender: guess the gender of the secret character\n" +
                        "   Responses:\n" +
                        "       Man\n" +
                        "       Woman\n\n" +
                        "Eye Color: guess the eye color of the secret character\n" +
                        "   Responses:\n" +
                        "       BROWN\n" +
                        "       HAZEL\n" +
                        "       GREEN\n" +
                        "       BLUE\n\n" +
                        "Glasses: guess if the secret character is wearing glasses\n" +
                        "   Respond with 'true' if you believe the character is wearing glasses.\n"
                        +
                        "   Respond with 'false' otherwise.\n\n" +
                        "(for pet games only)\n" +
                        "Species: guess the species of the secret character\n" +
                        "   Responses:\n" +
                        "       DOG\n" +
                        "       CAT\n" +
                        "       BIRD\n" +
                        "       FISH\n\n" +
                        "Toy: guess if the secret character has a toy\n" +
                        "   Respond with 'true' if you believe the character has a toy.\n" +
                        "   Respond with 'false' otherwise.\n"
        );

        return new JScrollPane(instructionsArea);
    }
}
