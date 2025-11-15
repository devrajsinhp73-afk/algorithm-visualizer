package com.algorithmvisualizer.ui;

import com.algorithmvisualizer.models.AlgorithmType;
import com.algorithmvisualizer.ui.panels.SortingPanel;
import com.algorithmvisualizer.ui.panels.PathfindingPanel;
import com.algorithmvisualizer.ui.panels.GraphTraversalPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main window of the Algorithm Visualizer application.
 * Provides a tabbed interface for different types of algorithm visualizations.
 */
public class MainWindow extends JFrame {
    
    private static final String APP_TITLE = "Algorithm Visualizer";
    private static final Dimension DEFAULT_SIZE = new Dimension(1200, 800);
    private static final Dimension MIN_SIZE = new Dimension(800, 600);
    
    private JTabbedPane tabbedPane;
    private SortingPanel sortingPanel;
    private PathfindingPanel pathfindingPanel;
    private GraphTraversalPanel graphTraversalPanel;
    
    public MainWindow() {
        initializeWindow();
        createComponents();
        setupLayout();
        setupMenuBar();
    }
    
    private void initializeWindow() {
        setTitle(APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_SIZE);
        setMinimumSize(MIN_SIZE);
        setLocationRelativeTo(null);
        
        // Set application icon
        try {
            // setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        } catch (Exception e) {
            // Icon not found, use default
        }
    }
    
    private void createComponents() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        
        // Create panels for each algorithm type
        sortingPanel = new SortingPanel();
        pathfindingPanel = new PathfindingPanel();
        graphTraversalPanel = new GraphTraversalPanel();
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Sorting", createTabIcon("🔢"), sortingPanel, "Visualize sorting algorithms");
        tabbedPane.addTab("Pathfinding", createTabIcon("🗺️"), pathfindingPanel, "Visualize pathfinding algorithms");
        tabbedPane.addTab("Graph Traversal", createTabIcon("🕸️"), graphTraversalPanel, "Visualize graph traversal algorithms");
        
        // Set tab properties
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add status bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        JLabel memoryLabel = new JLabel();
        memoryLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        statusBar.add(memoryLabel, BorderLayout.EAST);
        
        // Update memory usage every second
        Timer memoryTimer = new Timer(1000, e -> {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            memoryLabel.setText(String.format("Memory: %d MB / %d MB", 
                usedMemory / (1024 * 1024), 
                totalMemory / (1024 * 1024)));
        });
        memoryTimer.start();
        
        return statusBar;
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createMenuItem("New", "Ctrl+N", this::newVisualization));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", "Ctrl+Q", e -> System.exit(0)));
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.add(createMenuItem("Reset Zoom", "Ctrl+0", this::resetZoom));
        viewMenu.add(createMenuItem("Fit to Window", "Ctrl+F", this::fitToWindow));
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(createMenuItem("About", "F1", this::showAbout));
        helpMenu.add(createMenuItem("GitHub Repository", null, this::openGitHub));
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JMenuItem createMenuItem(String text, String accelerator, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        if (accelerator != null) {
            item.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        }
        item.addActionListener(action);
        return item;
    }
    
    private Icon createTabIcon(String emoji) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(emoji);
                int textHeight = fm.getHeight();
                g2.drawString(emoji, x + (getIconWidth() - textWidth) / 2, y + (getIconHeight() + textHeight) / 2 - 2);
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() { return 20; }
            
            @Override
            public int getIconHeight() { return 16; }
        };
    }
    
    // Menu action methods
    private void newVisualization(ActionEvent e) {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent instanceof SortingPanel) {
            ((SortingPanel) selectedComponent).reset();
        } else if (selectedComponent instanceof PathfindingPanel) {
            ((PathfindingPanel) selectedComponent).reset();
        } else if (selectedComponent instanceof GraphTraversalPanel) {
            ((GraphTraversalPanel) selectedComponent).reset();
        }
    }
    
    private void resetZoom(ActionEvent e) {
        // TODO: Implement zoom reset
    }
    
    private void fitToWindow(ActionEvent e) {
        // TODO: Implement fit to window
    }
    
    private void showAbout(ActionEvent e) {
        String message = """
            Algorithm Visualizer v1.0.0
            
            A Java application for visualizing algorithms including:
            • Sorting algorithms (Bubble, Quick, Merge, Heap)
            • Pathfinding algorithms (A*, Dijkstra, BFS)
            • Graph traversal algorithms (DFS, BFS)
            
            Built with Java Swing for educational purposes.
            
            © devrajsinhp73-afk
            """;
        
        JOptionPane.showMessageDialog(this, message, "About Algorithm Visualizer", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openGitHub(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(java.net.URI.create("https://github.com/devrajsinhp73-afk/algorithm-visualizer"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Could not open GitHub repository.\nURL: https://github.com/devrajsinhp73-afk/algorithm-visualizer",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}