# Algorithm Visualizer 

A comprehensive Java application that demonstrates various algorithms through interactive visualizations. Perfect for learning algorithms and showcasing programming skills.

![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)

## Features

### Sorting Algorithms
- **Bubble Sort** - O(n²) time complexity with step-by-step comparison visualization
- **Quick Sort** - O(n log n) average case with pivot selection and partitioning
- **Merge Sort** - O(n log n) divide-and-conquer approach with merge visualization
- **Heap Sort** - O(n log n) heap-based sorting with heap construction visualization

### Pathfinding Algorithms
- **A\* Algorithm** - Optimal pathfinding with heuristic guidance and f-cost visualization
- **Dijkstra's Algorithm** - Guaranteed shortest path with distance visualization  
- **Breadth-First Search (BFS)** - Level-by-level exploration with optimal unweighted paths
- Interactive grid with mouse controls for walls, start, and end positions
- Real-time visualization of algorithm exploration and final path highlighting

### Graph Traversal Algorithms
- **Depth-First Search (DFS)** - Deep exploration with recursive and iterative implementations
- **Breadth-First Search (BFS)** - Level-by-level exploration with discovery/finish times
- **Topological Sort** - Dependency ordering with Kahn's algorithm and DFS approaches
- Interactive graph creation with add/edit/delete nodes and edges
- Real-time visualization of discovery times, finish times, and traversal paths
- Support for both directed and undirected graphs

## Getting Started

### Prerequisites
- **Java 11** or higher
- **Maven 3.6** or higher
- **Git** (for cloning the repository)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/devrajsinhp73-afk/algorithm-visualizer.git
   cd algorithm-visualizer
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn exec:java
   ```

   Or alternatively:
   ```bash
   java -cp target/classes com.algorithmvisualizer.AlgorithmVisualizerApp
   ```

4. **Create executable JAR**
   ```bash
   mvn clean package
   java -jar target/algorithm-visualizer-1.0.0.jar
   ```

## Usage

### Pathfinding Algorithms
1. **Select an Algorithm**: Choose from A*, Dijkstra's Algorithm, or Breadth-First Search
2. **Set Up the Grid**:
   - **Set Start**: Click button then click on grid to place start position
   - **Set End**: Click button then click on grid to place end position
   - **Create Walls**: Left-click and drag to toggle walls
   - **Generate Maze**: Create random obstacle patterns
3. **Control Pathfinding**:
   - **Find Path**: Start the pathfinding visualization
   - **Pause**: Stop the current search
   - **Reset**: Clear search results but keep walls
   - **Clear All**: Reset entire grid
4. **Adjust Settings**:
   - **Speed**: Control animation speed (1-10)

### Graph Traversal Algorithms
1. **Select an Algorithm**: Choose from DFS (Recursive/Iterative), BFS, or Topological Sort
2. **Build Your Graph**:
   - **Add Node**: Click button then click on canvas to create nodes
   - **Add Edge**: Click button then click two nodes to connect them
   - **Select/Move**: Click button then drag nodes to reposition
   - **Delete**: Click button then click nodes or edges to remove them
3. **Control Traversal**:
   - **Start Traversal**: Begin the algorithm visualization
   - **Pause/Resume**: Control animation playback
   - **Reset**: Clear traversal results but keep graph structure
   - **Clear Graph**: Remove all nodes and edges
4. **Graph Options**:
   - **Directed Graph**: Toggle between directed and undirected graphs
   - **Speed**: Control animation speed (1-10)

### Color Coding

#### Sorting Algorithms
- 🔴 **Red**: Elements being compared
- 🔵 **Blue**: Elements in comparison or being moved
- 🟢 **Green**: Elements being swapped or in correct position
- 🟡 **Yellow**: Range being processed
- 🟠 **Orange**: Pivot elements or sorted elements

#### Pathfinding Algorithms
- ⚪ **White**: Empty/walkable cells
- ⚫ **Black**: Wall/obstacle cells
- 🟢 **Green**: Start position (S)
- 🔴 **Red**: End/target position (E)
- 🟡 **Yellow**: Currently exploring (frontier)
- 🔘 **Light Gray**: Visited cells
- 🔵 **Blue**: Final path
- 🟠 **Orange**: In algorithm queue/frontier

#### Graph Traversal Algorithms
- ⚪ **White**: Unvisited nodes
- 🟡 **Yellow**: Currently exploring nodes
- 🟢 **Green**: Finished/visited nodes
- 🔵 **Blue**: Traversed edges
- **Numbers**: Discovery time / Finish time (e.g., "2/7")
- 🔴 **Red**: Back edges (cycle detection)
- 🟠 **Orange**: Cross or forward edges

## Project Structure

```
algorithm-visualizer/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/algorithmvisualizer/
│   │           ├── AlgorithmVisualizerApp.java      # Main application entry point
│   │           ├── algorithms/
│   │           │   ├── SortingAlgorithm.java        # Sorting interface
│   │           │   ├── PathfindingAlgorithm.java    # Pathfinding interface
│   │           │   ├── GraphTraversalAlgorithm.java # Graph traversal interface
│   │           │   ├── sorting/
│   │           │   │   ├── BubbleSort.java          # Bubble sort implementation
│   │           │   │   ├── QuickSort.java           # Quick sort implementation
│   │           │   │   ├── MergeSort.java           # Merge sort implementation
│   │           │   │   └── HeapSort.java            # Heap sort implementation
│   │           │   ├── pathfinding/
│   │           │   │   ├── AStarAlgorithm.java      # A* pathfinding
│   │           │   │   ├── DijkstraAlgorithm.java   # Dijkstra's algorithm
│   │           │   │   └── BreadthFirstSearchAlgorithm.java # BFS pathfinding
│   │           │   └── graph/
│   │           │       ├── BreadthFirstSearchAlgorithm.java # BFS graph traversal
│   │           │       ├── DepthFirstSearchAlgorithm.java   # DFS graph traversal
│   │           │       └── TopologicalSortAlgorithm.java    # Topological sorting
│   │           ├── models/
│   │           │   ├── AlgorithmType.java           # Algorithm categorization
│   │           │   ├── VisualizationElement.java    # Element representation
│   │           │   ├── graph/
│   │           │   │   ├── Graph.java               # Graph data structure
│   │           │   │   ├── GraphNode.java           # Graph node representation
│   │           │   │   └── GraphEdge.java           # Graph edge representation
│   │           │   └── pathfinding/
│   │           │       ├── PathfindingGrid.java     # Grid for pathfinding
│   │           │       ├── GridCell.java            # Individual grid cell
│   │           │       └── CellType.java            # Cell type enumeration
│   │           └── ui/
│   │               ├── MainWindow.java              # Main application window
│   │               ├── VisualizationCanvas.java     # Sorting visualization canvas
│   │               ├── PathfindingCanvas.java       # Pathfinding grid canvas
│   │               ├── components/
│   │               │   └── GraphCanvas.java         # Interactive graph canvas
│   │               └── panels/
│   │                   ├── SortingPanel.java       # Sorting controls and display
│   │                   ├── PathfindingPanel.java   # Pathfinding controls and grid
│   │                   └── GraphTraversalPanel.java # Graph traversal controls
│   └── test/
│       └── java/
│           └── com/algorithmvisualizer/
│               └── SortingAlgorithmsTest.java       # Unit tests
├── pom.xml                                          # Maven configuration
└── README.md                                        # This file
```

## Testing

Run the unit tests:
```bash
mvn test
```

The tests verify that all sorting algorithms correctly sort arrays and handle edge cases like empty arrays and single elements.

## Technical Highlights

### Design Patterns Used
- **Strategy Pattern**: Interchangeable sorting algorithms
- **Observer Pattern**: Animation callbacks and UI updates
- **Template Method**: Common sorting visualization structure
- **Factory Pattern**: Algorithm instantiation

### Key Technologies
- **Java Swing**: Modern UI with FlatLaf look and feel
- **Multithreading**: SwingWorker for non-blocking animations
- **Thread-Safe Collections**: CopyOnWriteArrayList for concurrent access
- **Maven**: Dependency management and build automation
- **JUnit 5**: Comprehensive unit testing

### Performance Considerations
- Efficient rendering with double buffering
- Memory usage monitoring in status bar
- Configurable animation delays for smooth performance
- Proper cleanup and resource management


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Inspired by various algorithm visualization tools
- Built with Java Swing for cross-platform compatibility
- Uses FlatLaf for modern look and feel
- Educational resource for computer science students

