package com.algorithmvisualizer.algorithms.sorting;

import com.algorithmvisualizer.algorithms.SortingAlgorithm;
import com.algorithmvisualizer.models.VisualizationElement;
import java.awt.Color;

/**
 * Quick Sort implementation with visualization.
 * Time Complexity: O(n log n) average, O(n²) worst case
 * Space Complexity: O(log n)
 */
public class QuickSort implements SortingAlgorithm {
    
    private SortingCallback callback;
    
    @Override
    public void sort(VisualizationElement[] array, SortingCallback callback) {
        this.callback = callback;
        resetColors(array);
        quickSort(array, 0, array.length - 1);
        
        // Mark all elements as sorted
        for (VisualizationElement element : array) {
            element.setColor(Color.GREEN);
        }
        callback.onStep(array, "Quick Sort completed!");
    }
    
    private void quickSort(VisualizationElement[] array, int low, int high) {
        if (low < high) {
            // Highlight the range being sorted
            highlightRange(array, low, high, Color.YELLOW);
            callback.onStep(array, "Sorting range [" + low + ", " + high + "]");
            
            int pivotIndex = partition(array, low, high);
            
            // Highlight the pivot
            array[pivotIndex].setColor(Color.ORANGE);
            callback.onStep(array, "Pivot element " + array[pivotIndex].getValue() + " placed at position " + pivotIndex);
            
            // Recursively sort elements before and after partition
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }
    
    private int partition(VisualizationElement[] array, int low, int high) {
        // Choose the rightmost element as pivot
        VisualizationElement pivot = array[high];
        pivot.setColor(Color.RED);
        callback.onStep(array, "Choosing pivot: " + pivot.getValue());
        
        int i = low - 1; // Index of smaller element
        
        for (int j = low; j < high; j++) {
            // Highlight current element being compared
            array[j].setColor(Color.BLUE);
            callback.onStep(array, "Comparing " + array[j].getValue() + " with pivot " + pivot.getValue());
            
            if (array[j].getValue() <= pivot.getValue()) {
                i++;
                
                // Highlight elements being swapped
                if (i != j) {
                    array[i].setColor(Color.GREEN);
                    array[j].setColor(Color.GREEN);
                    callback.onStep(array, "Swapping " + array[i].getValue() + " with " + array[j].getValue());
                    
                    swap(array, i, j);
                }
            }
            
            // Reset color after comparison
            if (i != j) {
                array[j].setColor(Color.LIGHT_GRAY);
            }
        }
        
        // Place pivot in correct position
        array[i + 1].setColor(Color.GREEN);
        callback.onStep(array, "Placing pivot in correct position");
        swap(array, i + 1, high);
        
        return i + 1;
    }
    
    private void swap(VisualizationElement[] array, int i, int j) {
        VisualizationElement temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    private void resetColors(VisualizationElement[] array) {
        for (VisualizationElement element : array) {
            element.setColor(Color.LIGHT_GRAY);
        }
    }
    
    private void highlightRange(VisualizationElement[] array, int start, int end, Color color) {
        for (int i = start; i <= end && i < array.length; i++) {
            if (!array[i].getColor().equals(Color.ORANGE)) { // Don't override pivots
                array[i].setColor(color);
            }
        }
    }
    
    @Override
    public String getName() {
        return "Quick Sort";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n) average, O(n²) worst case";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(log n)";
    }
    
    @Override
    public String getDescription() {
        return "Quick Sort picks a pivot element and partitions the array around it, then recursively sorts the sub-arrays.";
    }
    
    @Override
    public String toString() {
        return getName();
    }
}