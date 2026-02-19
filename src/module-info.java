module gameoflife {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens gameoflife to javafx.fxml, javafx.graphics;

    exports gameoflife;
}