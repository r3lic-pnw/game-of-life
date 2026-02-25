package gameoflife;

/**
 * Represents an impassable wall entity in the Game of Life.
 * Walls act as static environmental features that never come alive.
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           2.0.0
 */
public class WallCell extends InorganicCell {

    /**
     * Constructs a default WallCell. It is initialized as dead.
     */
    public WallCell() {
        super();
    }

    /**
     * Calculates the next state for a wall.
     * Walls cannot reproduce or come alive, so the next state is always false.
     *
     * @param livingNeighbors the number of adjacent living cells
     */
    @Override
    public void calculateNextState(int livingNeighbors) {
        nextState = false;
    }

    @Override
    public CellType getCellType() {
        return CellType.WALL;
    }
    @Override
    public String toString() {
        return "W";
    }
}
