package com.algorithmvisualizer;

import com.algorithmvisualizer.ui.MainWindow;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main application class for the Algorithm Visualizer.
 * This application demonstrates various algorithms through interactive visualizations.
 * 
 * @author devrajsinhp73-afk
 * @version 1.0.0
 */
public class AlgorithmVisualizerApp {
    
    public static void main(String[] args) {
        // Set up the modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf, using default L&F");
        }
        
        // Create and display the main window on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to start Algorithm Visualizer: " + e.getMessage());
                System.exit(1);
            }
        });
    }
}