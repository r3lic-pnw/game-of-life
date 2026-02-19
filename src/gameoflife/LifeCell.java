package gameoflife;

import javafx.scene.shape.Rectangle;

public class LifeCell extends Rectangle {
    private boolean living = false;

    public LifeCell(int i, int j) {
        super(i, j);
    }

    public boolean isLiving() {return living;}
    public void setLiving(boolean living) {this.living = living;}
    public void toggleLiving() {this.living = !this.living;}
}
