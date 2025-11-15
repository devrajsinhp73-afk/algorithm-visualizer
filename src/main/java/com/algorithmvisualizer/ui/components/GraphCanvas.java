package com.algorithmvisualizer.ui.components;

import com.algorithmvisualizer.models.graph.Graph;
import com.algorithmvisualizer.models.graph.GraphNode;
import com.algorithmvisualizer.models.graph.GraphEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Interactive canvas for graph visualization and editing.
 * Supports drag-and-drop node creation, edge connections, and graph manipulation.
 */
public class GraphCanvas extends JPanel {
    
    private Graph graph;
    private GraphNode selectedNode;
    private GraphNode draggedNode;
    private Point dragOffset;
    private boolean isCreatingEdge;
    private GraphNode edgeSourceNode;
    private Point currentMousePosition;
    private String statusText;
    
    // Visual settings
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color GRID_COLOR = new Color(240, 240, 240);
    private static final int GRID_SIZE = 20;
    
    // Interaction modes
    public enum InteractionMode {
        SELECT,     // Select and move nodes
        ADD_NODE,   // Add new nodes
        ADD_EDGE,   // Add edges between nodes
        DELETE      // Delete nodes/edges
    }
    
    private InteractionMode currentMode;
    
    public GraphCanvas() {
        this.graph = new Graph();
        this.currentMode = InteractionMode.SELECT;
        this.statusText = "";
        
        initializeCanvas();
        setupMouseListeners();
        
        // Create a sample graph for demonstration
        graph.createSampleGraph();
    }
    
    private void initializeCanvas() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(BACKGROUND_COLOR);
        setDoubleBuffered(true);
        
        // Enable focus for keyboard events
        setFocusable(true);
    }
    
    private void setupMouseListeners() {
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                handleMousePress(e);
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(e);
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        };
        
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        
        // Add keyboard listener for shortcuts
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }
    
    private void handleMousePress(MouseEvent e) {
        Point point = e.getPoint();
        GraphNode nodeAtPoint = graph.getNodeAt(point);
        
        switch (currentMode) {
            case SELECT:
                if (nodeAtPoint != null) {
                    selectNode(nodeAtPoint);
                    draggedNode = nodeAtPoint;
                    dragOffset = new Point(point.x - nodeAtPoint.getX(), point.y - nodeAtPoint.getY());
                } else {
                    selectNode(null);
                }
                break;
                
            case ADD_NODE:
                if (nodeAtPoint == null) {
                    String nodeId = generateNodeId();
                    graph.addNode(nodeId, point);
                    fireGraphChanged();
                    repaint();
                }
                break;
                
            case ADD_EDGE:
                if (nodeAtPoint != null) {
                    if (edgeSourceNode == null) {
                        edgeSourceNode = nodeAtPoint;
                        isCreatingEdge = true;
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    } else if (nodeAtPoint != edgeSourceNode) {
                        graph.addEdge(edgeSourceNode, nodeAtPoint);
                        edgeSourceNode = null;
                        isCreatingEdge = false;
                        setCursor(Cursor.getDefaultCursor());
                        fireGraphChanged();
                        repaint();
                    }
                }
                break;
                
            case DELETE:
                if (nodeAtPoint != null) {
                    graph.removeNode(nodeAtPoint.getId());
                    selectNode(null);
                    fireGraphChanged();
                    repaint();
                }
                break;
        }
    }
    
    private void handleMouseDrag(MouseEvent e) {
        if (draggedNode != null && currentMode == InteractionMode.SELECT) {
            Point newPosition = new Point(
                e.getX() - dragOffset.x,
                e.getY() - dragOffset.y
            );
            draggedNode.setPosition(newPosition);
            repaint();
        }
        
        currentMousePosition = e.getPoint();
        if (isCreatingEdge) {
            repaint();
        }
    }
    
    private void handleMouseRelease(MouseEvent e) {
        draggedNode = null;
        dragOffset = null;
    }
    
    private void handleMouseMove(MouseEvent e) {
        currentMousePosition = e.getPoint();
        
        // Update cursor based on mode and what's under mouse
        GraphNode nodeAtPoint = graph.getNodeAt(e.getPoint());
        
        switch (currentMode) {
            case SELECT:
                setCursor(nodeAtPoint != null ? 
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : 
                    Cursor.getDefaultCursor());
                break;
            case ADD_NODE:
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                break;
            case ADD_EDGE:
                if (!isCreatingEdge) {
                    setCursor(nodeAtPoint != null ? 
                        Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR) : 
                        Cursor.getDefaultCursor());
                }
                break;
            case DELETE:
                setCursor(nodeAtPoint != null ? 
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : 
                    Cursor.getDefaultCursor());
                break;
        }
        
        if (isCreatingEdge) {
            repaint();
        }
    }
    
    private void handleMouseClick(MouseEvent e) {
        if (e.getClickCount() == 2 && currentMode == InteractionMode.SELECT) {
            GraphNode nodeAtPoint = graph.getNodeAt(e.getPoint());
            if (nodeAtPoint != null) {
                editNodeLabel(nodeAtPoint);
            }
        }
    }
    
    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_BACK_SPACE:
                if (selectedNode != null) {
                    graph.removeNode(selectedNode.getId());
                    selectNode(null);
                    fireGraphChanged();
                    repaint();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if (isCreatingEdge) {
                    isCreatingEdge = false;
                    edgeSourceNode = null;
                    setCursor(Cursor.getDefaultCursor());
                    repaint();
                }
                break;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        drawGrid(g2d);
        drawEdges(g2d);
        drawNodes(g2d);
        drawTemporaryEdge(g2d);
        drawStatusText(g2d);
        
        g2d.dispose();
    }
    
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1.0f));
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw vertical grid lines
        for (int x = 0; x < width; x += GRID_SIZE) {
            g2d.drawLine(x, 0, x, height);
        }
        
        // Draw horizontal grid lines
        for (int y = 0; y < height; y += GRID_SIZE) {
            g2d.drawLine(0, y, width, y);
        }
    }
    
    private void drawEdges(Graphics2D g2d) {
        for (GraphEdge edge : graph.getAllEdges()) {
            edge.draw(g2d);
        }
    }
    
    private void drawNodes(Graphics2D g2d) {
        for (GraphNode node : graph.getAllNodes()) {
            drawNode(g2d, node);
        }
    }
    
    private void drawNode(Graphics2D g2d, GraphNode node) {
        int x = node.getX();
        int y = node.getY();
        int radius = node.getRadius();
        
        // Draw node shadow
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(x - radius + 2, y - radius + 2, radius * 2, radius * 2);
        
        // Draw node background
        g2d.setColor(node.getColor());
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        
        // Draw node border
        if (node.isSelected()) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3.0f));
        } else if (node.isHighlighted()) {
            g2d.setColor(Color.ORANGE);
            g2d.setStroke(new BasicStroke(2.0f));
        } else {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
        }
        g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
        
        // Draw node label
        String displayText = node.getDisplayText();
        if (displayText != null && !displayText.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            
            String[] lines = displayText.split("\n");
            FontMetrics fm = g2d.getFontMetrics();
            int lineHeight = fm.getHeight();
            int totalHeight = lines.length * lineHeight;
            
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                int textWidth = fm.stringWidth(line);
                int textX = x - textWidth / 2;
                int textY = y - totalHeight / 2 + (i + 1) * lineHeight - 3;
                g2d.drawString(line, textX, textY);
            }
        }
    }
    
    private void drawTemporaryEdge(Graphics2D g2d) {
        if (isCreatingEdge && edgeSourceNode != null && currentMousePosition != null) {
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
            
            g2d.drawLine(edgeSourceNode.getX(), edgeSourceNode.getY(), 
                currentMousePosition.x, currentMousePosition.y);
        }
    }
    
    private void drawStatusText(Graphics2D g2d) {
        if (statusText != null && !statusText.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(statusText, 10, getHeight() - 10);
        }
    }
    
    private void selectNode(GraphNode node) {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
        }
        
        selectedNode = node;
        
        if (selectedNode != null) {
            selectedNode.setSelected(true);
        }
        
        fireNodeSelected(selectedNode);
        repaint();
    }
    
    private String generateNodeId() {
        int counter = graph.getNodeCount();
        String baseId = "Node";
        
        // Try simple incremental IDs first
        for (char c = 'A'; c <= 'Z'; c++) {
            String id = String.valueOf(c);
            if (graph.getNode(id) == null) {
                return id;
            }
        }
        
        // Fall back to numbered nodes
        while (graph.getNode(baseId + counter) != null) {
            counter++;
        }
        
        return baseId + counter;
    }
    
    private void editNodeLabel(GraphNode node) {
        String newLabel = JOptionPane.showInputDialog(this, 
            "Enter node label:", node.getLabel());
        
        if (newLabel != null && !newLabel.trim().isEmpty()) {
            node.setLabel(newLabel.trim());
            repaint();
        }
    }
    
    // Public methods
    public Graph getGraph() {
        return graph;
    }
    
    public void setGraph(Graph graph) {
        this.graph = graph;
        selectNode(null);
        repaint();
    }
    
    public InteractionMode getInteractionMode() {
        return currentMode;
    }
    
    public void setInteractionMode(InteractionMode mode) {
        this.currentMode = mode;
        
        // Reset interaction state
        if (isCreatingEdge) {
            isCreatingEdge = false;
            edgeSourceNode = null;
        }
        
        setCursor(Cursor.getDefaultCursor());
    }
    
    public void clearGraph() {
        graph.clear();
        selectNode(null);
        repaint();
    }
    
    public void resetTraversal() {
        graph.reset();
        repaint();
    }
    
    public void createSampleGraph() {
        graph.createSampleGraph();
        selectNode(null);
        repaint();
    }
    
    public void setDirected(boolean directed) {
        graph.setDirected(directed);
        repaint();
    }
    
    public void setStatusText(String statusText) {
        this.statusText = statusText;
        repaint();
    }
    
    public GraphNode getSelectedNode() {
        return selectedNode;
    }
    
    public void highlightNode(GraphNode node) {
        if (node != null) {
            node.setHighlighted(true);
            repaint();
        }
    }
    
    public void unhighlightAllNodes() {
        for (GraphNode node : graph.getNodeCollection()) {
            node.setHighlighted(false);
        }
        repaint();
    }
    
    // Listener interfaces and methods
    public interface GraphChangeListener {
        void onGraphChanged();
    }
    
    public interface NodeSelectionListener {
        void onNodeSelected(GraphNode node);
    }
    
    private GraphChangeListener graphChangeListener;
    private NodeSelectionListener nodeSelectionListener;
    
    public void addGraphChangeListener(GraphChangeListener listener) {
        this.graphChangeListener = listener;
    }
    
    public void addNodeSelectionListener(NodeSelectionListener listener) {
        this.nodeSelectionListener = listener;
    }
    
    private void fireGraphChanged() {
        if (graphChangeListener != null) {
            SwingUtilities.invokeLater(() -> {
                graphChangeListener.onGraphChanged();
            });
        }
    }
    
    private void fireNodeSelected(GraphNode node) {
        if (nodeSelectionListener != null) {
            SwingUtilities.invokeLater(() -> {
                nodeSelectionListener.onNodeSelected(node);
            });
        }
    }
}