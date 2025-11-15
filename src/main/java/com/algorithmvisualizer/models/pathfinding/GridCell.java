package com.algorithmvisualizer.models.pathfinding;

import java.awt.Color;
import java.util.Objects;

/**
 * Represents a single cell in the pathfinding grid.
 */
public class GridCell {
    private final int row;
    private final int col;
    private CellType type;
    private Color color;
    private double gCost; // Distance from start
    private double hCost; // Heuristic distance to end
    private double fCost; // Total cost (g + h)
    private GridCell parent; // For path reconstruction
    private boolean visited;
    private String label; // Optional text label
    
    public GridCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.type = CellType.EMPTY;
        this.color = CellType.EMPTY.getDefaultColor();
        this.gCost = Double.MAX_VALUE;
        this.hCost = 0;
        this.fCost = Double.MAX_VALUE;
        this.parent = null;
        this.visited = false;
        this.label = "";
    }
    
    public GridCell(int row, int col, CellType type) {
        this(row, col);
        setType(type);
    }
    
    // Getters
    public int getRow() { return row; }
    public int getCol() { return col; }
    public CellType getType() { return type; }
    public Color getColor() { return color; }
    public double getGCost() { return gCost; }
    public double getHCost() { return hCost; }
    public double getFCost() { return fCost; }
    public GridCell getParent() { return parent; }
    public boolean isVisited() { return visited; }
    public String getLabel() { return label; }
    
    // Setters
    public void setType(CellType type) {
        this.type = type;
        this.color = type.getDefaultColor();
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setGCost(double gCost) {
        this.gCost = gCost;
        updateFCost();
    }
    
    public void setHCost(double hCost) {
        this.hCost = hCost;
        updateFCost();
    }
    
    public void setParent(GridCell parent) {
        this.parent = parent;
    }
    
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    public void setLabel(String label) {
        this.label = label != null ? label : "";
    }
    
    private void updateFCost() {
        this.fCost = this.gCost + this.hCost;
    }
    
    /**
     * Resets the cell for a new pathfinding search.
     */
    public void reset() {
        if (type != CellType.WALL && type != CellType.START && type != CellType.END) {
            setType(CellType.EMPTY);
        }
        gCost = Double.MAX_VALUE;
        hCost = 0;
        fCost = Double.MAX_VALUE;
        parent = null;
        visited = false;
        label = "";
    }
    
    /**
     * Calculates Manhattan distance to another cell.
     */
    public double manhattanDistance(GridCell other) {
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }
    
    /**
     * Calculates Euclidean distance to another cell.
     */
    public double euclideanDistance(GridCell other) {
        double dx = this.row - other.row;
        double dy = this.col - other.col;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Checks if this cell is walkable (not a wall).
     */
    public boolean isWalkable() {
        return type != CellType.WALL;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GridCell cell = (GridCell) obj;
        return row == cell.row && col == cell.col;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
    @Override
    public String toString() {
        return String.format("Cell(%d,%d)[%s]", row, col, type);
    }
}