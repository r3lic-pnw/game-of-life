package gameoflife;

/**
 * Enumeration defining the specific types of entities that can exist
 * within the simulation grid. Used primarily by the UI controller
 * to determine rendering colors and user placement toggles.
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           2.0.0
 */
public enum CellType {
    /** Represents an OrganicCell of type Animal. */
    ANIMAL,
    /** Represents an OrganicCell of type Plant. */
    PLANT,
    /** Represents an InorganicCell of type Wall. */
    WALL
}
