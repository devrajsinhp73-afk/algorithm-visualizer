package com.algorithmvisualizer.algorithms.graph;

import com.algorithmvisualizer.algorithms.GraphTraversalAlgorithm;
import com.algorithmvisualizer.models.graph.Graph;
import com.algorithmvisualizer.models.graph.GraphNode;
import com.algorithmvisualizer.models.graph.GraphEdge;

import java.awt.Color;
import java.util.*;

/**
 * Depth-First Search implementation with visualization.
 * Explores as far as possible along each branch before backtracking.
 * Time Complexity: O(V + E)
 * Space Complexity: O(V) for recursion stack or explicit stack
 */
public class DepthFirstSearchAlgorithm implements GraphTraversalAlgorithm {
    
    private boolean useRecursive;
    private int timeCounter;
    
    public DepthFirstSearchAlgorithm() {
        this(true); // Default to recursive implementation
    }
    
    public DepthFirstSearchAlgorithm(boolean useRecursive) {
        this.useRecursive = useRecursive;
    }
    
    @Override
    public List<GraphNode> traverse(Graph graph, GraphNode startNode, GraphTraversalCallback callback) {
        if (startNode == null) {
            callback.onStep(graph, "No starting node specified!", null, new ArrayList<>());
            return new ArrayList<>();
        }
        
        List<GraphNode> visitedOrder = new ArrayList<>();
        Set<GraphNode> visited = new HashSet<>();
        timeCounter = 0;
        
        callback.onStep(graph, "Starting DFS from node " + startNode.getId(), startNode, visitedOrder);
        
        if (useRecursive) {
            dfsRecursive(graph, startNode, visited, visitedOrder, callback);
        } else {
            dfsIterative(graph, startNode, visited, visitedOrder, callback);
        }
        
        callback.onStep(graph, "DFS traversal complete! Visited " + visitedOrder.size() + " nodes.", 
            null, visitedOrder);
        
        return visitedOrder;
    }
    
    private void dfsRecursive(Graph graph, GraphNode node, Set<GraphNode> visited, 
                             List<GraphNode> visitedOrder, GraphTraversalCallback callback) {
        
        // Mark node as visited
        visited.add(node);
        visitedOrder.add(node);
        node.setVisited(true);
        node.setDiscoveryTime(timeCounter++);
        node.markExploring();
        
        callback.onStep(graph, "Discovered node " + node.getId() + 
            " (discovery time: " + node.getDiscoveryTime() + ")", node, visitedOrder);
        
        // Explore all unvisited neighbors
        List<GraphNode> neighbors = graph.getNeighbors(node);
        
        // Sort neighbors for consistent visualization
        neighbors.sort(Comparator.comparing(GraphNode::getId));
        
        for (GraphNode neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                // Highlight the edge being traversed
                GraphEdge edge = graph.getEdge(node, neighbor);
                if (edge != null) {
                    edge.setTraversed(true);
                }
                
                callback.onStep(graph, "Exploring edge from " + node.getId() + 
                    " to " + neighbor.getId(), neighbor, visitedOrder);
                
                // Set parent for tree construction
                neighbor.setParent(node);
                
                // Recursive call
                dfsRecursive(graph, neighbor, visited, visitedOrder, callback);
            } else {
                callback.onStep(graph, "Node " + neighbor.getId() + 
                    " already visited (back edge from " + node.getId() + ")", neighbor, visitedOrder);
            }
        }
        
        // Mark node as finished
        node.setFinishTime(timeCounter++);
        node.markFinished();
        
        callback.onStep(graph, "Finished processing node " + node.getId() + 
            " (finish time: " + node.getFinishTime() + ")", node, visitedOrder);
    }
    
    private void dfsIterative(Graph graph, GraphNode startNode, Set<GraphNode> visited,
                             List<GraphNode> visitedOrder, GraphTraversalCallback callback) {
        
        Stack<GraphNode> stack = new Stack<>();
        stack.push(startNode);
        
        while (!stack.isEmpty()) {
            GraphNode current = stack.pop();
            
            if (!visited.contains(current)) {
                // Mark node as visited
                visited.add(current);
                visitedOrder.add(current);
                current.setVisited(true);
                current.setDiscoveryTime(timeCounter++);
                current.markExploring();
                
                callback.onStep(graph, "Discovered node " + current.getId() + 
                    " (discovery time: " + current.getDiscoveryTime() + ")", current, visitedOrder);
                
                // Get neighbors and add them to stack (in reverse order for consistent traversal)
                List<GraphNode> neighbors = graph.getNeighbors(current);
                neighbors.sort(Comparator.comparing(GraphNode::getId));
                Collections.reverse(neighbors); // Reverse for stack order
                
                for (GraphNode neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                        
                        // Highlight the edge
                        GraphEdge edge = graph.getEdge(current, neighbor);
                        if (edge != null) {
                            edge.setHighlighted(true);
                        }
                        
                        // Set parent for tree construction
                        if (neighbor.getParent() == null) {
                            neighbor.setParent(current);
                        }
                        
                        callback.onStep(graph, "Added " + neighbor.getId() + 
                            " to stack from " + current.getId(), neighbor, visitedOrder);
                    }
                }
                
                // Mark node as finished after processing neighbors
                current.setFinishTime(timeCounter++);
                current.markFinished();
                
                callback.onStep(graph, "Finished processing node " + current.getId(), current, visitedOrder);
            }
        }
    }
    
    public void setUseRecursive(boolean useRecursive) {
        this.useRecursive = useRecursive;
    }
    
    public boolean isUseRecursive() {
        return useRecursive;
    }
    
    @Override
    public String getName() {
        return "Depth-First Search" + (useRecursive ? " (Recursive)" : " (Iterative)");
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(V + E)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return useRecursive ? "O(V) recursion stack" : "O(V) explicit stack";
    }
    
    @Override
    public String getDescription() {
        return "DFS explores as far as possible along each branch before backtracking. " +
               "It can detect cycles and classify edges in directed graphs. " +
               (useRecursive ? "Uses recursion for elegant implementation." : 
                              "Uses explicit stack to avoid recursion limits.");
    }
    
    @Override
    public boolean producesOrdering() {
        return true; // DFS produces a valid ordering with discovery/finish times
    }
    
    @Override
    public String toString() {
        return getName();
    }
}