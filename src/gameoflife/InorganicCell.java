package gameoflife;

/**
 * Represents a category of static, non-biological cells in the simulation.
 * Inorganic cells are inherently lifeless and generally serve as environmental obstacles.
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           2.0.0
 */
public abstract class InorganicCell extends AbstractCell {
    /**
     * Constructs an InorganicCell. By definition, these start in a dead state.
     */
    public InorganicCell() {
        super(false);
    }
}
