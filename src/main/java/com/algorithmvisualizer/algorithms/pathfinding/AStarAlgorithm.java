package com.algorithmvisualizer.algorithms.pathfinding;

import com.algorithmvisualizer.algorithms.PathfindingAlgorithm;
import com.algorithmvisualizer.models.pathfinding.PathfindingGrid;
import com.algorithmvisualizer.models.pathfinding.GridCell;
import com.algorithmvisualizer.models.pathfinding.CellType;

import java.awt.Color;
import java.util.*;

/**
 * A* pathfinding algorithm implementation with visualization.
 * Uses heuristic to guide search towards the goal.
 * Time Complexity: O(b^d) where b is branching factor and d is depth
 * Space Complexity: O(b^d)
 */
public class AStarAlgorithm implements PathfindingAlgorithm {
    
    @Override
    public List<GridCell> findPath(PathfindingGrid grid, PathfindingCallback callback) {
        GridCell start = grid.getStartCell();
        GridCell end = grid.getEndCell();
        
        if (start == null || end == null) {
            callback.onStep(grid, "Start or end position not set!", null);
            return new ArrayList<>();
        }
        
        // Initialize data structures
        PriorityQueue<GridCell> openSet = new PriorityQueue<>(Comparator.comparingDouble(GridCell::getFCost));
        Set<GridCell> closedSet = new HashSet<>();
        
        // Initialize start cell
        start.setGCost(0);
        start.setHCost(start.euclideanDistance(end));
        openSet.add(start);
        
        callback.onStep(grid, "Starting A* search from " + start + " to " + end, start);
        
        while (!openSet.isEmpty()) {
            GridCell current = openSet.poll();
            closedSet.add(current);
            
            // Mark as visited
            if (current.getType() != CellType.START && current.getType() != CellType.END) {
                current.setType(CellType.VISITED);
                current.setColor(Color.LIGHT_GRAY);
            }
            
            callback.onStep(grid, "Exploring cell " + current + " (f=" + 
                String.format("%.1f", current.getFCost()) + ")", current);
            
            // Check if we reached the goal
            if (current.equals(end)) {
                callback.onStep(grid, "Path found! Reconstructing path...", current);
                return reconstructPath(current, callback, grid);
            }
            
            // Explore neighbors
            List<GridCell> neighbors = grid.getNeighbors(current);
            for (GridCell neighbor : neighbors) {
                if (!neighbor.isWalkable() || closedSet.contains(neighbor)) {
                    continue;
                }
                
                double tentativeGCost = current.getGCost() + getDistance(current, neighbor);
                
                if (tentativeGCost < neighbor.getGCost()) {
                    // This path to neighbor is better
                    neighbor.setParent(current);
                    neighbor.setGCost(tentativeGCost);
                    neighbor.setHCost(neighbor.euclideanDistance(end));
                    
                    // Set label to show costs
                    neighbor.setLabel(String.format("f=%.1f", neighbor.getFCost()));
                    
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                        
                        // Mark as in frontier
                        if (neighbor.getType() != CellType.START && neighbor.getType() != CellType.END) {
                            neighbor.setType(CellType.FRONTIER);
                            neighbor.setColor(Color.ORANGE);
                        }
                        
                        callback.onStep(grid, "Added neighbor " + neighbor + " to frontier", neighbor);
                    }
                }
            }
        }
        
        callback.onStep(grid, "No path found!", null);
        return new ArrayList<>();
    }
    
    private List<GridCell> reconstructPath(GridCell endCell, PathfindingCallback callback, PathfindingGrid grid) {
        List<GridCell> path = new ArrayList<>();
        GridCell current = endCell;
        
        while (current != null) {
            path.add(0, current);
            current = current.getParent();
        }
        
        // Highlight the path
        for (int i = 0; i < path.size(); i++) {
            GridCell cell = path.get(i);
            if (cell.getType() != CellType.START && cell.getType() != CellType.END) {
                cell.setType(CellType.PATH);
                cell.setColor(Color.BLUE);
            }
            callback.onStep(grid, "Path step " + (i + 1) + ": " + cell, cell);
        }
        
        callback.onStep(grid, "Path reconstruction complete! Length: " + path.size(), null);
        return path;
    }
    
    private double getDistance(GridCell a, GridCell b) {
        // Use Manhattan distance for grid-based movement
        return a.manhattanDistance(b);
    }
    
    @Override
    public String getName() {
        return "A* Algorithm";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(b^d)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(b^d)";
    }
    
    @Override
    public String getDescription() {
        return "A* uses a heuristic to guide the search towards the goal, making it more efficient than Dijkstra's algorithm while still guaranteeing the shortest path.";
    }
    
    @Override
    public boolean isOptimal() {
        return true;
    }
    
    @Override
    public String toString() {
        return getName();
    }
}