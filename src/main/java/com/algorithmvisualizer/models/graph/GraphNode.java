package com.algorithmvisualizer.models.graph;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

/**
 * Represents a node in a graph visualization.
 */
public class GraphNode {
    private final String id;
    private String label;
    private Point position;
    private Color color;
    private boolean visited;
    private boolean highlighted;
    private boolean selected;
    private int radius;
    private int discoveryTime; // For DFS
    private int finishTime;    // For DFS
    private int level;         // For BFS
    private GraphNode parent;  // For tree construction
    
    // Visual properties
    private static final int DEFAULT_RADIUS = 25;
    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color VISITED_COLOR = Color.CYAN;
    private static final Color EXPLORING_COLOR = Color.YELLOW;
    private static final Color FINISHED_COLOR = Color.GREEN;
    
    public GraphNode(String id, int x, int y) {
        this.id = id;
        this.label = id;
        this.position = new Point(x, y);
        this.color = DEFAULT_COLOR;
        this.visited = false;
        this.highlighted = false;
        this.selected = false;
        this.radius = DEFAULT_RADIUS;
        this.discoveryTime = -1;
        this.finishTime = -1;
        this.level = -1;
        this.parent = null;
    }
    
    public GraphNode(String id, Point position) {
        this(id, position.x, position.y);
    }
    
    // Getters
    public String getId() { return id; }
    public String getLabel() { return label; }
    public Point getPosition() { return position; }
    public int getX() { return position.x; }
    public int getY() { return position.y; }
    public Color getColor() { return color; }
    public boolean isVisited() { return visited; }
    public boolean isHighlighted() { return highlighted; }
    public boolean isSelected() { return selected; }
    public int getRadius() { return radius; }
    public int getDiscoveryTime() { return discoveryTime; }
    public int getFinishTime() { return finishTime; }
    public int getLevel() { return level; }
    public GraphNode getParent() { return parent; }
    
    // Setters
    public void setLabel(String label) { this.label = label; }
    public void setPosition(Point position) { this.position = new Point(position); }
    public void setPosition(int x, int y) { this.position = new Point(x, y); }
    public void setColor(Color color) { this.color = color; }
    public void setVisited(boolean visited) { 
        this.visited = visited;
        if (visited && !highlighted) {
            setColor(VISITED_COLOR);
        }
    }
    public void setHighlighted(boolean highlighted) { 
        this.highlighted = highlighted;
        if (highlighted) {
            setColor(EXPLORING_COLOR);
        }
    }
    public void setSelected(boolean selected) { this.selected = selected; }
    public void setRadius(int radius) { this.radius = Math.max(10, radius); }
    public void setDiscoveryTime(int discoveryTime) { this.discoveryTime = discoveryTime; }
    public void setFinishTime(int finishTime) { this.finishTime = finishTime; }
    public void setLevel(int level) { this.level = level; }
    public void setParent(GraphNode parent) { this.parent = parent; }
    
    /**
     * Resets the node for a new traversal.
     */
    public void reset() {
        visited = false;
        highlighted = false;
        selected = false;
        color = DEFAULT_COLOR;
        discoveryTime = -1;
        finishTime = -1;
        level = -1;
        parent = null;
    }
    
    /**
     * Marks the node as currently being explored.
     */
    public void markExploring() {
        highlighted = true;
        color = EXPLORING_COLOR;
    }
    
    /**
     * Marks the node as completely processed.
     */
    public void markFinished() {
        highlighted = false;
        visited = true;
        color = FINISHED_COLOR;
    }
    
    /**
     * Checks if a point is within this node's bounds.
     */
    public boolean contains(Point point) {
        return contains(point.x, point.y);
    }
    
    /**
     * Resets all visualization states to default.
     */
    public void resetVisualization() {
        visited = false;
        highlighted = false;
        selected = false;
        parent = null;
        discoveryTime = -1;
        finishTime = -1;
        color = DEFAULT_COLOR;
    }
    
    /**
     * Checks if coordinates are within this node's bounds.
     */
    public boolean contains(int x, int y) {
        double dx = x - position.x;
        double dy = y - position.y;
        return (dx * dx + dy * dy) <= (radius * radius);
    }
    
    /**
     * Calculates distance to another node.
     */
    public double distanceTo(GraphNode other) {
        double dx = this.position.x - other.position.x;
        double dy = this.position.y - other.position.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Gets the display text for this node including traversal info.
     */
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder(label);
        
        if (level >= 0) {
            sb.append("\nL:").append(level);
        }
        
        if (discoveryTime >= 0 && finishTime >= 0) {
            sb.append("\n").append(discoveryTime).append("/").append(finishTime);
        } else if (discoveryTime >= 0) {
            sb.append("\nD:").append(discoveryTime);
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GraphNode node = (GraphNode) obj;
        return Objects.equals(id, node.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Node{" + "id='" + id + "', pos=" + position + "}";
    }
}