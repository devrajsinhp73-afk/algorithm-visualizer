package com.algorithmvisualizer;

import com.algorithmvisualizer.algorithms.sorting.BubbleSort;
import com.algorithmvisualizer.algorithms.sorting.QuickSort;
import com.algorithmvisualizer.algorithms.sorting.MergeSort;
import com.algorithmvisualizer.algorithms.sorting.HeapSort;
import com.algorithmvisualizer.models.VisualizationElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for sorting algorithms.
 */
public class SortingAlgorithmsTest {
    
    private VisualizationElement[] testArray;
    private VisualizationElement[] sortedArray;
    
    @BeforeEach
    public void setUp() {
        // Create test array: [5, 2, 8, 1, 9]
        testArray = new VisualizationElement[] {
            new VisualizationElement(5),
            new VisualizationElement(2),
            new VisualizationElement(8),
            new VisualizationElement(1),
            new VisualizationElement(9)
        };
        
        // Expected sorted result: [1, 2, 5, 8, 9]
        sortedArray = new VisualizationElement[] {
            new VisualizationElement(1),
            new VisualizationElement(2),
            new VisualizationElement(5),
            new VisualizationElement(8),
            new VisualizationElement(9)
        };
    }
    
    @Test
    public void testBubbleSort() {
        BubbleSort bubbleSort = new BubbleSort();
        bubbleSort.sort(testArray, (arr, step) -> {});
        
        assertArrayEquals(getValues(sortedArray), getValues(testArray));
    }
    
    @Test
    public void testQuickSort() {
        QuickSort quickSort = new QuickSort();
        quickSort.sort(testArray, (arr, step) -> {});
        
        assertArrayEquals(getValues(sortedArray), getValues(testArray));
    }
    
    @Test
    public void testMergeSort() {
        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(testArray, (arr, step) -> {});
        
        assertArrayEquals(getValues(sortedArray), getValues(testArray));
    }
    
    @Test
    public void testHeapSort() {
        HeapSort heapSort = new HeapSort();
        heapSort.sort(testArray, (arr, step) -> {});
        
        assertArrayEquals(getValues(sortedArray), getValues(testArray));
    }
    
    @Test
    public void testEmptyArray() {
        VisualizationElement[] emptyArray = new VisualizationElement[0];
        
        BubbleSort bubbleSort = new BubbleSort();
        assertDoesNotThrow(() -> bubbleSort.sort(emptyArray, (arr, step) -> {}));
    }
    
    @Test
    public void testSingleElement() {
        VisualizationElement[] singleElement = { new VisualizationElement(42) };
        
        QuickSort quickSort = new QuickSort();
        quickSort.sort(singleElement, (arr, step) -> {});
        
        assertEquals(42, singleElement[0].getValue());
    }
    
    private int[] getValues(VisualizationElement[] array) {
        int[] values = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            values[i] = array[i].getValue();
        }
        return values;
    }
}