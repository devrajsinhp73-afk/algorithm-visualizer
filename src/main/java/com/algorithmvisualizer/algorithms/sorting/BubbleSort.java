package com.algorithmvisualizer.algorithms.sorting;

import com.algorithmvisualizer.algorithms.SortingAlgorithm;
import com.algorithmvisualizer.models.VisualizationElement;
import java.awt.Color;

/**
 * Bubble Sort implementation with visualization.
 * Time Complexity: O(n²)
 * Space Complexity: O(1)
 */
public class BubbleSort implements SortingAlgorithm {
    
    @Override
    public void sort(VisualizationElement[] array, SortingCallback callback) {
        int n = array.length;
        
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            
            for (int j = 0; j < n - i - 1; j++) {
                // Highlight elements being compared
                resetColors(array);
                array[j].setColor(Color.RED);
                array[j + 1].setColor(Color.BLUE);
                
                callback.onStep(array, "Comparing elements at positions " + j + " and " + (j + 1));
                
                if (array[j].getValue() > array[j + 1].getValue()) {
                    // Swap elements
                    swap(array, j, j + 1);
                    swapped = true;
                    
                    // Show swap
                    array[j].setColor(Color.GREEN);
                    array[j + 1].setColor(Color.GREEN);
                    callback.onStep(array, "Swapped elements at positions " + j + " and " + (j + 1));
                }
            }
            
            // Mark the last element of this pass as sorted
            array[n - i - 1].setColor(Color.ORANGE);
            callback.onStep(array, "Element at position " + (n - i - 1) + " is now in its final position");
            
            if (!swapped) {
                // Array is already sorted
                break;
            }
        }
        
        // Mark first element as sorted
        if (array.length > 0) {
            array[0].setColor(Color.ORANGE);
        }
        
        callback.onStep(array, "Bubble Sort completed!");
    }
    
    private void swap(VisualizationElement[] array, int i, int j) {
        VisualizationElement temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    private void resetColors(VisualizationElement[] array) {
        for (VisualizationElement element : array) {
            if (!element.getColor().equals(Color.ORANGE)) { // Don't reset sorted elements
                element.setColor(Color.LIGHT_GRAY);
            }
        }
    }
    
    @Override
    public String getName() {
        return "Bubble Sort";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
    
    @Override
    public String getDescription() {
        return "Bubble Sort repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order.";
    }
    
    @Override
    public String toString() {
        return getName();
    }
}