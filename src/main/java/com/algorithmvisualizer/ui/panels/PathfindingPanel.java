package com.algorithmvisualizer.ui.panels;

import com.algorithmvisualizer.algorithms.PathfindingAlgorithm;
import com.algorithmvisualizer.algorithms.pathfinding.*;
import com.algorithmvisualizer.models.pathfinding.GridCell;
import com.algorithmvisualizer.models.pathfinding.PathfindingGrid;
import com.algorithmvisualizer.ui.PathfindingCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Panel for pathfinding algorithm visualizations.
 * Features interactive grid with multiple pathfinding algorithms.
 */
public class PathfindingPanel extends JPanel {
    
    private PathfindingCanvas canvas;
    private JComboBox<PathfindingAlgorithm> algorithmComboBox;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton clearButton;
    private JButton mazeButton;
    private JButton setStartButton;
    private JButton setEndButton;
    private JSlider speedSlider;
    private JSlider gridSizeSlider;
    private JLabel statusLabel;
    private JLabel algorithmInfoLabel;
    
    private PathfindingAlgorithm currentAlgorithm;
    private SwingWorker<List<GridCell>, PathfindingGrid> pathfindingWorker;
    
    public PathfindingPanel() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateAlgorithmInfo();
    }
    
    private void initializeComponents() {
        canvas = new PathfindingCanvas(25, 40); // 25x40 grid
        
        // Algorithm selection
        algorithmComboBox = new JComboBox<>(new PathfindingAlgorithm[] {
            new AStarAlgorithm(),
            new DijkstraAlgorithm(),
            new BreadthFirstSearchAlgorithm()
        });
        
        // Control buttons
        startButton = new JButton("Find Path");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        clearButton = new JButton("Clear All");
        mazeButton = new JButton("Generate Maze");
        setStartButton = new JButton("Set Start");
        setEndButton = new JButton("Set End");
        
        pauseButton.setEnabled(false);
        
        // Speed control
        speedSlider = new JSlider(1, 10, 5);
        speedSlider.setMajorTickSpacing(3);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        
        // Grid size control
        gridSizeSlider = new JSlider(10, 50, 25);
        gridSizeSlider.setMajorTickSpacing(20);
        gridSizeSlider.setPaintTicks(true);
        gridSizeSlider.setPaintLabels(true);
        
        statusLabel = new JLabel("Ready to find path");
        algorithmInfoLabel = new JLabel();
        
        currentAlgorithm = (PathfindingAlgorithm) algorithmComboBox.getSelectedItem();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Control panel at the top
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Canvas with scroll pane in the center
        JScrollPane scrollPane = new JScrollPane(canvas);
        scrollPane.setPreferredSize(new Dimension(800, 500));
        add(scrollPane, BorderLayout.CENTER);
        
        // Status panel at the bottom
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Pathfinding Controls"));
        
        // First row - Algorithm and main controls
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Algorithm:"));
        row1.add(algorithmComboBox);
        row1.add(Box.createHorizontalStrut(20));
        row1.add(startButton);
        row1.add(pauseButton);
        row1.add(resetButton);
        row1.add(clearButton);
        
        // Second row - Grid controls
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(setStartButton);
        row2.add(setEndButton);
        row2.add(mazeButton);
        row2.add(Box.createHorizontalStrut(20));
        row2.add(new JLabel("Speed:"));
        row2.add(speedSlider);
        
        // Third row - Algorithm info
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(algorithmInfoLabel);
        
        panel.add(row1);
        panel.add(row2);
        panel.add(row3);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(statusLabel, BorderLayout.WEST);
        
        // Instructions
        JLabel instructions = new JLabel("Left-click: Toggle walls | Right-click: Drag to draw walls");
        instructions.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        instructions.setFont(instructions.getFont().deriveFont(Font.ITALIC));
        panel.add(instructions, BorderLayout.EAST);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        startButton.addActionListener(this::startPathfinding);
        pauseButton.addActionListener(this::pausePathfinding);
        resetButton.addActionListener(this::resetVisualization);
        clearButton.addActionListener(this::clearAll);
        mazeButton.addActionListener(this::generateMaze);
        setStartButton.addActionListener(this::setStart);
        setEndButton.addActionListener(this::setEnd);
        
        speedSlider.addChangeListener(e -> updateAnimationSpeed());
        
        algorithmComboBox.addActionListener(e -> {
            currentAlgorithm = (PathfindingAlgorithm) algorithmComboBox.getSelectedItem();
            updateAlgorithmInfo();
            updateStatus("Selected: " + currentAlgorithm.getName());
        });
    }
    
    private void startPathfinding(ActionEvent e) {
        if (pathfindingWorker != null && !pathfindingWorker.isDone()) {
            return; // Already running
        }
        
        PathfindingGrid grid = canvas.getGrid();
        if (grid.getStartCell() == null || grid.getEndCell() == null) {
            updateStatus("Please set both start and end positions!");
            return;
        }
        
        // Reset the grid for new search
        canvas.clearPath();
        
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        
        updateStatus("Finding path with " + currentAlgorithm.getName() + "...");
        
        pathfindingWorker = new SwingWorker<List<GridCell>, PathfindingGrid>() {
            @Override
            protected List<GridCell> doInBackground() throws Exception {
                return currentAlgorithm.findPath(grid, (g, step, cell) -> {
                    if (isCancelled()) return;
                    
                    // Publish update for UI
                    publish(g);
                    
                    // Update status on EDT
                    SwingUtilities.invokeLater(() -> {
                        canvas.setStatusText(step);
                        updateStatus(step);
                    });
                    
                    try {
                        Thread.sleep(getAnimationDelay());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                });
            }
            
            @Override
            protected void process(List<PathfindingGrid> chunks) {
                if (!chunks.isEmpty()) {
                    canvas.repaint();
                }
            }
            
            @Override
            protected void done() {
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                
                try {
                    List<GridCell> path = get();
                    if (path.isEmpty()) {
                        updateStatus("No path found!");
                    } else {
                        updateStatus("Path found! Length: " + path.size() + 
                            (currentAlgorithm.isOptimal() ? " (optimal)" : ""));
                        canvas.highlightPath(path);
                    }
                } catch (Exception ex) {
                    if (!isCancelled()) {
                        updateStatus("Pathfinding interrupted");
                    }
                }
            }
        };
        
        pathfindingWorker.execute();
    }
    
    private void pausePathfinding(ActionEvent e) {
        if (pathfindingWorker != null && !pathfindingWorker.isDone()) {
            pathfindingWorker.cancel(true);
            updateStatus("Pathfinding paused");
            
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        }
    }
    
    private void resetVisualization(ActionEvent e) {
        if (pathfindingWorker != null && !pathfindingWorker.isDone()) {
            pathfindingWorker.cancel(true);
        }
        
        canvas.resetGrid();
        updateStatus("Grid reset - ready to find path");
        
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }
    
    private void clearAll(ActionEvent e) {
        if (pathfindingWorker != null && !pathfindingWorker.isDone()) {
            pathfindingWorker.cancel(true);
        }
        
        canvas.clearAll();
        updateStatus("Grid cleared");
        
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }
    
    private void generateMaze(ActionEvent e) {
        if (pathfindingWorker != null && !pathfindingWorker.isDone()) {
            return; // Don't generate maze while pathfinding
        }
        
        canvas.generateMaze(0.3); // 30% wall probability
        updateStatus("Random maze generated");
    }
    
    private void setStart(ActionEvent e) {
        canvas.setSettingStart(true);
        updateStatus("Click on the grid to set start position");
    }
    
    private void setEnd(ActionEvent e) {
        canvas.setSettingEnd(true);
        updateStatus("Click on the grid to set end position");
    }
    
    private void updateAnimationSpeed() {
        // Animation delay is handled in getAnimationDelay()
    }
    
    private int getAnimationDelay() {
        int speed = speedSlider.getValue();
        return Math.max(50, 1100 - (speed * 100)); // 50ms to 1000ms
    }
    
    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
        });
    }
    
    private void updateAlgorithmInfo() {
        if (currentAlgorithm != null) {
            String info = String.format("%s - Time: %s, Space: %s, Optimal: %s", 
                currentAlgorithm.getName(),
                currentAlgorithm.getTimeComplexity(),
                currentAlgorithm.getSpaceComplexity(),
                currentAlgorithm.isOptimal() ? "Yes" : "No");
            algorithmInfoLabel.setText(info);
        }
    }
    
    public void reset() {
        resetVisualization(null);
    }
}