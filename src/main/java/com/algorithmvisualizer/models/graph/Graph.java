package com.algorithmvisualizer.models.graph;

import java.awt.Point;
import java.util.*;

/**
 * Represents a graph data structure for visualization.
 * Supports both directed and undirected graphs with weighted edges.
 */
public class Graph {
    private final Map<String, GraphNode> nodes;
    private final List<GraphEdge> edges;
    private final Map<GraphNode, List<GraphEdge>> adjacencyList;
    private boolean directed;
    private String name;
    
    public Graph() {
        this(false);
    }
    
    public Graph(boolean directed) {
        this.nodes = new HashMap<>();
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        this.directed = directed;
        this.name = "Graph";
    }
    
    // Getters
    public Map<String, GraphNode> getNodes() { return new HashMap<>(nodes); }
    public List<GraphEdge> getEdges() { return new ArrayList<>(edges); }
    public boolean isDirected() { return directed; }
    public String getName() { return name; }
    public int getNodeCount() { return nodes.size(); }
    public int getEdgeCount() { return edges.size(); }
    
    // Setters
    public void setDirected(boolean directed) { this.directed = directed; }
    public void setName(String name) { this.name = name; }
    
    /**
     * Adds a node to the graph.
     */
    public GraphNode addNode(String id, int x, int y) {
        if (nodes.containsKey(id)) {
            return nodes.get(id);
        }
        
        GraphNode node = new GraphNode(id, x, y);
        nodes.put(id, node);
        adjacencyList.put(node, new ArrayList<>());
        return node;
    }
    
    /**
     * Adds a node to the graph.
     */
    public GraphNode addNode(String id, Point position) {
        return addNode(id, position.x, position.y);
    }
    
    /**
     * Removes a node from the graph.
     */
    public boolean removeNode(String id) {
        GraphNode node = nodes.get(id);
        if (node == null) return false;
        
        // Remove all edges connected to this node
        List<GraphEdge> edgesToRemove = new ArrayList<>();
        for (GraphEdge edge : edges) {
            if (edge.isIncidentTo(node)) {
                edgesToRemove.add(edge);
            }
        }
        
        for (GraphEdge edge : edgesToRemove) {
            removeEdge(edge);
        }
        
        // Remove node
        nodes.remove(id);
        adjacencyList.remove(node);
        
        return true;
    }
    
    /**
     * Gets a node by its ID.
     */
    public GraphNode getNode(String id) {
        return nodes.get(id);
    }
    
    /**
     * Adds an edge between two nodes.
     */
    public GraphEdge addEdge(String sourceId, String targetId) {
        return addEdge(sourceId, targetId, 1.0);
    }
    
    /**
     * Adds a weighted edge between two nodes.
     */
    public GraphEdge addEdge(String sourceId, String targetId, double weight) {
        GraphNode source = nodes.get(sourceId);
        GraphNode target = nodes.get(targetId);
        
        if (source == null || target == null) {
            return null;
        }
        
        return addEdge(source, target, weight);
    }
    
    /**
     * Adds an edge between two nodes.
     */
    public GraphEdge addEdge(GraphNode source, GraphNode target) {
        return addEdge(source, target, 1.0);
    }
    
    /**
     * Adds a weighted edge between two nodes.
     */
    public GraphEdge addEdge(GraphNode source, GraphNode target, double weight) {
        // Check if edge already exists
        for (GraphEdge edge : edges) {
            if (edge.connects(source, target)) {
                return edge;
            }
        }
        
        GraphEdge edge = new GraphEdge(source, target, directed, weight);
        edges.add(edge);
        
        // Update adjacency list
        adjacencyList.get(source).add(edge);
        if (!directed) {
            adjacencyList.get(target).add(edge);
        }
        
        return edge;
    }
    
    /**
     * Removes an edge from the graph.
     */
    public boolean removeEdge(GraphEdge edge) {
        if (!edges.remove(edge)) {
            return false;
        }
        
        // Update adjacency list
        adjacencyList.get(edge.getSource()).remove(edge);
        if (!directed) {
            adjacencyList.get(edge.getTarget()).remove(edge);
        }
        
        return true;
    }
    
    /**
     * Removes an edge between two nodes.
     */
    public boolean removeEdge(String sourceId, String targetId) {
        GraphNode source = nodes.get(sourceId);
        GraphNode target = nodes.get(targetId);
        
        if (source == null || target == null) {
            return false;
        }
        
        return removeEdge(source, target);
    }
    
    /**
     * Removes an edge between two nodes.
     */
    public boolean removeEdge(GraphNode source, GraphNode target) {
        for (GraphEdge edge : edges) {
            if (edge.connects(source, target)) {
                return removeEdge(edge);
            }
        }
        return false;
    }
    
    /**
     * Gets all neighbors of a node.
     */
    public List<GraphNode> getNeighbors(GraphNode node) {
        List<GraphNode> neighbors = new ArrayList<>();
        List<GraphEdge> nodeEdges = adjacencyList.get(node);
        
        if (nodeEdges != null) {
            for (GraphEdge edge : nodeEdges) {
                GraphNode neighbor = edge.getOther(node);
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            }
        }
        
        return neighbors;
    }
    
    /**
     * Gets all edges incident to a node.
     */
    public List<GraphEdge> getIncidentEdges(GraphNode node) {
        return new ArrayList<>(adjacencyList.getOrDefault(node, new ArrayList<>()));
    }
    
    /**
     * Gets the edge between two nodes if it exists.
     */
    public GraphEdge getEdge(GraphNode source, GraphNode target) {
        for (GraphEdge edge : edges) {
            if (edge.connects(source, target)) {
                return edge;
            }
        }
        return null;
    }
    
    /**
     * Resets all nodes and edges for a new traversal.
     */
    public void reset() {
        for (GraphNode node : nodes.values()) {
            node.reset();
        }
        for (GraphEdge edge : edges) {
            edge.reset();
        }
    }
    
    /**
     * Clears the entire graph.
     */
    public void clear() {
        nodes.clear();
        edges.clear();
        adjacencyList.clear();
    }
    
    /**
     * Checks if the graph is empty.
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }
    
    /**
     * Creates a sample graph for demonstration.
     */
    public void createSampleGraph() {
        clear();
        
        // Add nodes in a circular layout
        int centerX = 300;
        int centerY = 200;
        int radius = 120;
        String[] nodeIds = {"A", "B", "C", "D", "E", "F"};
        
        for (int i = 0; i < nodeIds.length; i++) {
            double angle = 2 * Math.PI * i / nodeIds.length;
            int x = centerX + (int)(radius * Math.cos(angle));
            int y = centerY + (int)(radius * Math.sin(angle));
            addNode(nodeIds[i], x, y);
        }
        
        // Add edges to create an interesting graph
        addEdge("A", "B");
        addEdge("A", "C");
        addEdge("B", "D");
        addEdge("C", "D");
        addEdge("C", "E");
        addEdge("D", "E");
        addEdge("D", "F");
        addEdge("E", "F");
        
        if (directed) {
            addEdge("F", "A"); // Creates a cycle in directed graph
        }
    }
    
    /**
     * Gets all nodes as a list.
     */
    public List<GraphNode> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }
    
    /**
     * Gets all edges as a list.
     */
    public List<GraphEdge> getAllEdges() {
        return new ArrayList<>(edges);
    }
    
    /**
     * Finds a node at the given position.
     */
    public GraphNode getNodeAt(Point point) {
        return getNodeAt(point.x, point.y);
    }
    
    /**
     * Finds a node at the given coordinates.
     */
    public GraphNode getNodeAt(int x, int y) {
        for (GraphNode node : nodes.values()) {
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }
    
    /**
     * Gets all nodes as a collection.
     */
    public Collection<GraphNode> getNodeCollection() {
        return nodes.values();
    }
    
    /**
     * Resets all visualization states in the graph.
     */
    public void resetVisualization() {
        for (GraphNode node : nodes.values()) {
            node.resetVisualization();
        }
        
        for (GraphEdge edge : edges) {
            edge.resetVisualization();
        }
    }
    
    /**
     * Gets basic statistics about the graph.
     */
    public String getStatistics() {
        return String.format("Nodes: %d, Edges: %d, Type: %s", 
            getNodeCount(), getEdgeCount(), directed ? "Directed" : "Undirected");
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s]", name, getStatistics());
    }
}