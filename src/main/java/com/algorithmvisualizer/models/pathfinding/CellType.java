package com.algorithmvisualizer.models.pathfinding;

import java.awt.Color;

/**
 * Represents different types of cells in the pathfinding grid.
 */
public enum CellType {
    EMPTY(Color.WHITE, "Empty cell"),
    WALL(Color.BLACK, "Wall/Obstacle"),
    START(Color.GREEN, "Start position"),
    END(Color.RED, "End/Target position"),
    PATH(Color.BLUE, "Final path"),
    VISITED(Color.LIGHT_GRAY, "Visited during search"),
    EXPLORING(Color.YELLOW, "Currently exploring"),
    FRONTIER(Color.ORANGE, "In frontier/queue");
    
    private final Color defaultColor;
    private final String description;
    
    CellType(Color defaultColor, String description) {
        this.defaultColor = defaultColor;
        this.description = description;
    }
    
    public Color getDefaultColor() {
        return defaultColor;
    }
    
    public String getDescription() {
        return description;
    }
}