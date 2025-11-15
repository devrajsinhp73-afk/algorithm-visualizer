package com.algorithmvisualizer.algorithms.graph;

import com.algorithmvisualizer.algorithms.GraphTraversalAlgorithm;
import com.algorithmvisualizer.models.graph.Graph;
import com.algorithmvisualizer.models.graph.GraphNode;
import com.algorithmvisualizer.models.graph.GraphEdge;

import java.awt.Color;
import java.util.*;

/**
 * Breadth-First Search implementation with visualization.
 * Explores nodes level by level using a queue.
 * Time Complexity: O(V + E)
 * Space Complexity: O(V)
 * Finds shortest path in unweighted graphs.
 */
public class BreadthFirstSearchAlgorithm implements GraphTraversalAlgorithm {
    
    private int levelCounter;
    
    @Override
    public List<GraphNode> traverse(Graph graph, GraphNode startNode, GraphTraversalCallback callback) {
        if (startNode == null) {
            callback.onStep(graph, "No starting node specified!", null, new ArrayList<>());
            return new ArrayList<>();
        }
        
        List<GraphNode> visitedOrder = new ArrayList<>();
        Set<GraphNode> visited = new HashSet<>();
        Queue<GraphNode> queue = new LinkedList<>();
        Map<GraphNode, Integer> levels = new HashMap<>();
        
        levelCounter = 0;
        
        // Initialize BFS
        queue.offer(startNode);
        visited.add(startNode);
        levels.put(startNode, 0);
        startNode.setDiscoveryTime(0);
        startNode.markExploring();
        
        callback.onStep(graph, "Starting BFS from node " + startNode.getId() + 
            " (Level 0)", startNode, visitedOrder);
        
        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            visitedOrder.add(current);
            current.setVisited(true);
            
            int currentLevel = levels.get(current);
            
            callback.onStep(graph, "Processing node " + current.getId() + 
                " at level " + currentLevel, current, visitedOrder);
            
            // Explore all unvisited neighbors
            List<GraphNode> neighbors = graph.getNeighbors(current);
            
            // Sort neighbors for consistent visualization
            neighbors.sort(Comparator.comparing(GraphNode::getId));
            
            for (GraphNode neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                    
                    // Set level and parent
                    levels.put(neighbor, currentLevel + 1);
                    neighbor.setDiscoveryTime(currentLevel + 1);
                    neighbor.setParent(current);
                    neighbor.markExploring();
                    
                    // Highlight the edge
                    GraphEdge edge = graph.getEdge(current, neighbor);
                    if (edge != null) {
                        edge.setTraversed(true);
                    }
                    
                    callback.onStep(graph, "Discovered node " + neighbor.getId() + 
                        " at level " + (currentLevel + 1) + " via " + current.getId(), 
                        neighbor, visitedOrder);
                        
                } else if (!visitedOrder.contains(neighbor)) {
                    // Already discovered but not yet processed (cross edge in BFS tree)
                    callback.onStep(graph, "Cross edge from " + current.getId() + 
                        " to " + neighbor.getId() + " (already discovered)", neighbor, visitedOrder);
                }
            }
            
            // Mark current node as finished
            current.markFinished();
            
            callback.onStep(graph, "Finished processing node " + current.getId(), current, visitedOrder);
        }
        
        callback.onStep(graph, "BFS traversal complete! Visited " + visitedOrder.size() + 
            " nodes in " + (Collections.max(levels.values()) + 1) + " levels.", null, visitedOrder);
        
        return visitedOrder;
    }
    
    /**
     * Finds shortest path from start to target node (unweighted graph).
     */
    public List<GraphNode> findShortestPath(Graph graph, GraphNode startNode, GraphNode targetNode, 
                                           GraphTraversalCallback callback) {
        if (startNode == null || targetNode == null) {
            callback.onStep(graph, "Start or target node is null!", null, new ArrayList<>());
            return new ArrayList<>();
        }
        
        Set<GraphNode> visited = new HashSet<>();
        Queue<GraphNode> queue = new LinkedList<>();
        Map<GraphNode, GraphNode> parent = new HashMap<>();
        
        queue.offer(startNode);
        visited.add(startNode);
        parent.put(startNode, null);
        startNode.markExploring();
        
        callback.onStep(graph, "Searching for shortest path from " + startNode.getId() + 
            " to " + targetNode.getId(), startNode, new ArrayList<>());
        
        boolean found = false;
        
        while (!queue.isEmpty() && !found) {
            GraphNode current = queue.poll();
            current.setVisited(true);
            
            callback.onStep(graph, "Exploring from node " + current.getId(), current, new ArrayList<>());
            
            if (current.equals(targetNode)) {
                found = true;
                callback.onStep(graph, "Target node " + targetNode.getId() + " found!", 
                    current, new ArrayList<>());
                break;
            }
            
            List<GraphNode> neighbors = graph.getNeighbors(current);
            neighbors.sort(Comparator.comparing(GraphNode::getId));
            
            for (GraphNode neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                    parent.put(neighbor, current);
                    neighbor.markExploring();
                    
                    GraphEdge edge = graph.getEdge(current, neighbor);
                    if (edge != null) {
                        edge.setHighlighted(true);
                    }
                    
                    callback.onStep(graph, "Discovered " + neighbor.getId() + 
                        " via " + current.getId(), neighbor, new ArrayList<>());
                }
            }
            
            current.markFinished();
        }
        
        // Reconstruct path
        List<GraphNode> path = new ArrayList<>();
        if (found) {
            GraphNode node = targetNode;
            while (node != null) {
                path.add(0, node);
                node = parent.get(node);
            }
            
            // Highlight the path
            for (int i = 0; i < path.size() - 1; i++) {
                GraphEdge edge = graph.getEdge(path.get(i), path.get(i + 1));
                if (edge != null) {
                    edge.setTraversed(true);
                    edge.setColor(Color.GREEN);
                }
            }
            
            callback.onStep(graph, "Shortest path found! Length: " + (path.size() - 1) + 
                " edges", targetNode, path);
        } else {
            callback.onStep(graph, "No path exists from " + startNode.getId() + 
                " to " + targetNode.getId(), null, path);
        }
        
        return path;
    }
    
    @Override
    public String getName() {
        return "Breadth-First Search";
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
        return "BFS explores nodes level by level using a queue. " +
               "It finds the shortest path in unweighted graphs and " +
               "can detect bipartiteness. The algorithm visits nodes " +
               "in order of their distance from the start node.";
    }
    
    @Override
    public boolean producesOrdering() {
        return true; // BFS produces level-order traversal
    }
    
    @Override
    public String toString() {
        return getName();
    }
}