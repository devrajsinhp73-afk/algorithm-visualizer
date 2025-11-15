package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.models.pathfinding.PathfindingGrid;
import com.algorithmvisualizer.models.pathfinding.GridCell;
import java.util.List;

/**
 * Interface for pathfinding algorithms with visualization callbacks.
 */
public interface PathfindingAlgorithm {
    
    /**
     * Finds a path from start to end and calls the callback for each step.
     * 
     * @param grid the grid to search in
     * @param callback callback function called after each step
     * @return the path from start to end, or empty list if no path exists
     */
    List<GridCell> findPath(PathfindingGrid grid, PathfindingCallback callback);
    
    /**
     * Gets the name of the pathfinding algorithm.
     * 
     * @return the algorithm name
     */
    String getName();
    
    /**
     * Gets the time complexity of the algorithm.
     * 
     * @return time complexity description
     */
    String getTimeComplexity();
    
    /**
     * Gets the space complexity of the algorithm.
     * 
     * @return space complexity description
     */
    String getSpaceComplexity();
    
    /**
     * Gets a description of how the algorithm works.
     * 
     * @return algorithm description
     */
    String getDescription();
    
    /**
     * Whether this algorithm guarantees the shortest path.
     * 
     * @return true if optimal, false otherwise
     */
    boolean isOptimal();
    
    /**
     * Callback interface for pathfinding visualization steps.
     */
    @FunctionalInterface
    interface PathfindingCallback {
        /**
         * Called after each step of the pathfinding algorithm.
         * 
         * @param grid current state of the grid
         * @param step step description
         * @param currentCell the cell currently being processed (can be null)
         */
        void onStep(PathfindingGrid grid, String step, GridCell currentCell);
    }
}