package gameoflife;

/**
 * Logical model for the Game of Life grid.
 * Holds the state of the cells in a 2-D array of {@code AbstractCell} objects,
 * enabling polymorphic behavior based on cell type. It provides methods for
 * retrieving and updating the board state, calculating the next board state
 * by delegating logic to individual cells, and tracking simulation statistics.
 *
 * <pre>
 * File            LifeLogic.java
 * Project         Game of Life
 * Platform        PC, Windows 11; JDK 25
 * Course          CS 142
 * Date            02/24/2026
 * </pre>
 *
 * @author          Jarrell Quincy | r3lic
 * @version         2.0.0
 * @since           1.0.0
 */
public class LifeLogic {
    private AbstractCell[][] boardState;
    private int logicRows;
    private int logicCols;

    private int generationCount = 0;
    private int livingCount = 0;
    private int deceasedCount = 0;

    /**
     * Constructs a new logical game board with the specified dimensions.
     * All cells default to dead {@code AnimalCell} instances.
     *
     * @param rows the number of rows in the grid
     * @param cols the number of columns in the grid
     */
    public LifeLogic(int rows, int cols) {
        logicRows = rows;
        logicCols = cols;
        boardState = new AbstractCell[logicRows][logicCols];

        // Initialize the board with dead AnimalCells by default
        for (int r = 0; r < logicRows; r++) {
            for (int c = 0; c < logicCols; c++) {
                boardState[r][c] = new AnimalCell(false);
            }
        }
    }

    /**
     * Returns a shallow copy of the 2D {@code AbstractCell} array structure.
     * Note: The cell objects themselves are not cloned.
     *
     * @return a 2D array representing the current board layout
     */
    public AbstractCell[][] getBoardState() {
        AbstractCell[][] boardStateCopy = new AbstractCell[logicRows][logicCols];
        for (int r = 0; r < logicRows; r++) {
            System.arraycopy(boardState[r], 0, boardStateCopy[r], 0, logicCols);
        }
        return boardStateCopy;
    }

    /**
     * Updates the internal board state with a new 2D array of cells
     * and synchronizes the board dimensions.
     *
     * @param boardState the new 2D array of {@code AbstractCell} objects
     */
    public void setBoardState(AbstractCell[][] boardState) {
        logicRows = boardState.length;
        logicCols = boardState[0].length;
        this.boardState = boardState;
    }

    /**
     * Processes the entire board by first telling each cell to calculate its
     * next state based on its neighbors, then applying those states and
     * updating the generation statistics.
     */
    public void calculateNextGeneration() {
        int newlyDeceased = 0;
        int newlyLiving = 0;

        // Calculate next state for all cells
        for (int r = 0; r < logicRows; r++) {
            for (int c = 0; c < logicCols; c++) {
                int livingNeighbors = countLivingNeighbors(r, c);
                boardState[r][c].calculateNextState(livingNeighbors);
            }
        }

        // Apply the calculated states and track statistics
        livingCount = 0;
        for(int r = 0; r < logicRows; r++){
            for(int c = 0; c < logicCols; c++){
                boolean wasAlive = boardState[r][c].isAlive();
                boardState[r][c].applyNextState();
                boolean isNowAlive = boardState[r][c].isAlive();

                if (isNowAlive) {
                    livingCount++;
                }
                if (wasAlive && !isNowAlive) {
                    newlyDeceased++;
                }
            }
        }

        deceasedCount += newlyDeceased;
        generationCount++;
    }

    /**
     * Calculates the number of living cells in the 8 adjacent positions
     * surrounding a specific coordinate.
     *
     * @param row the row index of the target cell
     * @param col the column index of the target cell
     * @return the number of adjacent living cells
     */
    private int countLivingNeighbors(int row, int col) {
        int count = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue;

                int neighborRow = row + r;
                int neighborCol = col + c;
                if (neighborRow >= 0 && neighborRow < logicRows && neighborCol >= 0 && neighborCol < logicCols) {
                    if (boardState[neighborRow][neighborCol].isAlive()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Inverts the living state of a specific cell; if it is alive,
     * it becomes dead, and vice versa.
     *
     * @param row the row index of the target cell
     * @param col the column index of the target cell
     */
    public void toggleLiving(int row, int col) {
        boolean currentState = boardState[row][col].isAlive();
        boardState[row][col].setAlive(!currentState);
    }

    /**
     * Checks the current living state of the specific {@code AbstractCell}
     * at the given coordinates.
     *
     * @param row the row index of the target cell
     * @param col the column index of the target cell
     * @return true if the cell is alive, false otherwise
     */
    public boolean isAlive(int row, int col) {
        return boardState[row][col].isAlive();
    }

    /**
     * Retrieves the specific {@code AbstractCell} object located at the given coordinates.
     *
     * @param row the row index of the target cell
     * @param col the column index of the target cell
     * @return the {@code AbstractCell} at the specified location
     */
    public AbstractCell getCell(int row, int col) {
        return boardState[row][col];
    }

    /**
     * Replaces the cell at the specified coordinates with a new {@code AbstractCell} instance
     * (e.g., swapping an {@code AnimalCell} for a {@code WallCell}).
     *
     * @param row  the row index of the target cell
     * @param col  the column index of the target cell
     * @param cell the new {@code AbstractCell} to place at the coordinates
     */
    public void setCell(int row, int col, AbstractCell cell) {
        boardState[row][col] = cell;
    }

    /**
     * Retrieves the total number of generations (ticks) that have occurred
     * since the simulation started.
     *
     * @return the generation count
     */
    public int getGenerationCount() {
        return generationCount;
    }

    /**
     * Returns the current population count of all alive cells on the board
     * for the current generation.
     *
     * @return the number of living cells
     */
    public int getLivingCount() {
        return livingCount;
    }

    /**
     * Returns the cumulative number of cells that have transitioned from alive
     * to dead over the course of the simulation.
     *
     * @return the total number of deceased cells
     */
    public int getDeceasedCount() {
        return deceasedCount;
    }
}
