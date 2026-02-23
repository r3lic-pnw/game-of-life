package gameoflife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings({"UnnecessaryModifier", "unused"})
public class RunGame extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOfLife.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Conway's Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        RunGame.launch(args);
    }
}
