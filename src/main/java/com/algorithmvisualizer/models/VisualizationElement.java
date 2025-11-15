package com.algorithmvisualizer.models;

import java.awt.Color;

/**
 * Represents an element in a visualization (e.g., array element, graph node, grid cell).
 */
public class VisualizationElement {
    private int value;
    private int x, y;
    private int width, height;
    private Color color;
    private String label;
    private boolean highlighted;
    private boolean selected;
    
    public VisualizationElement(int value, int x, int y, int width, int height) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = Color.LIGHT_GRAY;
        this.label = String.valueOf(value);
        this.highlighted = false;
        this.selected = false;
    }
    
    public VisualizationElement(int value) {
        this(value, 0, 0, 30, 30);
    }
    
    // Getters and setters
    public int getValue() { return value; }
    public void setValue(int value) { 
        this.value = value; 
        this.label = String.valueOf(value);
    }
    
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    
    public boolean isHighlighted() { return highlighted; }
    public void setHighlighted(boolean highlighted) { this.highlighted = highlighted; }
    
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    
    @Override
    public String toString() {
        return "Element{" + "value=" + value + ", position=(" + x + "," + y + ")}";
    }
}