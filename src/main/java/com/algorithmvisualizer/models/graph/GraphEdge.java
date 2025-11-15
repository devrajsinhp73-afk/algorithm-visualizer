package com.algorithmvisualizer.models.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.Objects;

/**
 * Represents an edge between two nodes in a graph visualization.
 */
public class GraphEdge {
    private final GraphNode source;
    private final GraphNode target;
    private boolean directed;
    private double weight;
    private Color color;
    private boolean highlighted;
    private boolean traversed;
    private String label;
    
    // Visual properties
    private static final Color DEFAULT_COLOR = Color.GRAY;
    private static final Color HIGHLIGHTED_COLOR = Color.RED;
    private static final Color TRAVERSED_COLOR = Color.BLUE;
    private static final int DEFAULT_THICKNESS = 2;
    private static final int HIGHLIGHTED_THICKNESS = 3;
    
    public GraphEdge(GraphNode source, GraphNode target) {
        this(source, target, false, 1.0);
    }
    
    public GraphEdge(GraphNode source, GraphNode target, boolean directed) {
        this(source, target, directed, 1.0);
    }
    
    public GraphEdge(GraphNode source, GraphNode target, boolean directed, double weight) {
        this.source = source;
        this.target = target;
        this.directed = directed;
        this.weight = weight;
        this.color = DEFAULT_COLOR;
        this.highlighted = false;
        this.traversed = false;
        this.label = weight != 1.0 ? String.valueOf(weight) : "";
    }
    
    // Getters
    public GraphNode getSource() { return source; }
    public GraphNode getTarget() { return target; }
    public boolean isDirected() { return directed; }
    public double getWeight() { return weight; }
    public Color getColor() { return color; }
    public boolean isHighlighted() { return highlighted; }
    public boolean isTraversed() { return traversed; }
    public String getLabel() { return label; }
    
    // Setters
    public void setDirected(boolean directed) { this.directed = directed; }
    public void setWeight(double weight) { 
        this.weight = weight;
        this.label = weight != 1.0 ? String.valueOf(weight) : "";
    }
    public void setColor(Color color) { this.color = color; }
    public void setHighlighted(boolean highlighted) { 
        this.highlighted = highlighted;
        this.color = highlighted ? HIGHLIGHTED_COLOR : (traversed ? TRAVERSED_COLOR : DEFAULT_COLOR);
    }
    public void setTraversed(boolean traversed) { 
        this.traversed = traversed;
        if (traversed && !highlighted) {
            this.color = TRAVERSED_COLOR;
        }
    }
    public void setLabel(String label) { this.label = label; }
    
    /**
     * Resets the edge for a new traversal.
     */
    public void reset() {
        highlighted = false;
        traversed = false;
        color = DEFAULT_COLOR;
    }
    
    /**
     * Resets visualization state (alias for reset).
     */
    public void resetVisualization() {
        reset();
    }
    
    /**
     * Gets the other node in this edge (given one endpoint).
     */
    public GraphNode getOther(GraphNode node) {
        if (node.equals(source)) {
            return target;
        } else if (node.equals(target)) {
            return source;
        }
        return null;
    }
    
    /**
     * Checks if this edge connects the given nodes.
     */
    public boolean connects(GraphNode node1, GraphNode node2) {
        return (source.equals(node1) && target.equals(node2)) ||
               (!directed && source.equals(node2) && target.equals(node1));
    }
    
    /**
     * Checks if this edge is incident to the given node.
     */
    public boolean isIncidentTo(GraphNode node) {
        return source.equals(node) || target.equals(node);
    }
    
    /**
     * Gets the length of this edge based on node positions.
     */
    public double getLength() {
        return source.distanceTo(target);
    }
    
    /**
     * Draws this edge on the given graphics context.
     */
    public void draw(Graphics2D g2d) {
        // Set stroke based on highlight status
        int thickness = highlighted ? HIGHLIGHTED_THICKNESS : DEFAULT_THICKNESS;
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        
        // Draw line
        int x1 = source.getX();
        int y1 = source.getY();
        int x2 = target.getX();
        int y2 = target.getY();
        
        // Calculate edge endpoints to stop at node boundaries
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int sourceRadius = source.getRadius();
        int targetRadius = target.getRadius();
        
        int startX = x1 + (int)(sourceRadius * Math.cos(angle));
        int startY = y1 + (int)(sourceRadius * Math.sin(angle));
        int endX = x2 - (int)(targetRadius * Math.cos(angle));
        int endY = y2 - (int)(targetRadius * Math.sin(angle));
        
        g2d.drawLine(startX, startY, endX, endY);
        
        // Draw arrowhead for directed edges
        if (directed) {
            drawArrowhead(g2d, endX, endY, angle);
        }
        
        // Draw edge label if present
        if (label != null && !label.isEmpty()) {
            drawLabel(g2d, (startX + endX) / 2, (startY + endY) / 2);
        }
    }
    
    private void drawArrowhead(Graphics2D g2d, int x, int y, double angle) {
        int arrowLength = 10;
        double arrowAngle = Math.PI / 6; // 30 degrees
        
        // Calculate arrowhead points
        int x1 = x - (int)(arrowLength * Math.cos(angle - arrowAngle));
        int y1 = y - (int)(arrowLength * Math.sin(angle - arrowAngle));
        int x2 = x - (int)(arrowLength * Math.cos(angle + arrowAngle));
        int y2 = y - (int)(arrowLength * Math.sin(angle + arrowAngle));
        
        // Draw arrowhead
        g2d.drawLine(x, y, x1, y1);
        g2d.drawLine(x, y, x2, y2);
    }
    
    private void drawLabel(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawString(label, x - 10, y - 5);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GraphEdge edge = (GraphEdge) obj;
        return Objects.equals(source, edge.source) && Objects.equals(target, edge.target);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
    
    @Override
    public String toString() {
        String arrow = directed ? " -> " : " -- ";
        return source.getId() + arrow + target.getId() + 
               (weight != 1.0 ? " (w=" + weight + ")" : "");
    }
}