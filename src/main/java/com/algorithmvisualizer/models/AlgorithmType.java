package com.algorithmvisualizer.models;

/**
 * Represents the type of algorithm being visualized.
 */
public enum AlgorithmType {
    SORTING("Sorting Algorithms"),
    PATHFINDING("Pathfinding Algorithms"),
    GRAPH_TRAVERSAL("Graph Traversal Algorithms");
    
    private final String displayName;
    
    AlgorithmType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}