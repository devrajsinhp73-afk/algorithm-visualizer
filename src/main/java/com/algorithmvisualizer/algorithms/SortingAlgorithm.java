package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.models.VisualizationElement;

/**
 * Interface for sorting algorithms with visualization callbacks.
 */
public interface SortingAlgorithm {
    
    /**
     * Sorts the array and calls the callback for each step.
     * 
     * @param array the array to sort
     * @param callback callback function called after each step
     */
    void sort(VisualizationElement[] array, SortingCallback callback);
    
    /**
     * Gets the name of the sorting algorithm.
     * 
     * @return the algorithm name
     */
    String getName();
    
    /**
     * Gets the time complexity of the algorithm.
     * 
     * @return time complexity description
     */
    String getTimeComplexity();
    
    /**
     * Gets the space complexity of the algorithm.
     * 
     * @return space complexity description
     */
    String getSpaceComplexity();
    
    /**
     * Gets a description of how the algorithm works.
     * 
     * @return algorithm description
     */
    String getDescription();
    
    /**
     * Callback interface for sorting visualization steps.
     */
    @FunctionalInterface
    interface SortingCallback {
        /**
         * Called after each step of the sorting algorithm.
         * 
         * @param array current state of the array
         * @param step step number or description
         */
        void onStep(VisualizationElement[] array, String step);
    }
}