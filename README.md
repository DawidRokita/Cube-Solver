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
- 📊 **Graphical visualization of solutions**
- 📡 **Bluetooth support** (optional – robot integration)

---

## 🧠 Implemented Solving Algorithms

### 🔹 Two-Phase Algorithm (Kociemba)
Used as a fast and reliable general solver.

- Input: cube state as a 54-character facelet string  
- Output: optimal or near-optimal solution (≤ 21 moves)

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

---

## 📚 External Sources & Credits

This project **uses and adapts algorithms from the following open-source repositories**:

### 🔗 Two-Phase Algorithm
Based on Herbert Kociemba’s algorithm and its Java implementations:
- https://github.com/cs0x7f/min2phase

### 🔗 LBL Solver
Inspired by educational and open-source LBL implementations:
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


