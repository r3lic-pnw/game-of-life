package gameoflife;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Controller class for the Game of Life JavaFX application.
 * Acts as the bridge between the FXML UI and the underlying logical model ({@code LifeLogic}).
 * Manages user input, updates visual components based on the logical state, and controls the animation loop.
 *
 * <pre>
 * File            LifeController.java
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
public class LifeController {
    /* ---------- Game Board ----------*/
    @FXML
    private GridPane gameBoard;
    private LifeLogic logicState;
    private LifeCell[][] uiState;

    /* ---------- CONTROLS ----------- */
    // Board Size
    @FXML
    private Spinner<Integer> colSpinner;
    @FXML
    private Spinner<Integer> rowSpinner;

    // Cell Size
    @FXML
    private Spinner<Integer> cellWidthSpinner;
    @FXML
    private Spinner<Integer> cellHeightSpinner;


    // Cell Colors
    @FXML
    private ColorPicker deadAnimalColorPicker;
    @FXML
    private ColorPicker deadPlantColorPicker;
    @FXML
    private ColorPicker liveAnimalColorPicker;
    @FXML
    private ColorPicker livePlantColorPicker;
    @FXML
    private ColorPicker wallColorPicker;
    @FXML
    private ColorPicker strokeColorPicker;

    // Cell Type Radio Buttons
    @FXML
    private ToggleGroup cellTypeGroup;
    @FXML
    private Toggle animalToggle;
    @FXML
    private Toggle plantToggle;
    @FXML
    private Toggle wallToggle;

    // Game Speed
    @FXML
    private Slider speedSlider;

    // Start/Stop Button
    @FXML
    private Button startStopButton;

    /* ---------- UI VARIABLES ---------- */
    private Color liveAnimalColor;
    private Color deadAnimalColor;
    private Color livePlantColor;
    private Color deadPlantColor;
    private Color wallColor;
    private Color strokeColor;
    private int boardRows;
    private int boardCols;
    private int cellWidth;
    private int cellHeight;

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
        setupSpinners();
        setDefaultValues();
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
    private void setupSpinners() {
        rowSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, Defaults.BOARD_ROWS));
        colSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, Defaults.BOARD_COLS));
        cellWidthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, Defaults.CELL_WIDTH));
        cellHeightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, Defaults.CELL_HEIGHT));
    }

    /**
     * Applies default values to all UI input controls (spinners, color pickers, and speed slider)
     * based on the constant values defined in the {@code Defaults} class.
     */
    private void setDefaultValues() {
        rowSpinner.getValueFactory().setValue(Defaults.BOARD_ROWS);
        colSpinner.getValueFactory().setValue(Defaults.BOARD_COLS);
        cellWidthSpinner.getValueFactory().setValue(Defaults.CELL_WIDTH);
        cellHeightSpinner.getValueFactory().setValue(Defaults.CELL_HEIGHT);

        liveAnimalColorPicker.setValue(Defaults.ANIMAL_LIVE);
        deadAnimalColorPicker.setValue(Defaults.ANIMAL_DEAD);
        livePlantColorPicker.setValue(Defaults.PLANT_LIVE);
        deadPlantColorPicker.setValue(Defaults.PLANT_DEAD);
        wallColorPicker.setValue(Defaults.WALL);
        strokeColorPicker.setValue(Defaults.STROKE);

        liveAnimalColor = Defaults.ANIMAL_LIVE;
        deadAnimalColor = Defaults.ANIMAL_DEAD;
        livePlantColor = Defaults.PLANT_LIVE;
        deadPlantColor = Defaults.PLANT_DEAD;
        wallColor = Defaults.WALL;
        strokeColor = Defaults.STROKE;

        speedSlider.setValue(Defaults.TICK_SPEED);
    }

    /**
     * Attaches change listeners to UI components to handle real-time updates.
     * Manages board resizing, color theme changes, and cell scaling dynamically
     * as the user interacts with the application.
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

        cellWidthSpinner.valueProperty().addListener((_, _, newValue) -> {
            cellWidth = newValue;
            updateCellDimensions();
        });
        cellHeightSpinner.valueProperty().addListener((_, _, newValue) -> {
            cellHeight = newValue;
            updateCellDimensions();
        });

        liveAnimalColorPicker.valueProperty().addListener((_, _, newValue) -> {
            liveAnimalColor = newValue;
            syncUILogicState();
        });
        deadAnimalColorPicker.valueProperty().addListener((_, _, newValue) -> {
            deadAnimalColor = newValue;
            syncUILogicState();
        });

        livePlantColorPicker.valueProperty().addListener((_, _, newValue) -> {
            livePlantColor = newValue;
            syncUILogicState();
        });
        deadPlantColorPicker.valueProperty().addListener((_, _, newValue) -> {
            deadPlantColor = newValue;
            syncUILogicState();
        });
        wallColorPicker.valueProperty().addListener((_, _, newValue) -> {
            wallColor = newValue;
            syncUILogicState();
        });
        strokeColorPicker.valueProperty().addListener((_, _, newValue) -> {
            strokeColor = newValue;
            syncUILogicState();
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

        statsData.addAll(generationStat, livingStat, deceasedStat);
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
        liveAnimalColor = liveAnimalColorPicker.getValue();
        deadAnimalColor = deadAnimalColorPicker.getValue();
        livePlantColor = livePlantColorPicker.getValue();
        deadPlantColor = deadPlantColorPicker.getValue();
        wallColor = wallColorPicker.getValue();
        strokeColor = strokeColorPicker.getValue();
    }

    /**
     * Initializes the {@code AnimationTimer} responsible for the game loop.
     * * <pre>
     * Implementation:  Calculates the tick interval based on the {@code speedSlider}.
     * The tick is executed only if the elapsed time exceeds the interval.
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
                LifeCell cell = createLifeCell(r, c);

                uiState[r][c] = cell;
                gameBoard.add(cell, c, r);
            }
        }

        syncUILogicState();
    }

    /**
     * Instantiates a visual {@code LifeCell}, applies stroke settings, and attaches
     * left/right mouse click event handlers.
     * @param r the row index for the new cell
     * @param c the column index for the new cell
     * @return the configured visual cell object
     */
    private LifeCell createLifeCell(int r, int c) {
        LifeCell cell = new LifeCell(cellWidth, cellHeight);
        cell.setStroke(strokeColor);

        final int row = r;
        final int col = c;
        cell.setOnMouseClicked(event -> {
            MouseButton button = event.getButton();
            if (button == MouseButton.PRIMARY) {
                onCellLeftClicked(cell, row, col);
            } else if (button == MouseButton.SECONDARY) {
                onCellRightClicked(row, col);
            }

        });
        return cell;
    }

    /**
     * Synchronizes the visual state of {@code LifeCell} objects with the internal logic state.
     * Queries the logical model to update the visual color of each cell based on its living status
     * and specific {@code CellType}. Also refreshes the data in the stats table.
     */
    private void syncUILogicState() {
        Color cellColor;

        for (int r = 0; r < uiState.length; r++) {
            for (int c = 0; c < uiState[0].length; c++) {
                boolean living = logicState.isAlive(r, c);
                CellType cellType = logicState.getCell(r, c).getCellType();

                if (cellType == CellType.PLANT) {
                    cellColor = living ? livePlantColor : deadPlantColor;
                } else if (cellType == CellType.ANIMAL) {
                    cellColor = living ? liveAnimalColor : deadAnimalColor;
                } else {
                    cellColor = wallColor;
                }

                uiState[r][c].setFill(cellColor);
                uiState[r][c].setStroke(strokeColor);
            }
        }

        updateStatsTable();
    }

    /**
     * Advances the simulation by calculating the next generation in the logic model,
     * then synchronizing the UI to reflect those changes.
     */
    @FXML
    private void tick() {
        logicState.calculateNextGeneration();
        syncUILogicState();
    }

    /**
     * Handles resizing the logic board when dimensions change.
     * * <pre>
     * Implementation:  Creates a new {@code AbstractCell} array and uses {@code System.arraycopy}
     * to migrate existing cell states to the new dimensions. Space outside the
     * previous bounds will be filled with default dead AnimalCells.
     * Postconditions:  The logic board is updated and the UI is rebuilt.
     * </pre>
     */
    private void handleResize() {
        AbstractCell[][] oldBoardState = logicState.getBoardState();
        AbstractCell[][] nextBoardState = new LifeLogic(boardRows, boardCols).getBoardState();
        int resizedRowCount = Math.min(oldBoardState.length, nextBoardState.length);
        int resizedColCount = Math.min(oldBoardState[0].length, nextBoardState[0].length);

        for (int r = 0; r < resizedRowCount; r++) {
            System.arraycopy(oldBoardState[r], 0, nextBoardState[r], 0, resizedColCount);
        }

        logicState.setBoardState(nextBoardState);

        rebuildUI();
    }

    /**
     * Toggles the living state of a cell within the logic model and updates its visual color
     * immediately without requiring a full UI sync.
     * @param cell the {@code LifeCell} UI component that was clicked (must not be null)
     * @param row  the row index of the cell
     * @param col  the column index of the cell
     */
    private void onCellLeftClicked(LifeCell cell, int row, int col) {
        logicState.toggleLiving(row, col);

        CellType cellType = logicState.getCell(row, col).getCellType();
        boolean living = logicState.isAlive(row, col);
        Color cellColor;

        if (cellType == CellType.PLANT) {
            cellColor = living ? livePlantColor : deadPlantColor;
        } else if (cellType == CellType.ANIMAL) {
            cellColor = living ? liveAnimalColor : deadAnimalColor;
        } else {
            cellColor = wallColor;
        }

        cell.setFill(cellColor);
    }

    /**
     * Replaces the target cell in the logic model with a new cell type (Animal, Plant, or Wall)
     * depending on which radio toggle is selected, then synchronizes the UI.
     * @param row  the row index of the cell
     * @param col  the column index of the cell
     */
    private void onCellRightClicked(int row, int col) {
        Toggle cellToggle = cellTypeGroup.getSelectedToggle();
        AbstractCell newCell;

        if (cellToggle.equals(animalToggle)) {
            newCell = new AnimalCell(false);
        } else if (cellToggle.equals(plantToggle)) {
            newCell = new PlantCell(true);
        } else {
            newCell = new WallCell();
        }

        logicState.setCell(row, col, newCell);
        syncUILogicState();
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
        startStopButton.setText(isPaused ? "Resume" : "Start");
        gameLoop.stop();
    }

    /**
     * Acts as a toggle switch, calling either {@code startGame()} or {@code stopGame()}
     * based on the current state of the animation loop.
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
     */
    @FXML
    private void resetGame() {
        isRunning = false;
        gameLoop.stop();
        startStopButton.setText("Start");

        setDefaultValues();
        logicState = new LifeLogic(boardRows, boardCols);
        updateStatsTable();
    }

    /**
     * Leaves current settings intact but wipes the current board by instantiating
     * a new {@code LifeLogic} instance and syncing the UI.
     * <pre>
     * Postconditions:  All cells in logicState are set to dead.
     * The UI is updated to reflect the empty board.
     * </pre>
     */
    @FXML
    private void clearBoard() {
        stopGame();
        logicState = new LifeLogic(boardRows, boardCols);
        syncUILogicState();
    }
}
