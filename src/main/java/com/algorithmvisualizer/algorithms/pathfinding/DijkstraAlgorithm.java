package com.algorithmvisualizer.algorithms.pathfinding;

import com.algorithmvisualizer.algorithms.PathfindingAlgorithm;
import com.algorithmvisualizer.models.pathfinding.PathfindingGrid;
import com.algorithmvisualizer.models.pathfinding.GridCell;
import com.algorithmvisualizer.models.pathfinding.CellType;

import java.awt.Color;
import java.util.*;

/**
 * Dijkstra's shortest path algorithm implementation with visualization.
 * Guarantees shortest path but explores more nodes than A*.
 * Time Complexity: O(V log V + E) with priority queue
 * Space Complexity: O(V)
 */
public class DijkstraAlgorithm implements PathfindingAlgorithm {
    
    @Override
    public List<GridCell> findPath(PathfindingGrid grid, PathfindingCallback callback) {
        GridCell start = grid.getStartCell();
        GridCell end = grid.getEndCell();
        
        if (start == null || end == null) {
            callback.onStep(grid, "Start or end position not set!", null);
            return new ArrayList<>();
        }
        
        // Initialize all distances to infinity
        for (GridCell cell : grid.getAllCells()) {
            cell.setGCost(Double.MAX_VALUE);
        }
        
        // Priority queue to always process the cell with minimum distance
        PriorityQueue<GridCell> pq = new PriorityQueue<>(Comparator.comparingDouble(GridCell::getGCost));
        Set<GridCell> visited = new HashSet<>();
        
        // Initialize start cell
        start.setGCost(0);
        pq.add(start);
        
        callback.onStep(grid, "Starting Dijkstra's algorithm from " + start + " to " + end, start);
        
        while (!pq.isEmpty()) {
            GridCell current = pq.poll();
            
            if (visited.contains(current)) {
                continue;
            }
            
            visited.add(current);
            
            // Mark as visited
            if (current.getType() != CellType.START && current.getType() != CellType.END) {
                current.setType(CellType.VISITED);
                current.setColor(Color.LIGHT_GRAY);
            }
            
            // Set label to show distance
            current.setLabel(String.format("d=%.1f", current.getGCost()));
            
            callback.onStep(grid, "Processing cell " + current + " (distance=" + 
                String.format("%.1f", current.getGCost()) + ")", current);
            
            // Check if we reached the goal
            if (current.equals(end)) {
                callback.onStep(grid, "Shortest path found! Reconstructing path...", current);
                return reconstructPath(current, callback, grid);
            }
            
            // Explore neighbors
            List<GridCell> neighbors = grid.getNeighbors(current);
            for (GridCell neighbor : neighbors) {
                if (!neighbor.isWalkable() || visited.contains(neighbor)) {
                    continue;
                }
                
                double newDistance = current.getGCost() + getDistance(current, neighbor);
                
                if (newDistance < neighbor.getGCost()) {
                    neighbor.setGCost(newDistance);
                    neighbor.setParent(current);
                    pq.add(neighbor);
                    
                    // Mark as in frontier
                    if (neighbor.getType() != CellType.START && neighbor.getType() != CellType.END) {
                        neighbor.setType(CellType.FRONTIER);
                        neighbor.setColor(Color.ORANGE);
                    }
                    
                    // Set label to show distance
                    neighbor.setLabel(String.format("d=%.1f", neighbor.getGCost()));
                    
                    callback.onStep(grid, "Updated distance to " + neighbor + " = " + 
                        String.format("%.1f", newDistance), neighbor);
                }
            }
        }
        
        callback.onStep(grid, "No path found!", null);
        return new ArrayList<>();
    }
    
    private List<GridCell> reconstructPath(GridCell endCell, PathfindingCallback callback, PathfindingGrid grid) {
        List<GridCell> path = new ArrayList<>();
        GridCell current = endCell;
        double totalDistance = endCell.getGCost();
        
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
            callback.onStep(grid, "Shortest path step " + (i + 1) + ": " + cell, cell);
        }
        
        callback.onStep(grid, "Shortest path found! Length: " + path.size() + 
            ", Distance: " + String.format("%.1f", totalDistance), null);
        return path;
    }
    
    private double getDistance(GridCell a, GridCell b) {
        // Use unit distance for each step
        return 1.0;
    }
    
    @Override
    public String getName() {
        return "Dijkstra's Algorithm";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(V log V + E)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(V)";
    }
    
    @Override
    public String getDescription() {
        return "Dijkstra's algorithm finds the shortest path by always exploring the nearest unexplored cell. Guarantees optimal solution but may explore more cells than A*.";
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