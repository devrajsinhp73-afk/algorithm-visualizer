package com.algorithmvisualizer;

import com.algorithmvisualizer.algorithms.pathfinding.*;
import com.algorithmvisualizer.models.pathfinding.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for pathfinding algorithms.
 */
public class PathfindingAlgorithmsTest {
    
    private PathfindingGrid grid;
    
    @BeforeEach
    public void setUp() {
        // Create a simple 5x5 grid for testing
        grid = new PathfindingGrid(5, 5);
        grid.setStart(0, 0);
        grid.setEnd(4, 4);
    }
    
    @Test
    public void testAStarWithClearPath() {
        AStarAlgorithm aStar = new AStarAlgorithm();
        List<GridCell> path = aStar.findPath(grid, (g, step, cell) -> {});
        
        assertFalse(path.isEmpty(), "A* should find a path in clear grid");
        assertEquals(grid.getStartCell(), path.get(0), "Path should start at start cell");
        assertEquals(grid.getEndCell(), path.get(path.size() - 1), "Path should end at end cell");
    }
    
    @Test
    public void testDijkstraWithClearPath() {
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        List<GridCell> path = dijkstra.findPath(grid, (g, step, cell) -> {});
        
        assertFalse(path.isEmpty(), "Dijkstra should find a path in clear grid");
        assertEquals(grid.getStartCell(), path.get(0), "Path should start at start cell");
        assertEquals(grid.getEndCell(), path.get(path.size() - 1), "Path should end at end cell");
    }
    
    @Test
    public void testBFSWithClearPath() {
        BreadthFirstSearchAlgorithm bfs = new BreadthFirstSearchAlgorithm();
        List<GridCell> path = bfs.findPath(grid, (g, step, cell) -> {});
        
        assertFalse(path.isEmpty(), "BFS should find a path in clear grid");
        assertEquals(grid.getStartCell(), path.get(0), "Path should start at start cell");
        assertEquals(grid.getEndCell(), path.get(path.size() - 1), "Path should end at end cell");
    }
    
    @Test
    public void testNoPathWhenBlocked() {
        // Create a wall that blocks the path
        for (int col = 0; col < 5; col++) {
            if (col != 0) { // Don't block the start
                grid.setWall(2, col, true);
            }
        }
        
        AStarAlgorithm aStar = new AStarAlgorithm();
        List<GridCell> path = aStar.findPath(grid, (g, step, cell) -> {});
        
        assertTrue(path.isEmpty(), "Should return empty path when no path exists");
    }
    
    @Test
    public void testPathLengthConsistency() {
        AStarAlgorithm aStar = new AStarAlgorithm();
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        BreadthFirstSearchAlgorithm bfs = new BreadthFirstSearchAlgorithm();
        
        List<GridCell> aStarPath = aStar.findPath(grid, (g, step, cell) -> {});
        grid.resetForSearch(); // Reset for next algorithm
        
        List<GridCell> dijkstraPath = dijkstra.findPath(grid, (g, step, cell) -> {});
        grid.resetForSearch(); // Reset for next algorithm
        
        List<GridCell> bfsPath = bfs.findPath(grid, (g, step, cell) -> {});
        
        // All algorithms should find optimal paths of the same length
        assertEquals(aStarPath.size(), dijkstraPath.size(), 
            "A* and Dijkstra should find same length optimal path");
        assertEquals(dijkstraPath.size(), bfsPath.size(), 
            "Dijkstra and BFS should find same length optimal path");
    }
    
    @Test
    public void testAlgorithmProperties() {
        AStarAlgorithm aStar = new AStarAlgorithm();
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        BreadthFirstSearchAlgorithm bfs = new BreadthFirstSearchAlgorithm();
        
        assertTrue(aStar.isOptimal(), "A* should be optimal");
        assertTrue(dijkstra.isOptimal(), "Dijkstra should be optimal");
        assertTrue(bfs.isOptimal(), "BFS should be optimal for unweighted graphs");
        
        assertNotNull(aStar.getName(), "A* should have a name");
        assertNotNull(dijkstra.getName(), "Dijkstra should have a name");
        assertNotNull(bfs.getName(), "BFS should have a name");
    }
    
    @Test
    public void testGridCellDistance() {
        GridCell cell1 = new GridCell(0, 0);
        GridCell cell2 = new GridCell(3, 4);
        
        assertEquals(7.0, cell1.manhattanDistance(cell2), "Manhattan distance should be 7");
        assertEquals(5.0, cell1.euclideanDistance(cell2), 0.01, "Euclidean distance should be 5.0");
    }
    
    @Test
    public void testGridOperations() {
        PathfindingGrid testGrid = new PathfindingGrid(10, 10);
        
        // Test valid positions
        assertTrue(testGrid.isValidPosition(0, 0), "Top-left should be valid");
        assertTrue(testGrid.isValidPosition(9, 9), "Bottom-right should be valid");
        assertFalse(testGrid.isValidPosition(-1, 0), "Negative row should be invalid");
        assertFalse(testGrid.isValidPosition(0, 10), "Too large column should be invalid");
        
        // Test neighbors
        GridCell center = testGrid.getCell(5, 5);
        List<GridCell> neighbors = testGrid.getNeighbors(center);
        assertEquals(4, neighbors.size(), "Center cell should have 4 neighbors");
        
        GridCell corner = testGrid.getCell(0, 0);
        List<GridCell> cornerNeighbors = testGrid.getNeighbors(corner);
        assertEquals(2, cornerNeighbors.size(), "Corner cell should have 2 neighbors");
    }
}