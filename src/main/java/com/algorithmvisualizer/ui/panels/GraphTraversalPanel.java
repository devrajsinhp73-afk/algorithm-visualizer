package com.algorithmvisualizer.ui.panels;

import com.algorithmvisualizer.algorithms.GraphTraversalAlgorithm;
import com.algorithmvisualizer.algorithms.graph.DepthFirstSearchAlgorithm;
import com.algorithmvisualizer.algorithms.graph.BreadthFirstSearchAlgorithm;
import com.algorithmvisualizer.algorithms.graph.TopologicalSortAlgorithm;
import com.algorithmvisualizer.models.graph.Graph;
import com.algorithmvisualizer.models.graph.GraphNode;
import com.algorithmvisualizer.ui.components.GraphCanvas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Panel for graph traversal algorithm visualization.
 * Provides controls for DFS, BFS, and Topological Sort algorithms.
 */
public class GraphTraversalPanel extends JPanel {
    
    private GraphCanvas canvas;
    private Graph graph;
    private JComboBox<GraphTraversalAlgorithm> algorithmCombo;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton clearGraphButton;
    private JCheckBox directedGraphCheck;
    private JSlider speedSlider;
    private JTextArea logArea;
    private JLabel statusLabel;
    private JLabel statsLabel;
    
    private SwingWorker<List<GraphNode>, String> currentWorker;
    private boolean isPaused = false;
    private GraphNode selectedStartNode = null;
    private final Object pauseLock = new Object();
    
    // Mode buttons
    private JButton addNodeButton;
    private JButton addEdgeButton;
    private JButton selectButton;
    private JButton deleteButton;
    
    public GraphTraversalPanel() {
        initializeComponents();
        setupLayout();
        
        // Create initial graph after layout is complete
        graph = new Graph(false); // Start with undirected graph
        canvas.setGraph(graph);
        
        setupEventHandlers();
        updateUI();
    }
    
    private void initializeComponents() {
        canvas = new GraphCanvas();
        
        // Algorithm selection
        algorithmCombo = new JComboBox<>(new GraphTraversalAlgorithm[]{
            new DepthFirstSearchAlgorithm(true),   // Recursive DFS
            new DepthFirstSearchAlgorithm(false),  // Iterative DFS
            new BreadthFirstSearchAlgorithm(),
            new TopologicalSortAlgorithm(false),   // DFS-based topological sort
            new TopologicalSortAlgorithm(true)     // Kahn's algorithm
        });
        
        // Control buttons
        startButton = new JButton("Start Traversal");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        clearGraphButton = new JButton("Clear Graph");
        
        // Graph interaction mode buttons
        addNodeButton = new JButton("Add Node");
        addEdgeButton = new JButton("Add Edge");
        selectButton = new JButton("Select/Move");
        deleteButton = new JButton("Delete");
        
        // Set initial mode (start with ADD_NODE for easier getting started)
        canvas.setInteractionMode(GraphCanvas.InteractionMode.ADD_NODE);
        addNodeButton.setBackground(Color.LIGHT_GRAY);
        
        // Add action listeners for mode buttons
        addNodeButton.addActionListener(e -> {
            canvas.setInteractionMode(GraphCanvas.InteractionMode.ADD_NODE);
            resetButtonColors();
            addNodeButton.setBackground(Color.LIGHT_GRAY);
            showStatus("Click on empty space to add nodes", Color.BLUE);
        });
        
        addEdgeButton.addActionListener(e -> {
            canvas.setInteractionMode(GraphCanvas.InteractionMode.ADD_EDGE);
            resetButtonColors();
            addEdgeButton.setBackground(Color.LIGHT_GRAY);
            showStatus("Click on two nodes to connect them", Color.BLUE);
        });
        
        selectButton.addActionListener(e -> {
            canvas.setInteractionMode(GraphCanvas.InteractionMode.SELECT);
            resetButtonColors();
            selectButton.setBackground(Color.LIGHT_GRAY);
            showStatus("Click and drag to select/move nodes", Color.BLUE);
        });
        
        deleteButton.addActionListener(e -> {
            canvas.setInteractionMode(GraphCanvas.InteractionMode.DELETE);
            resetButtonColors();
            deleteButton.setBackground(Color.LIGHT_GRAY);
            showStatus("Click on nodes or edges to delete them", Color.RED);
        });
        
        // Graph options
        directedGraphCheck = new JCheckBox("Directed Graph", false);
        
        // Speed control
        speedSlider = new JSlider(1, 10, 5);
        speedSlider.setMajorTickSpacing(3);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        
        // Status and logging
        statusLabel = new JLabel("Click 'Add Node' then click on canvas to create nodes");
        statsLabel = new JLabel("Nodes: 0, Edges: 0");
        
        logArea = new JTextArea(8, 30);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setBackground(getBackground());
        
        // Initial state
        pauseButton.setEnabled(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top control panel
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        // Algorithm and graph controls
        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.setBorder(new TitledBorder("Algorithm Selection"));
        algorithmPanel.add(new JLabel("Algorithm:"));
        algorithmPanel.add(algorithmCombo);
        algorithmPanel.add(Box.createHorizontalStrut(20));
        algorithmPanel.add(directedGraphCheck);
        
        // Graph interaction controls  
        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBorder(new TitledBorder("Graph Editing"));
        interactionPanel.add(addNodeButton);
        interactionPanel.add(addEdgeButton);
        interactionPanel.add(selectButton);
        interactionPanel.add(deleteButton);
        
        // Execution controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(new TitledBorder("Execution Controls"));
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resetButton);
        controlPanel.add(clearGraphButton);
        
        // Combine panels in a grid
        JPanel combinedPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        combinedPanel.add(algorithmPanel);
        combinedPanel.add(interactionPanel);
        combinedPanel.add(controlPanel);
        
        topPanel.add(combinedPanel, BorderLayout.CENTER);
        
        // Speed control panel
        JPanel speedPanel = new JPanel(new BorderLayout());
        speedPanel.setBorder(new TitledBorder("Animation Speed"));
        speedPanel.add(new JLabel("Slow"), BorderLayout.WEST);
        speedPanel.add(speedSlider, BorderLayout.CENTER);
        speedPanel.add(new JLabel("Fast"), BorderLayout.EAST);
        
        // Right panel with speed and log
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setPreferredSize(new Dimension(300, 0));
        
        rightPanel.add(speedPanel, BorderLayout.NORTH);
        
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(new TitledBorder("Algorithm Steps"));
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(logScrollPane, BorderLayout.CENTER);
        
        // Bottom status panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(statsLabel, BorderLayout.EAST);
        
        // Main layout
        add(topPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        startButton.addActionListener(e -> startTraversal());
        pauseButton.addActionListener(e -> pauseTraversal());
        resetButton.addActionListener(e -> resetVisualization());
        clearGraphButton.addActionListener(e -> clearGraph());
        
        directedGraphCheck.addActionListener(e -> toggleGraphDirection());
        
        // Canvas event handlers
        canvas.addGraphChangeListener(this::updateStats);
        canvas.addNodeSelectionListener(this::onNodeSelected);
        
        algorithmCombo.addActionListener(e -> updateAlgorithmInfo());
    }
    
    private void startTraversal() {
        if (graph.getNodeCollection().isEmpty()) {
            showStatus("Please add some nodes to the graph first!", Color.RED);
            return;
        }
        
        GraphTraversalAlgorithm algorithm = (GraphTraversalAlgorithm) algorithmCombo.getSelectedItem();
        
        // For topological sort, check if graph is directed
        if (algorithm instanceof TopologicalSortAlgorithm && !graph.isDirected()) {
            showStatus("Topological sort requires a directed graph!", Color.RED);
            return;
        }
        
        // Get start node (use selected node or first node)
        GraphNode startNode = selectedStartNode != null ? selectedStartNode : 
                             graph.getNodeCollection().iterator().next();
        
        logArea.setText("");
        showStatus("Running " + algorithm.getName() + "...", Color.BLUE);
        
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        resetButton.setEnabled(false);
        isPaused = false;
        
        // Create and start worker
        currentWorker = new SwingWorker<List<GraphNode>, String>() {
            @Override
            protected List<GraphNode> doInBackground() throws Exception {
                return algorithm.traverse(graph, startNode, new GraphTraversalAlgorithm.GraphTraversalCallback() {
                    @Override
                    public void onStep(Graph graph, String message, GraphNode currentNode, List<GraphNode> visitedNodes) {
                        if (isCancelled()) return;
                        
                        // Publish the step for UI update
                        publish(message);
                        
                        // Update canvas on EDT
                        SwingUtilities.invokeLater(() -> {
                            canvas.repaint();
                        });
                        
                        // Wait based on speed slider (but check for pause/cancel)
                        int delay = (11 - speedSlider.getValue()) * 200;
                        try {
                            Thread.sleep(delay);
                            
                            // Handle pause
                            synchronized (pauseLock) {
                                while (isPaused && !isCancelled()) {
                                    pauseLock.wait();
                                }
                            }
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                });
            }
            
            @Override
            protected void process(List<String> chunks) {
                // Update log with latest messages
                for (String message : chunks) {
                    logArea.append(message + "\n");
                }
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }
            
            @Override
            protected void done() {
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                resetButton.setEnabled(true);
                
                try {
                    List<GraphNode> result = get();
                    if (result != null && !result.isEmpty()) {
                        showStatus("Traversal completed! Visited " + result.size() + " nodes.", Color.GREEN);
                    } else {
                        showStatus("Traversal failed or was cancelled.", Color.RED);
                    }
                } catch (Exception ex) {
                    showStatus("Error during traversal: " + ex.getMessage(), Color.RED);
                    ex.printStackTrace();
                }
            }
        };
        
        currentWorker.execute();
    }
    
    private void pauseTraversal() {
        if (currentWorker != null && !currentWorker.isDone()) {
            isPaused = !isPaused;
            
            if (isPaused) {
                pauseButton.setText("Resume");
                showStatus("Traversal paused", Color.ORANGE);
            } else {
                pauseButton.setText("Pause");
                showStatus("Traversal resumed", Color.BLUE);
                
                // Notify the worker to continue using the correct lock
                synchronized (pauseLock) {
                    pauseLock.notifyAll();
                }
            }
        }
    }
    
    private void resetVisualization() {
        if (currentWorker != null) {
            currentWorker.cancel(true);
        }
        
        // Reset all visual states
        graph.resetVisualization();
        canvas.repaint();
        
        logArea.setText("");
        showStatus("Visualization reset", Color.BLACK);
        
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        pauseButton.setText("Pause");
        resetButton.setEnabled(true);
        isPaused = false;
    }
    
    private void clearGraph() {
        resetVisualization();
        graph.clear();
        canvas.repaint();
        selectedStartNode = null;
        updateStats();
        showStatus("Graph cleared", Color.BLACK);
    }
    
    private void toggleGraphDirection() {
        boolean directed = directedGraphCheck.isSelected();
        graph.setDirected(directed);
        canvas.repaint();
        showStatus("Graph is now " + (directed ? "directed" : "undirected"), Color.BLACK);
        updateAlgorithmInfo();
    }
    
    private void onNodeSelected(GraphNode node) {
        selectedStartNode = node;
        if (node != null) {
            showStatus("Selected start node: " + node.getId(), Color.BLUE);
        }
    }
    
    private void updateStats() {
        if (graph != null) {
            statsLabel.setText(String.format("Nodes: %d, Edges: %d", 
                graph.getNodeCollection().size(), graph.getEdges().size()));
        } else {
            statsLabel.setText("Nodes: 0, Edges: 0");
        }
    }
    
    private void updateAlgorithmInfo() {
        GraphTraversalAlgorithm algorithm = (GraphTraversalAlgorithm) algorithmCombo.getSelectedItem();
        if (algorithm != null) {
            String info = String.format("<html><b>%s</b><br/>Time: %s, Space: %s<br/>%s</html>",
                algorithm.getName(),
                algorithm.getTimeComplexity(),
                algorithm.getSpaceComplexity(),
                algorithm.getDescription()
            );
            algorithmCombo.setToolTipText(info);
        }
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
    private void resetButtonColors() {
        addNodeButton.setBackground(null);
        addEdgeButton.setBackground(null);
        selectButton.setBackground(null);
        deleteButton.setBackground(null);
    }
    
    public void updateUI() {
        if (statsLabel != null) {
            updateStats();
        }
        if (algorithmCombo != null) {
            updateAlgorithmInfo();
        }
    }
    
    public void reset() {
        resetVisualization();
    }
}