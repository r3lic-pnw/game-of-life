package gameoflife;

/**
 * Represents a hardy plant entity in the Game of Life.
 * Plants have more resilient survival rules compared to standard animals.
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           2.0.0
 */
public class PlantCell extends OrganicCell {

    /**
     * Constructs a PlantCell with an initial living state.
     * @param isAlive true if the cell starts alive, false if dead
     */
    public PlantCell(boolean isAlive) {
        super(isAlive);
    }

    /**
     * Calculates the next state using custom plant rules:
     * - Survives if it has between 1 and 4 living neighbors inclusive.
     * - Sprouts (born) if it has 2 or 3 living neighbors.
     * - Dies if isolated (0 neighbors) or overcrowded (5+ neighbors).
     *
     * @param livingNeighbors the number of adjacent living cells
     */
    @Override
    public void calculateNextState(int livingNeighbors) {
        if (isAlive) {
            nextState = (livingNeighbors >= 1 && livingNeighbors <= 4);
        } else {
            nextState = (livingNeighbors == 2 || livingNeighbors == 3);
        }
    }

    @Override
    public CellType getCellType() {
        return CellType.PLANT;
    }
    @Override
    public String toString() {
        return "P";
    }
}
