package gameoflife;

import javafx.scene.shape.Rectangle;

/**
 * Represents a single visual cell within the Game of Life grid.
 * This class extends the JavaFX {@code Rectangle} to provide a graphical
 * representation of a cell that can be styled based on its living state.
 *
 * <pre>
 * File            LifeCell.java
 * Project         Game of Life
 * Platform        PC, Windows 11; JDK 25, NetBeans IDE 26
 * Course          CS 142
 * Date            05/22/2026
 * </pre>
 *
 * @author          Jarrell Quincy | r3lic
 * @version         1.0.0
 * @since           1.0.0
 * @see             javafx.scene.shape.Rectangle
 */
public class LifeCell extends Rectangle {
    /**
     * Constructs a LifeCell with the specified dimensions.
     * Initializes the rectangle which will be placed within the
     * {@code GridPane} of the game board.
     *
     * <pre>
     * Postconditions:  A Rectangle is initialized with the given width/height.
     * The cell is ready to be added to a Parent container.
     * </pre>
     *
     * @param width  the initial width of the cell (must be > 0)
     * @param height the initial height of the cell (must be > 0)
     */
    public LifeCell(int width, int height) {
        super(width, height);
    }
}
