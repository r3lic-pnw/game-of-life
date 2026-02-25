package gameoflife;

/**
 * Represents a standard animal entity in the Game of Life.
 * Follows the traditional Conway rules for survival and reproduction.
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           2.0.0
 */
public class AnimalCell extends OrganicCell{

    /**
     * Constructs an AnimalCell with an initial living state.
     * @param isAlive true if the cell starts alive, false if dead
     */
    public AnimalCell(boolean isAlive) {
        super(isAlive);
    }

    /**
     * Calculates the next state using standard Conway rules:
     * - Survives if it has 2 or 3 living neighbors.
     * - Reproduces (born) if it has exactly 3 living neighbors.
     * - Dies otherwise (underpopulation or overpopulation).
     *
     * @param livingNeighbors the number of adjacent living cells
     */
    @Override
    public void calculateNextState(int livingNeighbors) {
        if(isAlive) {
            nextState = (livingNeighbors == 2 || livingNeighbors == 3);
        } else {
            nextState = (livingNeighbors ==3);
        }
    }

    @Override
    public CellType getCellType() {
        return CellType.ANIMAL;
    }

    @Override
    public String toString() {
        return "A";
    }
}
