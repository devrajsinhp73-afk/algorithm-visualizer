package com.algorithmvisualizer.models.pathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the grid for pathfinding algorithms.
 */
public class PathfindingGrid {
    private final int rows;
    private final int cols;
    private final GridCell[][] grid;
    private GridCell startCell;
    private GridCell endCell;
    
    public PathfindingGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new GridCell[rows][cols];
        
        // Initialize all cells
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col] = new GridCell(row, col);
            }
        }
    }
    
    // Getters
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public GridCell getStartCell() { return startCell; }
    public GridCell getEndCell() { return endCell; }
    
    /**
     * Gets the cell at the specified position.
     */
    public GridCell getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return grid[row][col];
        }
        return null;
    }
    
    /**
     * Sets a cell as the start position.
     */
    public void setStart(int row, int col) {
        if (isValidPosition(row, col)) {
            // Clear previous start
            if (startCell != null) {
                startCell.setType(CellType.EMPTY);
            }
            
            startCell = grid[row][col];
            startCell.setType(CellType.START);
        }
    }
    
    /**
     * Sets a cell as the end position.
     */
    public void setEnd(int row, int col) {
        if (isValidPosition(row, col)) {
            // Clear previous end
            if (endCell != null) {
                endCell.setType(CellType.EMPTY);
            }
            
            endCell = grid[row][col];
            endCell.setType(CellType.END);
        }
    }
    
    /**
     * Toggles a cell between wall and empty (if it's not start/end).
     */
    public void toggleWall(int row, int col) {
        if (isValidPosition(row, col)) {
            GridCell cell = grid[row][col];
            if (cell.getType() == CellType.EMPTY) {
                cell.setType(CellType.WALL);
            } else if (cell.getType() == CellType.WALL) {
                cell.setType(CellType.EMPTY);
            }
        }
    }
    
    /**
     * Sets a cell as a wall.
     */
    public void setWall(int row, int col, boolean isWall) {
        if (isValidPosition(row, col)) {
            GridCell cell = grid[row][col];
            if (cell.getType() != CellType.START && cell.getType() != CellType.END) {
                cell.setType(isWall ? CellType.WALL : CellType.EMPTY);
            }
        }
    }
    
    /**
     * Gets all valid neighbors of a cell.
     */
    public List<GridCell> getNeighbors(GridCell cell) {
        return getNeighbors(cell.getRow(), cell.getCol());
    }
    
    /**
     * Gets all valid neighbors of a cell at the given position.
     */
    public List<GridCell> getNeighbors(int row, int col) {
        List<GridCell> neighbors = new ArrayList<>();
        
        // 4-directional movement (up, down, left, right)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            if (isValidPosition(newRow, newCol)) {
                neighbors.add(grid[newRow][newCol]);
            }
        }
        
        return neighbors;
    }
    
    /**
     * Gets all valid neighbors including diagonals.
     */
    public List<GridCell> getNeighborsWithDiagonals(GridCell cell) {
        return getNeighborsWithDiagonals(cell.getRow(), cell.getCol());
    }
    
    /**
     * Gets all valid neighbors including diagonals.
     */
    public List<GridCell> getNeighborsWithDiagonals(int row, int col) {
        List<GridCell> neighbors = new ArrayList<>();
        
        // 8-directional movement (including diagonals)
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            if (isValidPosition(newRow, newCol)) {
                neighbors.add(grid[newRow][newCol]);
            }
        }
        
        return neighbors;
    }
    
    /**
     * Resets the grid for a new pathfinding search.
     */
    public void resetForSearch() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col].reset();
            }
        }
        
        // Restore start and end positions
        if (startCell != null) {
            startCell.setType(CellType.START);
        }
        if (endCell != null) {
            endCell.setType(CellType.END);
        }
    }
    
    /**
     * Clears the entire grid.
     */
    public void clear() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col].setType(CellType.EMPTY);
            }
        }
        startCell = null;
        endCell = null;
    }
    
    /**
     * Creates a random maze pattern.
     */
    public void generateRandomMaze(double wallProbability) {
        clear();
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (Math.random() < wallProbability) {
                    grid[row][col].setType(CellType.WALL);
                }
            }
        }
        
        // Set default start and end positions
        setStart(1, 1);
        setEnd(rows - 2, cols - 2);
    }
    
    /**
     * Checks if the given position is within grid bounds.
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    
    /**
     * Gets all cells in the grid as a flat list.
     */
    public List<GridCell> getAllCells() {
        List<GridCell> allCells = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                allCells.add(grid[row][col]);
            }
        }
        return allCells;
    }
}