package com.algorithmvisualizer.ui;

import com.algorithmvisualizer.models.VisualizationElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Canvas for rendering algorithm visualizations.
 * Supports smooth animations and various drawing modes.
 */
public class VisualizationCanvas extends JPanel {
    
    private List<VisualizationElement> elements;
    private boolean animating;
    private String statusText;
    private Color backgroundColor;
    
    // Animation settings
    private Timer animationTimer;
    private int animationDelay;
    
    public VisualizationCanvas() {
        this.elements = new CopyOnWriteArrayList<>();
        this.animating = false;
        this.statusText = "";
        this.backgroundColor = Color.WHITE;
        this.animationDelay = 100; // milliseconds
        
        initializeCanvas();
        setupAnimationTimer();
    }
    
    private void initializeCanvas() {
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(800, 400));
        setDoubleBuffered(true);
        
        // Add component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionElements();
            }
        });
    }
    
    private void setupAnimationTimer() {
        animationTimer = new Timer(animationDelay, e -> {
            if (animating) {
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        drawElements(g2d);
        drawStatusText(g2d);
        
        g2d.dispose();
    }
    
    private void drawElements(Graphics2D g2d) {
        for (VisualizationElement element : elements) {
            drawElement(g2d, element);
        }
    }
    
    private void drawElement(Graphics2D g2d, VisualizationElement element) {
        // Save the original stroke and color
        Stroke originalStroke = g2d.getStroke();
        Color originalColor = g2d.getColor();
        
        // Set element color
        g2d.setColor(element.getColor());
        
        // Draw element background
        g2d.fillRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());
        
        // Draw border
        if (element.isSelected()) {
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.setColor(Color.BLUE);
        } else if (element.isHighlighted()) {
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setColor(Color.RED);
        } else {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.setColor(Color.BLACK);
        }
        
        g2d.drawRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());
        
        // Draw label
        if (element.getLabel() != null && !element.getLabel().isEmpty()) {
            g2d.setColor(Color.BLACK);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(element.getLabel());
            int textHeight = fm.getHeight();
            
            int textX = element.getX() + (element.getWidth() - textWidth) / 2;
            int textY = element.getY() + (element.getHeight() + textHeight) / 2 - 3;
            
            g2d.drawString(element.getLabel(), textX, textY);
        }
        
        // Restore original stroke and color
        g2d.setStroke(originalStroke);
        g2d.setColor(originalColor);
    }
    
    private void drawStatusText(Graphics2D g2d) {
        if (statusText != null && !statusText.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2d.drawString(statusText, 10, getHeight() - 10);
        }
    }
    
    public void setElements(List<VisualizationElement> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
        repositionElements();
        repaint();
    }
    
    public List<VisualizationElement> getElements() {
        return new CopyOnWriteArrayList<>(elements);
    }
    
    public void addElement(VisualizationElement element) {
        elements.add(element);
        repaint();
    }
    
    public void removeElement(VisualizationElement element) {
        elements.remove(element);
        repaint();
    }
    
    public void clearElements() {
        elements.clear();
        repaint();
    }
    
    private void repositionElements() {
        if (elements.isEmpty()) return;
        
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        int elementCount = elements.size();
        
        if (elementCount == 0) return;
        
        // Calculate element dimensions
        int margin = 20;
        int spacing = 5;
        int availableWidth = canvasWidth - 2 * margin;
        int elementWidth = Math.max(30, (availableWidth - (elementCount - 1) * spacing) / elementCount);
        int elementHeight = Math.min(elementWidth * 2, canvasHeight - 2 * margin - 50); // Leave space for labels
        
        // Position elements horizontally
        int startX = margin;
        if (elementCount * elementWidth + (elementCount - 1) * spacing < availableWidth) {
            startX = (canvasWidth - (elementCount * elementWidth + (elementCount - 1) * spacing)) / 2;
        }
        
        for (int i = 0; i < elements.size(); i++) {
            VisualizationElement element = elements.get(i);
            int x = startX + i * (elementWidth + spacing);
            int y = canvasHeight - margin - elementHeight;
            
            element.setX(x);
            element.setY(y);
            element.setWidth(elementWidth);
            element.setHeight(elementHeight);
        }
    }
    
    public void startAnimation() {
        animating = true;
        animationTimer.start();
    }
    
    public void stopAnimation() {
        animating = false;
        animationTimer.stop();
        repaint();
    }
    
    public void setAnimationDelay(int delay) {
        this.animationDelay = Math.max(1, delay);
        animationTimer.setDelay(this.animationDelay);
    }
    
    public int getAnimationDelay() {
        return animationDelay;
    }
    
    public boolean isAnimating() {
        return animating;
    }
    
    public void setStatusText(String statusText) {
        this.statusText = statusText;
        repaint();
    }
    
    public String getStatusText() {
        return statusText;
    }
    
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBackground(backgroundColor);
        repaint();
    }
}