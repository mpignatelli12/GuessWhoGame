package org.cis1200.guessWho;

import javax.swing.*;
import java.awt.*;

public class LoadScreen extends JFrame {

    public LoadScreen(GameTypeSelectionCallback callback) {
        setTitle("Game Type Selection");
        setSize(300, 325);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon originalImageIcon = new ImageIcon("src/main/resources/Title.png");

        // Rescale the image
        Image scaledImage = originalImageIcon.getImage()
                .getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        // Create a scaled ImageIcon
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        // Create a JLabel to display the scaled image
        JLabel backgroundLabel = new JLabel(scaledImageIcon);
        backgroundLabel.setLayout(new BorderLayout()); // Set layout to BorderLayout

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton humanButton = new JButton("Play Human Game");
        JButton petButton = new JButton("Play Pet Game");
        buttonPanel.add(humanButton);
        buttonPanel.add(petButton);
        backgroundLabel.add(buttonPanel, BorderLayout.SOUTH);

        // Set the background label as the content pane
        setContentPane(backgroundLabel);

        humanButton.addActionListener(e -> {
            // Proceed with human game setup
            dispose(); // Close the load screen
            // Start the game with human players
            if (callback != null) {
                callback.onGameTypeSelected(true); // Notify callback that the game type is human
            }
        });

        petButton.addActionListener(e -> {
            // Proceed with pet game setup
            dispose(); // Close the load screen
            // Start the game with pet players
            if (callback != null) {
                callback.onGameTypeSelected(false); // Notify callback that the game type is pet
            }
        });

        setVisible(true);
    }

    // Interface for callback
    public interface GameTypeSelectionCallback {
        void onGameTypeSelected(boolean humanGame);
    }

}
