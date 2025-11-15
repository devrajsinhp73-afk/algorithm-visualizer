package com.algorithmvisualizer.algorithms.sorting;

import com.algorithmvisualizer.algorithms.SortingAlgorithm;
import com.algorithmvisualizer.models.VisualizationElement;
import java.awt.Color;

/**
 * Heap Sort implementation with visualization.
 * Time Complexity: O(n log n)
 * Space Complexity: O(1)
 */
public class HeapSort implements SortingAlgorithm {
    
    private SortingCallback callback;
    
    @Override
    public void sort(VisualizationElement[] array, SortingCallback callback) {
        this.callback = callback;
        resetColors(array);
        
        int n = array.length;
        
        // Build max heap
        callback.onStep(array, "Building max heap...");
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }
        
        highlightHeap(array, n);
        callback.onStep(array, "Max heap built successfully");
        
        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            array[0].setColor(Color.RED);
            array[i].setColor(Color.BLUE);
            callback.onStep(array, "Moving max element " + array[0].getValue() + " to position " + i);
            
            swap(array, 0, i);
            
            // Mark as sorted
            array[i].setColor(Color.GREEN);
            callback.onStep(array, "Element " + array[i].getValue() + " is now in its final position");
            
            // Restore heap property for the reduced heap
            heapify(array, i, 0);
        }
        
        // Mark the first element as sorted
        if (array.length > 0) {
            array[0].setColor(Color.GREEN);
        }
        
        callback.onStep(array, "Heap Sort completed!");
    }
    
    private void heapify(VisualizationElement[] array, int heapSize, int rootIndex) {
        int largest = rootIndex;
        int leftChild = 2 * rootIndex + 1;
        int rightChild = 2 * rootIndex + 2;
        
        // Highlight the current node being processed
        array[rootIndex].setColor(Color.YELLOW);
        callback.onStep(array, "Heapifying subtree rooted at index " + rootIndex);
        
        // Check if left child exists and is greater than root
        if (leftChild < heapSize) {
            array[leftChild].setColor(Color.CYAN);
            callback.onStep(array, "Comparing with left child at index " + leftChild);
            
            if (array[leftChild].getValue() > array[largest].getValue()) {
                largest = leftChild;
            }
        }
        
        // Check if right child exists and is greater than current largest
        if (rightChild < heapSize) {
            array[rightChild].setColor(Color.MAGENTA);
            callback.onStep(array, "Comparing with right child at index " + rightChild);
            
            if (array[rightChild].getValue() > array[largest].getValue()) {
                largest = rightChild;
            }
        }
        
        // If largest is not root, swap and continue heapifying
        if (largest != rootIndex) {
            array[rootIndex].setColor(Color.RED);
            array[largest].setColor(Color.RED);
            callback.onStep(array, "Swapping " + array[rootIndex].getValue() + " with " + array[largest].getValue());
            
            swap(array, rootIndex, largest);
            
            // Recursively heapify the affected subtree
            heapify(array, heapSize, largest);
        } else {
            // Reset colors if no swap needed
            resetHeapColors(array, heapSize);
        }
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
    
    private void resetHeapColors(VisualizationElement[] array, int heapSize) {
        for (int i = 0; i < heapSize; i++) {
            if (!array[i].getColor().equals(Color.GREEN)) {
                array[i].setColor(Color.LIGHT_GRAY);
            }
        }
    }
    
    private void highlightHeap(VisualizationElement[] array, int heapSize) {
        for (int i = 0; i < heapSize; i++) {
            array[i].setColor(Color.ORANGE);
        }
    }
    
    @Override
    public String getName() {
        return "Heap Sort";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
    
    @Override
    public String getDescription() {
        return "Heap Sort builds a max heap from the array, then repeatedly extracts the maximum element and places it at the end.";
    }
    
    @Override
    public String toString() {
        return getName();
    }
}