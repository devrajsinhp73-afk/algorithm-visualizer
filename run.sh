#!/bin/bash

echo "Algorithm Visualizer - Build and Run Script"
echo "=========================================="

# Create output directory
mkdir -p target/classes

# Download FlatLaf JAR if not present
if [ ! -f "lib/flatlaf-3.2.5.jar" ]; then
    echo "Downloading FlatLaf library..."
    mkdir -p lib
    curl -L -o lib/flatlaf-3.2.5.jar https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.5/flatlaf-3.2.5.jar
fi

echo "Compiling Java source files..."
javac -cp "lib/*" -d "target/classes" -sourcepath "src/main/java" \
    src/main/java/com/algorithmvisualizer/*.java \
    src/main/java/com/algorithmvisualizer/algorithms/*.java \
    src/main/java/com/algorithmvisualizer/algorithms/sorting/*.java \
    src/main/java/com/algorithmvisualizer/algorithms/pathfinding/*.java \
    src/main/java/com/algorithmvisualizer/models/*.java \
    src/main/java/com/algorithmvisualizer/models/pathfinding/*.java \
    src/main/java/com/algorithmvisualizer/ui/*.java \
    src/main/java/com/algorithmvisualizer/ui/panels/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Starting Algorithm Visualizer..."
    java -cp "target/classes:lib/*" com.algorithmvisualizer.AlgorithmVisualizerApp
else
    echo "Compilation failed!"
    read -p "Press any key to continue..."
fi