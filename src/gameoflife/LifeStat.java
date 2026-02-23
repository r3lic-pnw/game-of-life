package gameoflife;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Data model representing a single statistical entry for the Game of Life.
 * Utilizes JavaFX Properties to enable automatic UI updates when values change
 * within a {@code TableView}.
 *
 * <pre>
 * File            LifeStat.java
 * Project         Game of Life
 * Platform        PC, Windows 11; JDK 25, NetBeans IDE 26
 * Course          CS 142
 * Date            05/22/2026
 * </pre>
 *
 * @author          Jarrell Quincy | r3lic
 * @version         1.0.0
 * @since           1.0.0
 * @see             javafx.beans.property.Property
 */
@SuppressWarnings("unused")
public class LifeStat {
    private final StringProperty statName;
    private final IntegerProperty value;

    /**
     * Constructs a LifeStat entry with a label and an initial value.
     *
     * <pre>
     * Postconditions:  stat and value properties are initialized.
     * </pre>
     *
     * @param statName  the name/label of the statistic (e.g., "Generations")
     * @param value the initial integer value of the statistic
     */
    public LifeStat(String statName, int value) {
        this.statName = new SimpleStringProperty(statName);
        this.value = new SimpleIntegerProperty(value);
    }

    /* ---------- VALUE METHODS ---------- */
    /**
     * Returns the current primitive integer value of the statistic.
     * @return the current value
     */
    public int getValue() {
        return value.get();
    }

    /**
     * Updates the value of the statistic.
     * This will automatically trigger an update in any bound UI components.
     * @param value the new integer value
     */
    public void setValue(int value) {
        this.value.set(value);
    }

    /**
     * Returns the {@code IntegerProperty} object for the value.
     * Used by JavaFX for property binding and table column cell factories.
     * @return the value property object
     */
    public IntegerProperty valueProperty() {
        return value;
    }

    /* ---------- STAT METHODS ---------- */
    /**
     * Returns the current string label of the statistic.
     * @return the name of the statistic
     */
    public String getStatName() {
        return statName.get();
    }

    /**
     * Updates the name/label of the statistic.
     * @param statName the new string label (must not be null)
     */
    public void setStatName(String statName) {
        this.statName.set(statName);
    }

    /**
     * Returns the {@code StringProperty} object for the stat label.
     * Used by JavaFX for property binding and table column cell factories.
     * @return the stat property object
     */
    public StringProperty statNameProperty() {
        return statName;
    }
}
