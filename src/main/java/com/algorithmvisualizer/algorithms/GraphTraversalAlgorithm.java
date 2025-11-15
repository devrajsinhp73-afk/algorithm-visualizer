package com.algorithmvisualizer.algorithms;

import com.algorithmvisualizer.models.graph.Graph;
import com.algorithmvisualizer.models.graph.GraphNode;
import java.util.List;

/**
 * Interface for graph traversal algorithms with visualization callbacks.
 */
public interface GraphTraversalAlgorithm {
    
    /**
     * Traverses the graph starting from the given node and calls the callback for each step.
     * 
     * @param graph the graph to traverse
     * @param startNode the starting node for traversal
     * @param callback callback function called after each step
     * @return the order of nodes visited during traversal
     */
    List<GraphNode> traverse(Graph graph, GraphNode startNode, GraphTraversalCallback callback);
    
    /**
     * Gets the name of the traversal algorithm.
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
     * Whether this algorithm produces a specific ordering (like topological sort).
     * 
     * @return true if the algorithm produces a meaningful ordering
     */
    boolean producesOrdering();
    
    /**
     * Callback interface for graph traversal visualization steps.
     */
    @FunctionalInterface
    interface GraphTraversalCallback {
        /**
         * Called after each step of the graph traversal algorithm.
         * 
         * @param graph current state of the graph
         * @param step step description
         * @param currentNode the node currently being processed (can be null)
         * @param visitedNodes list of nodes visited so far
         */
        void onStep(Graph graph, String step, GraphNode currentNode, List<GraphNode> visitedNodes);
    }
}