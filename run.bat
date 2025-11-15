@echo off
echo Algorithm Visualizer - Build and Run Script
echo ==========================================

rem Create output directory
if not exist "target\classes" mkdir "target\classes"

rem Download FlatLaf JAR if not present
if not exist "lib\flatlaf-3.2.5.jar" (
    echo Downloading FlatLaf library...
    if not exist "lib" mkdir "lib"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.5/flatlaf-3.2.5.jar' -OutFile 'lib\flatlaf-3.2.5.jar'"
)

echo Compiling Java source files...
javac -cp "lib\*" -d "target\classes" -sourcepath "src\main\java" src\main\java\com\algorithmvisualizer\*.java src\main\java\com\algorithmvisualizer\algorithms\*.java src\main\java\com\algorithmvisualizer\algorithms\sorting\*.java src\main\java\com\algorithmvisualizer\algorithms\pathfinding\*.java src\main\java\com\algorithmvisualizer\models\*.java src\main\java\com\algorithmvisualizer\models\pathfinding\*.java src\main\java\com\algorithmvisualizer\ui\*.java src\main\java\com\algorithmvisualizer\ui\panels\*.java

if %errorlevel% == 0 (
    echo Compilation successful!
    echo Starting Algorithm Visualizer...
    java -cp "target\classes;lib\*" com.algorithmvisualizer.AlgorithmVisualizerApp
) else (
    echo Compilation failed!
    pause
)