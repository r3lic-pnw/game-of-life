package gameoflife;

/**
 * Represents a category of cells that possess biological traits,
 * meaning they have the capacity to be alive, reproduce, and die.
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           2.0.0
 */
public abstract class OrganicCell extends AbstractCell {

    /**
     * Constructs an OrganicCell with an initial living state.
     * @param isAlive true if the cell starts alive, false if dead
     */
    public OrganicCell(boolean isAlive) {
        super(isAlive);
    }
}
