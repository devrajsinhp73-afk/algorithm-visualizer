package com.algorithmvisualizer.algorithms.pathfinding;

import com.algorithmvisualizer.algorithms.PathfindingAlgorithm;
import com.algorithmvisualizer.models.pathfinding.PathfindingGrid;
import com.algorithmvisualizer.models.pathfinding.GridCell;
import com.algorithmvisualizer.models.pathfinding.CellType;

import java.awt.Color;
import java.util.*;

/**
 * Breadth-First Search pathfinding algorithm implementation with visualization.
 * Explores level by level, guaranteeing shortest path in unweighted graphs.
 * Time Complexity: O(V + E)
 * Space Complexity: O(V)
 */
public class BreadthFirstSearchAlgorithm implements PathfindingAlgorithm {
    
    @Override
    public List<GridCell> findPath(PathfindingGrid grid, PathfindingCallback callback) {
        GridCell start = grid.getStartCell();
        GridCell end = grid.getEndCell();
        
        if (start == null || end == null) {
            callback.onStep(grid, "Start or end position not set!", null);
            return new ArrayList<>();
        }
        
        Queue<GridCell> queue = new LinkedList<>();
        Set<GridCell> visited = new HashSet<>();
        
        queue.add(start);
        visited.add(start);
        start.setGCost(0);
        
        callback.onStep(grid, "Starting BFS from " + start + " to " + end, start);
        
        int level = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            level++;
            
            callback.onStep(grid, "Processing level " + level + " (" + levelSize + " cells)", null);
            
            // Process all cells at current level
            for (int i = 0; i < levelSize; i++) {
                GridCell current = queue.poll();
                
                // Mark as visited
                if (current.getType() != CellType.START && current.getType() != CellType.END) {
                    current.setType(CellType.VISITED);
                    current.setColor(new Color(200 - Math.min(level * 10, 100), 200, 200)); // Gradient effect
                }
                
                // Set label to show level
                current.setLabel("L" + level);
                
                callback.onStep(grid, "Exploring cell " + current + " at level " + level, current);
                
                // Check if we reached the goal
                if (current.equals(end)) {
                    callback.onStep(grid, "Path found at level " + level + "! Reconstructing path...", current);
                    return reconstructPath(current, callback, grid);
                }
                
                // Add neighbors to queue
                List<GridCell> neighbors = grid.getNeighbors(current);
                for (GridCell neighbor : neighbors) {
                    if (neighbor.isWalkable() && !visited.contains(neighbor)) {
                        visited.add(neighbor);
                        neighbor.setParent(current);
                        neighbor.setGCost(current.getGCost() + 1);
                        queue.add(neighbor);
                        
                        // Mark as in frontier
                        if (neighbor.getType() != CellType.START && neighbor.getType() != CellType.END) {
                            neighbor.setType(CellType.FRONTIER);
                            neighbor.setColor(Color.YELLOW);
                        }
                        
                        callback.onStep(grid, "Added neighbor " + neighbor + " to queue", neighbor);
                    }
                }
            }
        }
        
        callback.onStep(grid, "No path found after exploring " + level + " levels!", null);
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
        
        callback.onStep(grid, "BFS path complete! Length: " + path.size() + " (shortest in unweighted graph)", null);
        return path;
    }
    
    @Override
    public String getName() {
        return "Breadth-First Search";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(V + E)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(V)";
    }
    
    @Override
    public String getDescription() {
        return "BFS explores the graph level by level, guaranteeing the shortest path in unweighted graphs. It's optimal but may be slower than A* in large graphs.";
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