package com.algorithmvisualizer.ui;

import com.algorithmvisualizer.models.pathfinding.PathfindingGrid;
import com.algorithmvisualizer.models.pathfinding.GridCell;
import com.algorithmvisualizer.models.pathfinding.CellType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Interactive canvas for pathfinding grid visualization.
 * Supports mouse interaction for creating walls and setting start/end points.
 */
public class PathfindingCanvas extends JPanel {
    
    private PathfindingGrid grid;
    private int cellSize;
    private int gridRows;
    private int gridCols;
    private boolean isDrawingWalls;
    private boolean isErasingWalls;
    private boolean isSettingStart;
    private boolean isSettingEnd;
    private String statusText;
    
    // Colors
    private static final Color GRID_LINE_COLOR = Color.GRAY;
    private static final Color BORDER_COLOR = Color.BLACK;
    
    public PathfindingCanvas(int rows, int cols) {
        this.gridRows = rows;
        this.gridCols = cols;
        this.cellSize = 20;
        this.grid = new PathfindingGrid(rows, cols);
        this.statusText = "";
        
        initializeCanvas();
        setupMouseListeners();
        
        // Set default start and end positions
        grid.setStart(1, 1);
        grid.setEnd(rows - 2, cols - 2);
    }
    
    private void initializeCanvas() {
        setPreferredSize(new Dimension(gridCols * cellSize, gridRows * cellSize));
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }
    
    private void setupMouseListeners() {
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e);
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(e);
            }
        };
        
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }
    
    private void handleMousePress(MouseEvent e) {
        int row = e.getY() / cellSize;
        int col = e.getX() / cellSize;
        
        if (!grid.isValidPosition(row, col)) return;
        
        GridCell cell = grid.getCell(row, col);
        
        if (isSettingStart) {
            grid.setStart(row, col);
            isSettingStart = false;
            setCursor(Cursor.getDefaultCursor());
        } else if (isSettingEnd) {
            grid.setEnd(row, col);
            isSettingEnd = false;
            setCursor(Cursor.getDefaultCursor());
        } else {
            // Toggle walls
            if (cell.getType() == CellType.EMPTY) {
                cell.setType(CellType.WALL);
                isDrawingWalls = true;
            } else if (cell.getType() == CellType.WALL) {
                cell.setType(CellType.EMPTY);
                isErasingWalls = true;
            }
        }
        
        repaint();
    }
    
    private void handleMouseDrag(MouseEvent e) {
        if (isSettingStart || isSettingEnd) return;
        
        int row = e.getY() / cellSize;
        int col = e.getX() / cellSize;
        
        if (!grid.isValidPosition(row, col)) return;
        
        GridCell cell = grid.getCell(row, col);
        
        // Continue drawing or erasing walls
        if (isDrawingWalls && cell.getType() == CellType.EMPTY) {
            cell.setType(CellType.WALL);
            repaint();
        } else if (isErasingWalls && cell.getType() == CellType.WALL) {
            cell.setType(CellType.EMPTY);
            repaint();
        }
    }
    
    private void handleMouseRelease(MouseEvent e) {
        isDrawingWalls = false;
        isErasingWalls = false;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawGrid(g2d);
        drawCells(g2d);
        drawGridLines(g2d);
        drawStatusText(g2d);
        
        g2d.dispose();
    }
    
    private void drawGrid(Graphics2D g2d) {
        // Draw border
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRect(0, 0, gridCols * cellSize, gridRows * cellSize);
    }
    
    private void drawCells(Graphics2D g2d) {
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                GridCell cell = grid.getCell(row, col);
                drawCell(g2d, cell, col * cellSize, row * cellSize);
            }
        }
    }
    
    private void drawCell(Graphics2D g2d, GridCell cell, int x, int y) {
        // Fill cell background
        g2d.setColor(cell.getColor());
        g2d.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
        
        // Draw cell border for special types
        if (cell.getType() == CellType.START || cell.getType() == CellType.END) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
        }
        
        // Draw cell label if present
        String label = cell.getLabel();
        if (label != null && !label.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getHeight();
            
            int textX = x + (cellSize - textWidth) / 2;
            int textY = y + (cellSize + textHeight) / 2 - 2;
            
            g2d.drawString(label, textX, textY);
        }
        
        // Draw start/end symbols
        if (cell.getType() == CellType.START) {
            drawSymbol(g2d, "S", x, y, Color.WHITE);
        } else if (cell.getType() == CellType.END) {
            drawSymbol(g2d, "E", x, y, Color.WHITE);
        }
    }
    
    private void drawSymbol(Graphics2D g2d, String symbol, int x, int y, Color color) {
        g2d.setColor(color);
        g2d.setFont(new Font("Arial", Font.BOLD, cellSize / 2));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(symbol);
        int textHeight = fm.getHeight();
        
        int textX = x + (cellSize - textWidth) / 2;
        int textY = y + (cellSize + textHeight) / 2 - 2;
        
        g2d.drawString(symbol, textX, textY);
    }
    
    private void drawGridLines(Graphics2D g2d) {
        g2d.setColor(GRID_LINE_COLOR);
        g2d.setStroke(new BasicStroke(1.0f));
        
        // Vertical lines
        for (int col = 1; col < gridCols; col++) {
            int x = col * cellSize;
            g2d.drawLine(x, 0, x, gridRows * cellSize);
        }
        
        // Horizontal lines
        for (int row = 1; row < gridRows; row++) {
            int y = row * cellSize;
            g2d.drawLine(0, y, gridCols * cellSize, y);
        }
    }
    
    private void drawStatusText(Graphics2D g2d) {
        if (statusText != null && !statusText.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(statusText, 5, getHeight() - 5);
        }
    }
    
    // Public methods
    public PathfindingGrid getGrid() {
        return grid;
    }
    
    public void setGrid(PathfindingGrid grid) {
        this.grid = grid;
        repaint();
    }
    
    public void clearPath() {
        for (GridCell cell : grid.getAllCells()) {
            if (cell.getType() == CellType.PATH || 
                cell.getType() == CellType.VISITED || 
                cell.getType() == CellType.EXPLORING || 
                cell.getType() == CellType.FRONTIER) {
                cell.setType(CellType.EMPTY);
            }
        }
        repaint();
    }
    
    public void resetGrid() {
        grid.resetForSearch();
        repaint();
    }
    
    public void clearAll() {
        grid.clear();
        // Set default positions
        grid.setStart(1, 1);
        grid.setEnd(gridRows - 2, gridCols - 2);
        repaint();
    }
    
    public void generateMaze(double wallProbability) {
        grid.generateRandomMaze(wallProbability);
        repaint();
    }
    
    public void setSettingStart(boolean setting) {
        this.isSettingStart = setting;
        setCursor(setting ? Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR) : Cursor.getDefaultCursor());
    }
    
    public void setSettingEnd(boolean setting) {
        this.isSettingEnd = setting;
        setCursor(setting ? Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR) : Cursor.getDefaultCursor());
    }
    
    public void setStatusText(String statusText) {
        this.statusText = statusText;
        repaint();
    }
    
    public void highlightPath(List<GridCell> path) {
        if (path == null) return;
        
        for (GridCell cell : path) {
            if (cell.getType() != CellType.START && cell.getType() != CellType.END) {
                cell.setType(CellType.PATH);
            }
        }
        repaint();
    }
    
    public int getCellSize() {
        return cellSize;
    }
    
    public void setCellSize(int cellSize) {
        this.cellSize = Math.max(10, cellSize);
        setPreferredSize(new Dimension(gridCols * this.cellSize, gridRows * this.cellSize));
        revalidate();
        repaint();
    }
}