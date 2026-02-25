# ~~Conway's~~ r3lic's Game of Life

A remix of John Conway's Game of Life.  
Built with Java and JavaFX, this project was developed as the Final Programming Project for CS 142 (Winter 2026).

Unlike the traditional Game of Life, this simulator introduces distinct cell 
types, each with its own unique survival and reproduction rules.

## Features

* **Multiple Cell Types:** Paint the board with Animals, Plants, or static Walls.
* **Interactive Grid:** 
  * **Left-Click:** Toggle the living/dead state of a cell.
  * **Right-Click:** Place a specific cell type onto the board (selected via the Control Panel).
* **Real-time Customization:** Dynamically adjust board dimensions (rows/columns) and cell sizes.
* **Custom Themes:** Use the color pickers to customize the live/dead colors for each cell type, as well as the grid lines.
* **Simulation Controls:** Start/Stop the animation, Step forward by a single generation, or completely Reset/Clear the board.
* **Speed Control:** A slider allows you to adjust the time between generations on the fly.
* **Live Statistics:** Tracks the current Generation, Living Cells, and cumulative Deceased Cells using a JavaFX TableView.

## Cell Types & Rules

The grid utilizes a 3-level inheritance hierarchy to calculate the fate of each cell.

1. **Animal Cell (Standard Rules)**
    * **Underpopulation:** Dies with fewer than 2 living neighbors.
    * **Survival:** Survives with 2 or 3 living neighbors.
    * **Overpopulation:** Dies with more than 3 living neighbors.
    * **Reproduction:** A dead cell comes alive if it has exactly 3 living neighbors.
2. **Plant Cell (Resilient Rules)**
    * **Survival:** Highly resilient. Survives with 1 to 4 living neighbors inclusive.
    * **Reproduction:** Sprouts with 2 or 3 living neighbors.
    * **Death:** Dies only if completely isolated (0 neighbors) or overcrowded (5+ neighbors).
3. **Wall Cell (Static Obstacle)**
    * **Inorganic:** Impassable and inherently lifeless. Never comes alive and never reproduces.

## Architecture

This application strictly follows the **Model-View-Controller (MVC) architecture**:
* **Model (`LifeLogic`):** Encapsulates the simulation state in a 2D array of `AbstractCell` objects. It delegates rule evaluation to the polymorphic entities it contains.
* **View (`LifeCell` & FXML):** Represents the visual projection of the grid. `LifeCell` extends the JavaFX `Rectangle` class.
* **Controller (`LifeController`):** Bridges the UI and the logic state. It coordinates user interaction, manages the JavaFX `AnimationTimer`, and orchestrates rendering.

## Tech Stack & Requirements

* **Language:** Java
* **UI Framework:** JavaFX 
* **Environment:** Developed on Windows 11 using JDK 25 and IntelliJ IDEA 2025.2.6.1.

## Getting Started

1. Clone this repository: `git clone https://github.com/r3lic-pnw/game-of-life.git`
2. Open the project in your preferred Java IDE (NetBeans, IntelliJ, Eclipse). 
3. Ensure your IDE is configured with JDK 25 and download the [JavaFX 25 SDK](https://gluonhq.com/products/javafx/) separately from gluonhq.com: 
   - Add it as a library 
   - Create a module-info.java
    ```java
    module gameoflife {
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.graphics;
        requires javafx.base;

        opens gameoflife to javafx.fxml, javafx.graphics;

        exports gameoflife;
    }
    ``` 
   - and configure VM options with `--enable-native-access=javafx.graphics`
4. Run the main application class to launch the GUI.

## Author
**Jarrell Quincy | r3lic-pnw**