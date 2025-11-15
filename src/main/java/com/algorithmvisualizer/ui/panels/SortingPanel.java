package com.algorithmvisualizer.ui.panels;

import com.algorithmvisualizer.algorithms.SortingAlgorithm;
import com.algorithmvisualizer.algorithms.sorting.*;
import com.algorithmvisualizer.models.VisualizationElement;
import com.algorithmvisualizer.ui.VisualizationCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Panel for sorting algorithm visualizations.
 */
public class SortingPanel extends JPanel {
    
    private VisualizationCanvas canvas;
    private JComboBox<SortingAlgorithm> algorithmComboBox;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton shuffleButton;
    private JSlider speedSlider;
    private JSlider sizeSlider;
    private JLabel statusLabel;
    
    private List<VisualizationElement> array;
    private SortingAlgorithm currentAlgorithm;
    private SwingWorker<Void, List<VisualizationElement>> sortingWorker;
    private boolean isPaused;
    
    public SortingPanel() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        generateRandomArray();
    }
    
    private void initializeComponents() {
        canvas = new VisualizationCanvas();
        
        // Algorithm selection
        algorithmComboBox = new JComboBox<>(new SortingAlgorithm[] {
            new BubbleSort(),
            new QuickSort(),
            new MergeSort(),
            new HeapSort()
        });
        
        // Control buttons
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        shuffleButton = new JButton("Shuffle");
        
        pauseButton.setEnabled(false);
        
        // Speed control
        speedSlider = new JSlider(1, 10, 5);
        speedSlider.setMajorTickSpacing(3);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        
        // Size control
        sizeSlider = new JSlider(10, 100, 30);
        sizeSlider.setMajorTickSpacing(30);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        
        statusLabel = new JLabel("Ready to sort");
        
        array = new ArrayList<>();
        isPaused = false;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Control panel at the top
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Canvas in the center
        add(canvas, BorderLayout.CENTER);
        
        // Status at the bottom
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Sorting Controls"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Algorithm selection
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Algorithm:"), gbc);
        gbc.gridx = 1;
        panel.add(algorithmComboBox, gbc);
        
        // Control buttons
        gbc.gridx = 2;
        panel.add(startButton, gbc);
        gbc.gridx = 3;
        panel.add(pauseButton, gbc);
        gbc.gridx = 4;
        panel.add(resetButton, gbc);
        gbc.gridx = 5;
        panel.add(shuffleButton, gbc);
        
        // Speed control
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Speed:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(speedSlider, gbc);
        
        // Size control
        gbc.gridx = 3; gbc.gridwidth = 1;
        panel.add(new JLabel("Size:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2;
        panel.add(sizeSlider, gbc);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        startButton.addActionListener(this::startSorting);
        pauseButton.addActionListener(this::pauseSorting);
        resetButton.addActionListener(this::resetVisualization);
        shuffleButton.addActionListener(this::shuffleArray);
        
        speedSlider.addChangeListener(e -> updateAnimationSpeed());
        sizeSlider.addChangeListener(e -> {
            if (!sizeSlider.getValueIsAdjusting()) {
                generateRandomArray();
            }
        });
        
        algorithmComboBox.addActionListener(e -> {
            currentAlgorithm = (SortingAlgorithm) algorithmComboBox.getSelectedItem();
            updateStatus("Selected: " + currentAlgorithm.getName());
        });
    }
    
    private void startSorting(ActionEvent e) {
        if (sortingWorker != null && !sortingWorker.isDone()) {
            return; // Already sorting
        }
        
        currentAlgorithm = (SortingAlgorithm) algorithmComboBox.getSelectedItem();
        
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        shuffleButton.setEnabled(false);
        sizeSlider.setEnabled(false);
        
        updateStatus("Sorting with " + currentAlgorithm.getName() + "...");
        
        sortingWorker = new SwingWorker<Void, List<VisualizationElement>>() {
            @Override
            protected Void doInBackground() throws Exception {
                currentAlgorithm.sort(array.toArray(new VisualizationElement[0]), (arr, step) -> {
                    if (isCancelled()) return;
                    
                    // Create a copy for thread safety
                    List<VisualizationElement> snapshot = new ArrayList<>();
                    for (VisualizationElement elem : arr) {
                        VisualizationElement copy = new VisualizationElement(elem.getValue());
                        copy.setColor(elem.getColor());
                        copy.setHighlighted(elem.isHighlighted());
                        copy.setSelected(elem.isSelected());
                        snapshot.add(copy);
                    }
                    
                    publish(snapshot);
                    
                    try {
                        Thread.sleep(getAnimationDelay());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                });
                return null;
            }
            
            @Override
            protected void process(List<List<VisualizationElement>> chunks) {
                if (!chunks.isEmpty()) {
                    List<VisualizationElement> latestState = chunks.get(chunks.size() - 1);
                    canvas.setElements(latestState);
                }
            }
            
            @Override
            protected void done() {
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                shuffleButton.setEnabled(true);
                sizeSlider.setEnabled(true);
                
                try {
                    get(); // Check for exceptions
                    updateStatus("Sorting completed!");
                    highlightSortedArray();
                } catch (Exception ex) {
                    if (!isCancelled()) {
                        updateStatus("Sorting interrupted");
                    }
                }
            }
        };
        
        sortingWorker.execute();
    }
    
    private void pauseSorting(ActionEvent e) {
        if (sortingWorker != null && !sortingWorker.isDone()) {
            sortingWorker.cancel(true);
            updateStatus("Sorting paused");
            
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            shuffleButton.setEnabled(true);
            sizeSlider.setEnabled(true);
        }
    }
    
    private void resetVisualization(ActionEvent e) {
        if (sortingWorker != null && !sortingWorker.isDone()) {
            sortingWorker.cancel(true);
        }
        
        generateRandomArray();
        updateStatus("Reset to random array");
        
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        shuffleButton.setEnabled(true);
        sizeSlider.setEnabled(true);
    }
    
    private void shuffleArray(ActionEvent e) {
        if (sortingWorker != null && !sortingWorker.isDone()) {
            return; // Don't shuffle while sorting
        }
        
        Collections.shuffle(array);
        resetElementColors();
        canvas.setElements(array);
        updateStatus("Array shuffled");
    }
    
    private void generateRandomArray() {
        array.clear();
        Random random = new Random();
        int size = sizeSlider.getValue();
        
        for (int i = 0; i < size; i++) {
            int value = random.nextInt(size) + 1;
            VisualizationElement element = new VisualizationElement(value);
            element.setColor(Color.LIGHT_GRAY);
            array.add(element);
        }
        
        canvas.setElements(array);
        updateStatus("Generated random array of size " + size);
    }
    
    private void resetElementColors() {
        for (VisualizationElement element : array) {
            element.setColor(Color.LIGHT_GRAY);
            element.setHighlighted(false);
            element.setSelected(false);
        }
    }
    
    private void highlightSortedArray() {
        for (VisualizationElement element : array) {
            element.setColor(Color.GREEN);
        }
        canvas.repaint();
    }
    
    private void updateAnimationSpeed() {
        int speed = speedSlider.getValue();
        int delay = 1100 - (speed * 100); // Convert to delay (1000ms to 100ms)
        canvas.setAnimationDelay(delay);
    }
    
    private int getAnimationDelay() {
        int speed = speedSlider.getValue();
        return 1100 - (speed * 100); // Convert to delay
    }
    
    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            canvas.setStatusText(message);
        });
    }
    
    public void reset() {
        resetVisualization(null);
    }
}