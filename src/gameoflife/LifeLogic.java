package gameoflife;

/**
 * Handles the core simulation logic for Conway's Game of Life.
 * This class maintains the state of the grid and applies the rules of
 * evolution to calculate successive generations.
 *
 * <pre>
 * File            LifeLogic.java
 * Project         Game of Life
 * Platform        PC, Windows 11; JDK 25
 * Course          CS 142
 * Date            02/22/2026
 * </pre>
 *
 * @author          Jarrell Quincy | r3lic
 * @version         1.0.0
 * @since           1.0.0
 */
public class LifeLogic {
    private boolean[][] boardState;
    private int logicRows;
    private int logicCols;

    private int generationCount = 0;
    private int livingCount = 0;
    private int deceasedCount = 0;

    /**
     * Constructs a new LifeLogic instance with specified dimensions.
     * Initializes an empty (dead) board.
     *
     * <pre>
     * Postconditions:  A boolean grid of size [rows][cols] is initialized.
     * All cells are set to {@code false} (dead).
     * </pre>
     *
     * @param rows the number of rows in the grid (must be positive)
     * @param cols the number of columns in the grid (must be positive)
     */
    public LifeLogic(int rows, int cols) {
        logicRows = rows;
        logicCols = cols;
        boardState = new boolean[logicRows][logicCols];
    }

    /**
     * Returns a deep copy of the current board state.
     * Creating a copy ensures that external classes cannot modify the internal
     * state directly.
     *
     * <pre>
     * Implementation:  Performs a row-by-row {@code System.arraycopy}.
     * </pre>
     *
     * @return a 2D boolean array representing the current life states
     */
    public boolean[][] getBoardState() {
        boolean[][] boardStateCopy = new boolean[logicRows][logicCols];
        for (int row = 0; row < this.logicRows; row++) {
            System.arraycopy(this.boardState[row], 0, boardStateCopy[row], 0, this.logicCols);
        }
        return boardStateCopy;
    }

    /**
     * Updates the internal board state and synchronizes dimensions.
     * * @param boardState the new 2D boolean array to use as the state (must not be null)
     */
    public void setBoardState(boolean[][] boardState) {
        int newRows = boardState.length;
        int newCols = boardState[0].length;
        logicRows = newRows;
        logicCols = newCols;
        this.boardState = boardState;
    }

    /**
     * Applies Conway's Rules of Life to transition the board to the next generation.
     * * <pre>
     * Algorithm:       1. Any live cell with 2 or 3 neighbors survives.
     * 2. Any dead cell with exactly 3 neighbors becomes alive.
     * 3. All other cells die or stay dead.
     * Time Complexity: O(rows * cols)
     * Postconditions:  boardState is updated to the new generation.
     * generationCount is incremented.
     * livingCount and deceasedCount are updated.
     * </pre>
     */
    public void calculateNextGeneration() {
        boolean[][] nextBoardState = new boolean[boardState.length][boardState[0].length];
        livingCount = 0;

        for (int r = 0; r < logicRows; r++) {
            for (int c = 0; c < logicCols; c++) {
                boolean cellAlive = boardState[r][c];
                int livingNeighbors = countLivingNeighbors(r, c);

                if (cellAlive) {
                    boolean survived = livingNeighbors == 2 || livingNeighbors == 3;
                    nextBoardState[r][c] = survived;
                    livingCount += survived ? 1 : 0;
                    deceasedCount += survived ? 0 : 1;
                } else {
                    if (livingNeighbors == 3) {
                        nextBoardState[r][c] = true;
                        livingCount++;
                    }
                }
            }
        }

        boardState = nextBoardState;
        generationCount++;
    }

    /**
     * Counts the number of living cells in the 8-neighbor Moore neighborhood.
     * * <pre>
     * Implementation:  Iterates from -1 to 1 in both axes, skipping the center.
     * Special Cases:   Boundaries are checked to prevent ArrayIndexOutOfBoundsException.
     * </pre>
     *
     * @param row the row index of the target cell
     * @param col the column index of the target cell
     * @return the count of living neighbors (0-8)
     */
    private int countLivingNeighbors(int row, int col) {
        int count = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue;

                int neighborRow = row + r;
                int neighborCol = col + c;
                if (neighborRow >= 0 && neighborRow < logicRows && neighborCol >= 0 && neighborCol < logicCols) {
                    if (boardState[neighborRow][neighborCol]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Toggles the state of a specific cell (Alive -> Dead, Dead -> Alive).
     * @param row the row index to toggle
     * @param col the column index to toggle
     */
    public void toggleLiving(int row, int col) {
        boardState[row][col] = !boardState[row][col];
    }

    /**
     * Checks if a specific cell is currently alive.
     * @param row the row index to check
     * @param col the column index to check
     * @return {@code true} if the cell is alive, {@code false} otherwise
     */
    public boolean isAlive(int row, int col) {
        return boardState[row][col];
    }

    /**
     * Returns the total number of generations processed since initialization.
     * @return the generation count
     */
    public int getGenerationCount() {
        return generationCount;
    }

    /**
     * Returns the number of living cells currently on the board.
     * @return the count of alive cells
     */
    public int getLivingCount() {
        return livingCount;
    }

    /**
     * Returns the number of cells that died during the last generation transition.
     * @return the count of deceased cells
     */
    public int getDeceasedCount() {
        return deceasedCount;
    }
}
