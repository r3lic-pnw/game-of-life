package gameoflife;

/**
 * The abstract base class for all logical entities in the Game of Life grid.
 * Defines the core state (living or dead) and the contract for calculating
 * future states based on Conway's Game of Life principles.
 *
 * <pre>
 * File            AbstractCell.java
 * Project         Game of Life
 * Platform        PC, Windows 11; JDK 25
 * Course          CS 142
 * Date            02/24/2026
 * </pre>
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           2.0.0
 */
public abstract class AbstractCell {
    protected boolean isAlive;
    protected boolean nextState;

    /**
     * Constructs a base cell with an initial living state.
     * @param isAlive true if the cell starts alive, false if dead
     */
    public AbstractCell(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Returns the current living state of the cell.
     * @return true if alive, false if dead
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Instantly updates the living state of the cell.
     * @param isAlive the new living state
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * Commits the calculated next state to be the current state.
     * Should be called after all cells have calculated their next states.
     */
    public void applyNextState() {
        this.isAlive = nextState;
    }

    /**
     * Calculates whether this cell will be alive or dead in the next generation.
     * Subclasses must define their own survival and reproduction rules.
     * @param livingNeighbors the number of adjacent living cells (0 to 8)
     */
    public abstract void calculateNextState(int livingNeighbors);

    /**
     * Identifies the specific type of cell for UI rendering purposes.
     * @return the {@code CellType} enum representing this cell
     */
    public abstract CellType getCellType();

    /**
     * Returns a string representation of the cell, typically a single character.
     * Useful for text-based console debugging.
     * @return a string representing the cell type
     */
    public abstract String toString();
}
