package com.algorithmvisualizer.algorithms.graph;

import com.algorithmvisualizer.algorithms.GraphTraversalAlgorithm;
import com.algorithmvisualizer.models.graph.Graph;
import com.algorithmvisualizer.models.graph.GraphNode;
import com.algorithmvisualizer.models.graph.GraphEdge;

import java.awt.Color;
import java.util.*;

/**
 * Topological Sort implementation with visualization.
 * Produces a linear ordering of vertices in a Directed Acyclic Graph (DAG)
 * such that for every directed edge (u,v), u appears before v in the ordering.
 * Time Complexity: O(V + E)
 * Space Complexity: O(V)
 */
public class TopologicalSortAlgorithm implements GraphTraversalAlgorithm {
    
    private boolean useKahn; // true for Kahn's algorithm, false for DFS-based
    
    public TopologicalSortAlgorithm() {
        this(false); // Default to DFS-based approach
    }
    
    public TopologicalSortAlgorithm(boolean useKahn) {
        this.useKahn = useKahn;
    }
    
    @Override
    public List<GraphNode> traverse(Graph graph, GraphNode startNode, GraphTraversalCallback callback) {
        if (!graph.isDirected()) {
            callback.onStep(graph, "Topological sort requires a directed graph!", null, new ArrayList<>());
            return new ArrayList<>();
        }
        
        callback.onStep(graph, "Starting topological sort using " + 
            (useKahn ? "Kahn's algorithm" : "DFS-based approach"), null, new ArrayList<>());
        
        if (useKahn) {
            return topologicalSortKahn(graph, callback);
        } else {
            return topologicalSortDFS(graph, callback);
        }
    }
    
    /**
     * Kahn's algorithm for topological sorting.
     * Uses in-degree counting and removes nodes with zero in-degree.
     */
    private List<GraphNode> topologicalSortKahn(Graph graph, GraphTraversalCallback callback) {
        List<GraphNode> result = new ArrayList<>();
        Queue<GraphNode> queue = new LinkedList<>();
        Map<GraphNode, Integer> inDegree = new HashMap<>();
        
        // Calculate in-degrees
        for (GraphNode node : graph.getNodeCollection()) {
            inDegree.put(node, 0);
        }
        
        for (GraphNode node : graph.getNodeCollection()) {
            for (GraphNode neighbor : graph.getNeighbors(node)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }
        
        callback.onStep(graph, "Calculated in-degrees for all nodes", null, result);
        
        // Find all nodes with in-degree 0
        for (GraphNode node : graph.getNodeCollection()) {
            if (inDegree.get(node) == 0) {
                queue.offer(node);
                node.markExploring();
            }
        }
        
        callback.onStep(graph, "Found " + queue.size() + " nodes with in-degree 0", null, result);
        
        int processedCount = 0;
        
        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            result.add(current);
            current.setVisited(true);
            current.setDiscoveryTime(processedCount++);
            current.markFinished();
            
            callback.onStep(graph, "Added " + current.getId() + " to topological order (position " + 
                (result.size() - 1) + ")", current, result);
            
            // Reduce in-degree of neighbors
            for (GraphNode neighbor : graph.getNeighbors(current)) {
                int newInDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newInDegree);
                
                // Highlight the edge being processed
                GraphEdge edge = graph.getEdge(current, neighbor);
                if (edge != null) {
                    edge.setTraversed(true);
                }
                
                if (newInDegree == 0) {
                    queue.offer(neighbor);
                    neighbor.markExploring();
                    
                    callback.onStep(graph, "In-degree of " + neighbor.getId() + 
                        " reduced to 0, added to queue", neighbor, result);
                } else {
                    callback.onStep(graph, "In-degree of " + neighbor.getId() + 
                        " reduced to " + newInDegree, neighbor, result);
                }
            }
        }
        
        // Check for cycles
        if (result.size() != graph.getNodeCollection().size()) {
            callback.onStep(graph, "Cycle detected! Topological sort impossible. " +
                "Only " + result.size() + " of " + graph.getNodeCollection().size() + " nodes processed.", 
                null, result);
            return new ArrayList<>(); // Return empty list to indicate failure
        }
        
        callback.onStep(graph, "Topological sort completed successfully!", null, result);
        return result;
    }
    
    /**
     * DFS-based topological sorting.
     * Uses finish times from DFS traversal.
     */
    private List<GraphNode> topologicalSortDFS(Graph graph, GraphTraversalCallback callback) {
        List<GraphNode> result = new ArrayList<>();
        Set<GraphNode> visited = new HashSet<>();
        Set<GraphNode> recursionStack = new HashSet<>();
        Stack<GraphNode> finishStack = new Stack<>();
        
        callback.onStep(graph, "Starting DFS-based topological sort", null, result);
        
        // Visit all unvisited nodes
        for (GraphNode node : graph.getNodeCollection()) {
            if (!visited.contains(node)) {
                if (hasCycleDFS(graph, node, visited, recursionStack, finishStack, callback)) {
                    callback.onStep(graph, "Cycle detected starting from node " + node.getId() + 
                        "! Topological sort impossible.", node, result);
                    return new ArrayList<>();
                }
            }
        }
        
        // Pop all nodes from finish stack to get topological order
        int position = 0;
        while (!finishStack.isEmpty()) {
            GraphNode node = finishStack.pop();
            result.add(node);
            node.setDiscoveryTime(position++);
            
            callback.onStep(graph, "Added " + node.getId() + " to topological order (position " + 
                (result.size() - 1) + ")", node, result);
        }
        
        callback.onStep(graph, "DFS-based topological sort completed!", null, result);
        return result;
    }
    
    /**
     * DFS helper method that detects cycles and builds finish stack.
     */
    private boolean hasCycleDFS(Graph graph, GraphNode node, Set<GraphNode> visited,
                               Set<GraphNode> recursionStack, Stack<GraphNode> finishStack,
                               GraphTraversalCallback callback) {
        
        visited.add(node);
        recursionStack.add(node);
        node.markExploring();
        
        callback.onStep(graph, "Visiting node " + node.getId(), node, new ArrayList<>());
        
        // Visit all neighbors
        for (GraphNode neighbor : graph.getNeighbors(node)) {
            GraphEdge edge = graph.getEdge(node, neighbor);
            if (edge != null) {
                edge.setHighlighted(true);
            }
            
            if (recursionStack.contains(neighbor)) {
                // Back edge found - cycle detected
                if (edge != null) {
                    edge.setColor(Color.RED);
                }
                callback.onStep(graph, "Back edge detected: " + node.getId() + 
                    " -> " + neighbor.getId() + " (CYCLE!)", neighbor, new ArrayList<>());
                return true;
            }
            
            if (!visited.contains(neighbor)) {
                if (hasCycleDFS(graph, neighbor, visited, recursionStack, finishStack, callback)) {
                    return true;
                }
            }
        }
        
        // Finished processing this node
        recursionStack.remove(node);
        finishStack.push(node);
        node.markFinished();
        
        callback.onStep(graph, "Finished processing node " + node.getId(), node, new ArrayList<>());
        
        return false;
    }
    
    public void setUseKahn(boolean useKahn) {
        this.useKahn = useKahn;
    }
    
    public boolean isUseKahn() {
        return useKahn;
    }
    
    @Override
    public String getName() {
        return "Topological Sort" + (useKahn ? " (Kahn's Algorithm)" : " (DFS-based)");
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(V + E)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(V)";
    }
    
    @Override
    public String getDescription() {
        String base = "Topological sort produces a linear ordering of vertices in a DAG " +
                     "such that for every directed edge (u,v), u appears before v. ";
        
        if (useKahn) {
            return base + "Kahn's algorithm repeatedly removes nodes with zero in-degree.";
        } else {
            return base + "DFS-based approach uses finish times from depth-first traversal.";
        }
    }
    
    @Override
    public boolean producesOrdering() {
        return true; // Topological sort is all about producing an ordering
    }
    
    @Override
    public String toString() {
        return getName();
    }
}