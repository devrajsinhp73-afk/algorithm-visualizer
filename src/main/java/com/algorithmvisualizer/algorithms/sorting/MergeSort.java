package com.algorithmvisualizer.algorithms.sorting;

import com.algorithmvisualizer.algorithms.SortingAlgorithm;
import com.algorithmvisualizer.models.VisualizationElement;
import java.awt.Color;
import java.util.Arrays;

/**
 * Merge Sort implementation with visualization.
 * Time Complexity: O(n log n)
 * Space Complexity: O(n)
 */
public class MergeSort implements SortingAlgorithm {
    
    private SortingCallback callback;
    
    @Override
    public void sort(VisualizationElement[] array, SortingCallback callback) {
        this.callback = callback;
        resetColors(array);
        mergeSort(array, 0, array.length - 1);
        
        // Mark all elements as sorted
        for (VisualizationElement element : array) {
            element.setColor(Color.GREEN);
        }
        callback.onStep(array, "Merge Sort completed!");
    }
    
    private void mergeSort(VisualizationElement[] array, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            
            // Highlight the range being divided
            highlightRange(array, left, right, Color.YELLOW);
            callback.onStep(array, "Dividing range [" + left + ", " + right + "] at position " + middle);
            
            // Sort first and second halves
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            
            // Merge the sorted halves
            merge(array, left, middle, right);
        }
    }
    
    private void merge(VisualizationElement[] array, int left, int middle, int right) {
        // Create temporary arrays for the two halves
        int leftSize = middle - left + 1;
        int rightSize = right - middle;
        
        VisualizationElement[] leftArray = new VisualizationElement[leftSize];
        VisualizationElement[] rightArray = new VisualizationElement[rightSize];
        
        // Copy data to temporary arrays
        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = new VisualizationElement(array[left + i].getValue());
            leftArray[i].setColor(Color.CYAN);
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = new VisualizationElement(array[middle + 1 + j].getValue());
            rightArray[j].setColor(Color.MAGENTA);
        }
        
        // Highlight the arrays being merged
        highlightRange(array, left, middle, Color.CYAN);
        highlightRange(array, middle + 1, right, Color.MAGENTA);
        callback.onStep(array, "Merging ranges [" + left + ", " + middle + "] and [" + (middle + 1) + ", " + right + "]");
        
        // Merge the temporary arrays back
        int i = 0, j = 0, k = left;
        
        while (i < leftSize && j < rightSize) {
            if (leftArray[i].getValue() <= rightArray[j].getValue()) {
                array[k].setValue(leftArray[i].getValue());
                array[k].setColor(Color.GREEN);
                callback.onStep(array, "Placing " + leftArray[i].getValue() + " from left array at position " + k);
                i++;
            } else {
                array[k].setValue(rightArray[j].getValue());
                array[k].setColor(Color.GREEN);
                callback.onStep(array, "Placing " + rightArray[j].getValue() + " from right array at position " + k);
                j++;
            }
            k++;
        }
        
        // Copy remaining elements
        while (i < leftSize) {
            array[k].setValue(leftArray[i].getValue());
            array[k].setColor(Color.GREEN);
            callback.onStep(array, "Placing remaining " + leftArray[i].getValue() + " from left array at position " + k);
            i++;
            k++;
        }
        
        while (j < rightSize) {
            array[k].setValue(rightArray[j].getValue());
            array[k].setColor(Color.GREEN);
            callback.onStep(array, "Placing remaining " + rightArray[j].getValue() + " from right array at position " + k);
            j++;
            k++;
        }
        
        // Highlight the merged range
        highlightRange(array, left, right, Color.ORANGE);
        callback.onStep(array, "Merged range [" + left + ", " + right + "]");
    }
    
    private void resetColors(VisualizationElement[] array) {
        for (VisualizationElement element : array) {
            element.setColor(Color.LIGHT_GRAY);
        }
    }
    
    private void highlightRange(VisualizationElement[] array, int start, int end, Color color) {
        for (int i = start; i <= end && i < array.length; i++) {
            array[i].setColor(color);
        }
    }
    
    @Override
    public String getName() {
        return "Merge Sort";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }
    
    @Override
    public String getDescription() {
        return "Merge Sort divides the array into halves, recursively sorts them, and then merges the sorted halves.";
    }
    
    @Override
    public String toString() {
        return getName();
    }
}