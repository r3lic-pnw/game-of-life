package gameoflife;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Controller for the Game of Life JavaFX application.
 * This class manages the interaction between the user interface and the game
 * logic, handling board rendering, animation timing, and statistical tracking.
 *
 * <pre>
 * File            LifeController.java
 * Project         Game of Life
 * Platform        PC, Windows 11; JDK 25
 * Course          CS 142
 * Date            02/22/2026
 * </pre>
 *
 * @author          Jarrell Quincy | r3lic
 * @version         1.0.0
 * @since           1.0.0
 * @see             LifeLogic
 * @see             LifeCell
 * @see             LifeStat
 */
public class LifeController {
    /* ---------- Game Board ----------*/
    @FXML
    private GridPane gameBoard;
    private LifeLogic logicState;
    private LifeCell[][] uiState;

    /* ---------- CONTROLS ----------- */
    @FXML
    private Spinner<Integer> colSpinner;
    @FXML
    private Spinner<Integer> rowSpinner;
    @FXML
    private Spinner<Integer> cellWidthSpinner;
    @FXML
    private Spinner<Integer> cellHeightSpinner;
    @FXML
    private Slider speedSlider;
    @FXML
    private ColorPicker deadColorPicker;
    @FXML
    private ColorPicker liveColorPicker;
    @FXML
    private ColorPicker strokeColorPicker;
    @FXML
    private Button startStopButton;

    /* ---------- UI VARIABLES ---------- */
    private Color deadColor;
    private Color liveColor;
    private Color strokeColor;
    private int boardRows;
    private int boardCols;
    private int cellWidth;
    private int cellHeight;

    /* ---------- DEFAULT UI VALUES ---------- */
    private final Color DEFAULT_DEAD_COLOR = Color.RED;
    private final Color DEFAULT_LIVE_COLOR = Color.GREEN;
    private final Color DEFAULT_STROKE_COLOR = Color.WHITE;
    private final int DEFAULT_BOARD_ROWS = 17;
    private final int DEFAULT_BOARD_COLS = 17;
    private final int DEFAULT_CELL_WIDTH = 30;
    private final int DEFAULT_CELL_HEIGHT = 30;

    /* ---------- STATS TABLE ---------- */
    @FXML
    private TableView<LifeStat> statsTable;
    @FXML
    private TableColumn<LifeStat, String> statCol;
    @FXML
    private TableColumn<LifeStat, Integer> valueCol;
    private final ObservableList<LifeStat> statsData = FXCollections.observableArrayList();

    /* ---------- STATS OBJECTS ---------- */
    private LifeStat generationStat;
    private LifeStat livingStat;
    private LifeStat deceasedStat;
//    private LifeStat processingTimeStat;

    /* ---------- ANIMATION CONTROL ---------- */
    private AnimationTimer gameLoop;
    private boolean isRunning = false;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets up UI control factories, listeners, statistical tables, and
     * the primary game loop.
     * * <pre>
     * Postconditions:  UI controls are populated with default values.
     * Listeners are attached to all spinners and pickers.
     * The initial LifeLogic state and GridPane are generated.
     * </pre>
     */
    @FXML
    public void initialize() {
        setupUIControls();
        setupListeners();
        setupStatsTables();
        getCurrentSettings();

        // Set initial Logic and UI states
        logicState = new LifeLogic(boardRows, boardCols);
        rebuildUI();

        setupAnimationTimer();
    }

    /**
     * Configures the value factories for all Spinner controls.
     * Sets range constraints for board dimensions and cell sizes.
     */
    private void setupUIControls() {
        colSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, DEFAULT_BOARD_COLS));
        rowSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, DEFAULT_BOARD_ROWS));
        cellWidthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, DEFAULT_CELL_WIDTH));
        cellHeightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, DEFAULT_CELL_HEIGHT));
        deadColorPicker.setValue(DEFAULT_DEAD_COLOR);
        liveColorPicker.setValue(DEFAULT_LIVE_COLOR);
        strokeColorPicker.setValue(DEFAULT_STROKE_COLOR);
    }

    /**
     * Attaches change listeners to UI components to handle real-time updates.
     * Manages board resizing, color theme changes, and cell scaling.
     */
    private void setupListeners() {
        rowSpinner.valueProperty().addListener((_, _, newValue) -> {
            boardRows = newValue;
            handleResize();
        });
        colSpinner.valueProperty().addListener((_, _, newValue) -> {
            boardCols = newValue;
            handleResize();
        });

        deadColorPicker.valueProperty().addListener((_, _, newValue) -> {
            deadColor = newValue;
            syncUILogicState();
        });
        liveColorPicker.valueProperty().addListener((_, _, newValue) -> {
            liveColor = newValue;
            syncUILogicState();
        });
        strokeColorPicker.valueProperty().addListener((_, _, newValue) -> {
            strokeColor = newValue;
            syncUILogicState();
        });

        cellWidthSpinner.valueProperty().addListener((_, _, newValue) -> {
            cellWidth = newValue;
            updateCellDimensions();
        });
        cellHeightSpinner.valueProperty().addListener((_, _, newValue) -> {
            cellHeight = newValue;
            updateCellDimensions();
        });
    }

    /**
     * Updates the dimensions of all {@code LifeCell} objects in the UI grid.
     * * <pre>
     * Preconditions:   uiState array must be initialized.
     * Postconditions:  All cells in the grid reflect the new width and height.
     * </pre>
     */
    private void updateCellDimensions() {
        if (uiState == null) return;
        for (LifeCell[] cellRow : uiState) {
            for (LifeCell cell : cellRow) {
                cell.setWidth(cellWidth);
                cell.setHeight(cellHeight);
            }
        }
    }

    /**
     * Configures the {@code TableView} for displaying game statistics.
     * Initializes the columns and populates the observable list with stat trackers.
     */
    private void setupStatsTables() {
        statCol.setCellValueFactory(new PropertyValueFactory<>("statName"));
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        generationStat = new LifeStat("Generations", 0);
        livingStat = new LifeStat("Living Cells", 0);
        deceasedStat = new LifeStat("Deceased Cells", 0);
//        processingTimeStat = new LifeStat("Processing Time (ms)", 0);

        statsData.addAll(generationStat, livingStat, deceasedStat);
//        statsData.addAll(generationStat, livingStat, deceasedStat, processingTimeStat);
        statsTable.setItems(statsData);
    }

    /**
     * Refreshes the values in the statistics table from the current logic state.
     */
    private void updateStatsTable() {
        generationStat.setValue(logicState.getGenerationCount());
        livingStat.setValue(logicState.getLivingCount());
        deceasedStat.setValue(logicState.getDeceasedCount());
    }

    /**
     * Syncs internal variables with the current values of UI controls.
     */
    private void getCurrentSettings() {
        boardRows = rowSpinner.getValue();
        boardCols = colSpinner.getValue();
        cellWidth = cellWidthSpinner.getValue();
        cellHeight = cellHeightSpinner.getValue();
        deadColor = deadColorPicker.getValue();
        liveColor = liveColorPicker.getValue();
        strokeColor = strokeColorPicker.getValue();
    }

    /**
     * Initializes the {@code AnimationTimer} responsible for the game loop.
     * * <pre>
     * Implementation:  Calculates the tick interval based on the {@code speedSlider}.
     * The tick is executed only if the elapsed time exceeds
     * the interval.
     * </pre>
     */
    private void setupAnimationTimer() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                double speedInMillis = speedSlider.getValue();
                long interval = (long) (speedInMillis * 1_000_000);
                if (now - lastUpdate >= interval) {
                    tick();
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * Completely recreates the GridPane UI based on current board dimensions.
     * * <pre>
     * Implementation:  Clears the GridPane, instantiates new LifeCell objects,
     * and attaches mouse click events to each cell.
     * Postconditions:  uiState array is refreshed and GridPane is repopulated.
     * </pre>
     */
    private void rebuildUI() {
        gameBoard.getChildren().clear();
        uiState = new LifeCell[boardRows][boardCols];
        getCurrentSettings();

        for (int r = 0; r < boardRows; r++) {
            for (int c = 0; c < boardCols; c++) {
                LifeCell cell = new LifeCell(cellWidth, cellHeight);
                cell.setStroke(strokeColor);

                final int row = r;
                final int col = c;
                cell.setOnMouseClicked(_ -> onCellClicked(cell, row, col));

                uiState[r][c] = cell;
                gameBoard.add(cell, c, r);
            }
        }

        syncUILogicState();
    }

    /**
     * Synchronizes the visual state of {@code LifeCell} objects with the logic state.
     * Updates colors based on the living/dead status and refreshes the stats table.
     */
    private void syncUILogicState() {
        for (int r = 0; r < uiState.length; r++) {
            for (int c = 0; c < uiState[0].length; c++) {
                boolean living = logicState.isAlive(r, c);
                uiState[r][c].setFill(living ? liveColor : deadColor);
                uiState[r][c].setStroke(strokeColor);
            }
        }

        updateStatsTable();
    }

    /**
     * Advances the game by one generation and measures processing performance.
     * * <pre>
     * Implementation:  Wraps the logic calculation and UI sync in nanoTime measurements.
     * Time Complexity: O(rows * cols) for the sync step.
     * </pre>
     */
    @FXML
    private void tick() {
        /* ---------- START PROCESS TIME MEASUREMENT ---------- */
        // Measure time in nanoseconds
//        long startTime = System.nanoTime();

        logicState.calculateNextGeneration();
        syncUILogicState();

//        long endTime = System.nanoTime();

        // Convert nanoseconds to milliseconds (1,000,000 ns = 1 ms)
//        int elapsedTime = (int) ((endTime - startTime) / 1_000_000);
        /* ---------- END PROCESSING TIME MEASUREMENT */

//        processingTimeStat.setValue(elapsedTime);
    }

    /**
     * Handles board resizing while attempting to preserve the existing cell states.
     * * <pre>
     * Implementation:  Creates a new boolean array and uses {@code System.arraycopy}
     * to migrate existing cell data to the new dimensions.
     * Postconditions:  The logic board is updated and the UI is rebuilt.
     * </pre>
     */
    private void handleResize() {
        boolean[][] oldBoardState = logicState.getBoardState();
        boolean[][] nextBoardState = new boolean[boardRows][boardCols];
        int resizedRowCount = Math.min(oldBoardState.length, boardRows);
        int resizedColCount = Math.min(oldBoardState[0].length, boardCols);

        for (int r = 0; r < resizedRowCount; r++) {
            System.arraycopy(oldBoardState[r], 0, nextBoardState[r], 0, resizedColCount);
        }

        logicState.setBoardState(nextBoardState);
        logicState.calculateNextGeneration();

        rebuildUI();
    }

    /**
     * Manages logic and UI updates when an individual cell is clicked.
     * * @param cell the {@code LifeCell} UI component that was clicked (must not be null)
     * @param row  the row index of the cell
     * @param col  the column index of the cell
     */
    private void onCellClicked(LifeCell cell, int row, int col) {
        logicState.toggleLiving(row, col);
        cell.setFill(logicState.isAlive(row, col) ? liveColor : deadColor);
    }

    /**
     * Starts the animation timer and updates the button text.
     */
    private void startGame() {
        isRunning = true;
        gameLoop.start();
        startStopButton.setText("Stop");
    }

    /**
     * Stops the animation timer and updates the button text.
     */
    private void stopGame() {
        isRunning = false;
        boolean isPaused = startStopButton.getText().equals("Stop");
        gameLoop.stop();
        startStopButton.setText(isPaused ? "Resume" : "Start");
    }

    /**
     * Toggle handler for the Start/Stop button.
     */
    @FXML
    private void onStartStopClicked() {
        if (isRunning) {
            stopGame();
        } else {
            startGame();
        }
    }

    /**
     * Resets the entire game to its default state.
     * Stops any running animation and restores all UI control default values.
     */
    @FXML
    private void resetGame() {
        isRunning = false;
        gameLoop.stop();
        startStopButton.setText("Start");

        rowSpinner.getValueFactory().setValue(DEFAULT_BOARD_ROWS);
        colSpinner.getValueFactory().setValue(DEFAULT_BOARD_COLS);
        cellWidthSpinner.getValueFactory().setValue(DEFAULT_CELL_WIDTH);
        cellHeightSpinner.getValueFactory().setValue(DEFAULT_CELL_HEIGHT);
        deadColorPicker.setValue(DEFAULT_DEAD_COLOR);
        liveColorPicker.setValue(DEFAULT_LIVE_COLOR);
        strokeColorPicker.setValue(DEFAULT_STROKE_COLOR);

        int DEFAULT_GAME_SPEED = 200;
        speedSlider.setValue(DEFAULT_GAME_SPEED);

        deadColor = DEFAULT_DEAD_COLOR;
        liveColor = DEFAULT_LIVE_COLOR;
        strokeColor = DEFAULT_STROKE_COLOR;
        cellWidth = DEFAULT_CELL_WIDTH;
        cellHeight = DEFAULT_CELL_HEIGHT;

        logicState = new LifeLogic(DEFAULT_BOARD_ROWS, DEFAULT_BOARD_COLS);
        rebuildUI();
    }

    /**
     * Clears all living cells from the board.
     * * <pre>
     * Postconditions:  All cells in logicState are set to dead.
     * The UI is updated to reflect the empty board.
     * </pre>
     */
    @FXML
    private void clearBoard() {
        logicState = new LifeLogic(boardRows, boardCols);
        syncUILogicState();
    }
}
