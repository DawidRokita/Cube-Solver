# 🧊 Rubik’s Cube Solver – Android App

Android application for scanning, solving, and visualizing solutions for a 3×3 Rubik’s Cube.  
The app allows users to input the cube state manually or via camera, choose different solving algorithms, and view both textual and graphical solutions.

---

## ✨ Features

- 📷 **Camera-based cube scanning** (CameraX)
- 🎨 **Color detection using HSV**
- 🧩 **Manual cube state input**
- 🧠 **Multiple solving algorithms**
  - Two-Phase (Kociemba)
  - LBL (Layer By Layer)
  - CFOP (Cross → F2L → OLL → PLL)
  - Meta-Solver (parallel algorithm selection system)
- 📊 **Graphical visualization of solutions**
- 📡 **Bluetooth support** (optional – robot integration)

---

## 🧠 Implemented Solving Algorithms

### 🔹 Two-Phase Algorithm (Kociemba)
A Rubik’s Cube solving method that finds short solutions (usually 20–21 moves) by first reducing the cube to a restricted subgroup and then solving it completely in a second phase.

### 🔹 LBL (Layer By Layer)
Classic beginner-friendly method:
- Cross
- First layer corners
- Second layer edges
- Last layer orientation and permutation

### 🔹 CFOP (Fridrich Method)
Advanced speedcubing method:
- Cross
- F2L (First Two Layers)
- OLL (Orient Last Layer)
- PLL (Permute Last Layer)

### 🔹 Meta-Solver (Custom Hybrid Solver)
A custom-developed adaptive solving system that runs multiple solving algorithms in parallel and automatically selects the best solution based on predefined evaluation criteria.

The Meta-Solver:
- Executes multiple algorithms simultaneously using multithreading
- Measures:
  - Solution length
  - Solving time
  - Success status
- Computes a score for each algorithm
- Automatically selects the best solution

The evaluation score combines:
- Number of moves
- Computation time

This allows the application to dynamically balance:
- Fast solving
- Short solutions
- Overall solving efficiency

Implemented solver variants inside the Meta-Solver:
- LBL
- CFOP
- Two-Phase (Fast)
- Two-Phase Extended Search

---

## 📚 External Sources & Credits

This project **uses and adapts algorithms from the following open-source repositories**:

### 🔗 Two-Phase Algorithm
Based on Herbert Kociemba’s algorithm and its optimised Java implementation:
- https://github.com/cs0x7f/min2phase

### 🔗 LBL Solver
Adapted from a Java LBL implementation:
- https://github.com/puddles31/rubiks-solver

### 🔗 CFOP Solver
Adapted from a Java CFOP implementation:
- https://github.com/rubintz2/RubiksCubeSolver

All external code has been:
- Integrated into this project structure
- Adapted for Android
- Modified to support custom input/output formats
- Normalized to match a fixed cube orientation

---


