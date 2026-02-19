package gameoflife;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Conways Game of Life Rules:
 *
 * <p>
 * 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
 * 2. Any live cell with two or three live neighbours lives on to the next generation.
 * 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
 * 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 */
@SuppressWarnings("CommentedOutCode")
public class GameOfLifeController {
    @FXML
    private GridPane gameBoard;
    @FXML
    private Spinner<Integer> colSpinner;
    @FXML
    private Spinner<Integer> rowSpinner;
    @FXML
    private Spinner<Integer> cellWidthSpinner;
    @FXML
    private Spinner<Integer> cellHeightSpinner;
    @FXML
    private  Slider speedSlider;
//    @FXML
//    private ComboBox<Color> deadColorComboBox;
//    @FXML
//    private ComboBox<Color> liveColorComboBox;
//    @FXML
//    private ComboBox<Color> strokeColorComboBox;

    @FXML
    private Button startStopButton;
    @FXML
    private TableView<GameStat> statsTable;
    @FXML
    private TableColumn<GameStat, String> statCol;
    @FXML
    private TableColumn<GameStat, String> valueCol;

    private final ObservableList<GameStat> statsData = FXCollections.observableArrayList();

    private LifeCell[][] gameState;
    private int ROWS;
    private int COLS;
    private int CELL_WIDTH;
    private int CELL_HEIGHT;
//    private Color deadColor;
//    private Color liveColor;
//    private Color strokeColor;

    private AnimationTimer gameLoop;
    private boolean isRunning = false;

    @FXML
    public void initialize() {
        // Setup row/col spinners
        SpinnerValueFactory<Integer> widthFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 12);
        SpinnerValueFactory<Integer> heightFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 12);

        colSpinner.setValueFactory(widthFactory);
        rowSpinner.setValueFactory(heightFactory);

        // Event listeners to change gameBoard size
        colSpinner.valueProperty().addListener((_, _, _) -> {
            buildGameBoard(); // rebuild whenever width changes
        });
        rowSpinner.valueProperty().addListener((_, _, _) -> {
            buildGameBoard(); // rebuild whenever height changes
        });

        // Setup cellWidth/height spinners
        SpinnerValueFactory<Integer> cellWidthFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 30);
        SpinnerValueFactory<Integer> cellHeightFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 30);
        cellWidthSpinner.setValueFactory(cellWidthFactory);
        cellHeightSpinner.setValueFactory(cellHeightFactory);

        cellWidthSpinner.valueProperty().addListener((_, _, _) -> buildGameBoard());
        cellHeightSpinner.valueProperty().addListener((_, _, _) -> buildGameBoard());

        // Setup color combo boxes
//        deadColorComboBox.setValue(Color.RED);
//        liveColorComboBox.setValue(Color.GREEN);
//        strokeColorComboBox.setValue(Color.BLUE);

//        deadColorComboBox.setCellFactory();

        // Setup statsTable
        statCol.setCellValueFactory(new PropertyValueFactory<>("stat"));
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        statsData.add(new GameStat("Generations", 0));
        statsData.add(new GameStat("Living Cells", 0));
        statsData.add(new GameStat("Deceased Cells", 0));

        statsTable.setItems(statsData);

        // Build the gameBoard
        buildGameBoard();

        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                double speedInMillis = speedSlider.getValue();

                long interval = (long) (speedInMillis * 1_000_000);
                if (now - lastUpdate >= interval) {
                    calculateNextGeneration();
                    lastUpdate = now;
                }
            }
        };
    }


    // HELPER METHODS
    private void buildGameBoard() {
        if (isRunning) {
            gameLoop.stop();
            isRunning = false;
            startStopButton.setText("Start");
        }

        // Update dimensions from spinners
        ROWS = rowSpinner.getValue();
        COLS = colSpinner.getValue();
        CELL_WIDTH = cellWidthSpinner.getValue();
        CELL_HEIGHT = cellHeightSpinner.getValue();

        gameState = new LifeCell[ROWS][COLS];
        gameBoard.getChildren().clear();

        // Repopulate gameBoard
        populateGameBoard();

    }

    private void populateGameBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                LifeCell cell = new LifeCell(CELL_WIDTH, CELL_HEIGHT);
//                cell.setFill(deadColor);
//                cell.setStroke(strokeCol
                cell.setFill(Color.RED);
                cell.setStroke(Color.WHITE);

                cell.setOnMouseClicked(_ -> {
                    if (isRunning) {
                        stopGame();
                    }
                    cell.toggleLiving();
                    cell.setFill(cell.isLiving() ? Color.GREEN : Color.RED);
                });

                gameState[r][c] = cell;
                gameBoard.add(cell, c, r);
            }
        }
    }

    private void calculateNextGeneration() {
        // Temporary board to store the next calculated state
        boolean[][] nextState = new boolean[ROWS][COLS];

        // Counters for the statsTable
        int livingCount = 0;
        int generationCount = statsData.get(0).getValue();
        int deceasedCount = statsData.get(2).getValue();

        // Loop through every RunGame.LifeCell to calculate its fate
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                LifeCell cell = gameState[r][c];
                int livingNeighbors = countLivingNeighbors(r, c);

                if (cell.isLiving()) {
                    if (livingNeighbors == 2 || livingNeighbors == 3) {
                        // Rule 2: Survival (2 or 3 neighbors)
                        nextState[r][c] = true;
                        livingCount++;
                    } else {
                        // Rules 1 & 3: Death (Under/Over population)
                        nextState[r][c] = false;
                        deceasedCount++;
                    }
                } else {
                    if (livingNeighbors == 3) {
                        // Rule 4: Reproduction (Exactly 3 neighbors)
                        nextState[r][c] = true;
                        livingCount++;
                    }
                }
            }
        }

        // Apply new gameBoardState
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                LifeCell cell = gameState[r][c];

                if (cell.isLiving() != nextState[r][c]) {
                    cell.toggleLiving();
                    cell.setFill(cell.isLiving() ? Color.GREEN : Color.RED);
                }
            }
        }

        // Update statsTable data
        statsData.get(0).setValue(generationCount + 1);
        statsData.get(1).setValue(livingCount);
        statsData.get(2).setValue(deceasedCount);
    }

    private int countLivingNeighbors(int row, int col) {
        int count = 0;

        // Check the 3x3 grid around the RunGame.LifeCell
        // r is the row offset (-1, 0, 1)
        // c is the col offset (-1, 0, 1)
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {

                // Skip the center (0, 0), which is the current cell being checked
                if (r == 0 && c == 0) continue;

                // Boundary check to see if we are on the edge of the board
                int neighborRow = row + r;
                int neighborCol = col + c;
                if (neighborRow >= 0 && neighborRow < ROWS && neighborCol >= 0 && neighborCol < COLS) {
                    if (gameState[neighborRow][neighborCol].isLiving()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private void stopGame() {
        gameLoop.stop();
        isRunning = false;
        startStopButton.setText("Resume");
    }

    private void startGame() {
        gameLoop.start();
        isRunning = true;
        startStopButton.setText("Pause");
    }

    // EVENT HANDLERS
    @FXML
    public void onStartStopClicked() {
        if (isRunning) {
            stopGame();
        } else {
            startGame();
        }
    }

    @FXML
    public void onResetClicked() {
        if (isRunning) {
            stopGame();
        }
        startStopButton.setText("Start");
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                LifeCell cell = gameState[r][c];
                cell.setLiving(false);
                cell.setFill(Color.RED);
            }
        }

        speedSlider.setValue(200);
        statsData.forEach(gamestat -> gamestat.setValue(0));
    }

    public static class GameStat {
        SimpleStringProperty stat;
        SimpleIntegerProperty value;

        public GameStat(String stat, int value) {
            this.stat = new SimpleStringProperty(stat);
            this.value = new SimpleIntegerProperty(value);
        }

        public int getValue() {
            return value.get();
        }

        public void setValue(int value) {
            this.value.set(value);
        }

        public String getStat() {
            return stat.get();
        }
    }

}
