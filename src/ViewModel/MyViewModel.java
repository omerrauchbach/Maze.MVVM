package ViewModel;

import Model.IModel;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.KeyCode;
import java.util.Observable;
import java.util.Observer;

/**
 * The View Model layer is responsible for communication between View and Model classes
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class MyViewModel extends Observable implements Observer {

    private IModel refModel;
    public IntegerProperty currRow = new SimpleIntegerProperty();
    public IntegerProperty currCol = new SimpleIntegerProperty();
    public StringProperty statusBar = new SimpleStringProperty();

    /* Constructor */
    public MyViewModel(IModel newModel) {
        this.refModel = newModel;
        statusBar.setValue("Welcome! Press Start To Begin!");
    }

    /**
     * Observer: synchronized update function receives updates regarding to:
     * (1) maze generation
     * (2) player winning
     * (3) player moving on the board
     * (4) player tries to go through a wall
     * (5) maze is reset
     * (6) maze solver
     */
    public synchronized void update(Observable o, Object arg) {
        if (o == refModel) {
            if (arg == "Model Done Generating Maze") {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        currRow.set(getPlayerPosRow());
                        currCol.set(getPlayerPosCol());
                        statusBar.set("Done Creating Maze.");
                        setChanged();
                        notifyObservers("ViewModel Done Generating Maze");
                    }
                });

            }
            else if (arg == "Model Player Won!") {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        currRow.set(getPlayerPosRow());
                        currCol.set(getPlayerPosCol());
                        statusBar.set("Congratulations You Won!");
                        setChanged();
                        notifyObservers("ViewModel Player Won!");
                    }
                });
            }
            else if (arg == "Model Player On The Move.") {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        currRow.set(getPlayerPosRow());
                        currCol.set(getPlayerPosCol());
                        statusBar.set("Player On The Move.");
                        setChanged();
                        notifyObservers("ViewModel Player On The Move.");
                    }
                });

            }
            else if (arg == "Model Can't Move There's A Wall!") {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        currRow.set(getPlayerPosRow());
                        currCol.set(getPlayerPosCol());
                        statusBar.set("Can't Move There's A Wall!");
                        setChanged();
                        notifyObservers("ViewModel Can't Move There's A Wall");
                    }
                });
            }
            else if (arg == "Model Done Reset") {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusBar.set("Reset");
                        setChanged();
                        notifyObservers("ViewModel Done Reset");
                    }
                });
            }
            else if (arg == "Can't Move There's A Wall!" || arg== "Invalid move!" || arg == "Out Of Bounds!") {
                String txt = (String)arg;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        currRow.set(getPlayerPosRow());
                        currCol.set(getPlayerPosCol());
                        statusBar.set(txt);
                    }
                });
            }
            else if (arg == "Done Solving Maze."){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        currRow.set(getPlayerPosRow());
                        currCol.set(getPlayerPosCol());
                        statusBar.set("trying to solve the maze");
                        setChanged();
                        notifyObservers("trying to solve the maze");
                    }
                });
            }
        }
    }

    /* MVVM functions */

    /* function generates board */
    public void generateMaze(int row, int col) {
        refModel.generateMaze(row, col);
    }

    /* function updates player's position */
    public void changePos(int changeOfRow, int changeOfCol) {
        refModel.changePos(changeOfRow,changeOfCol);
    }

    /* function receives a specific key and translates it to a movement */
    public void KeyPressed(int changeOfRow, int changeOfCol) {
        refModel.changePos(changeOfRow,changeOfCol);
    }

    /* function in charge of indicating if the player wins  */
    public boolean playerWin() {
        return this.refModel.playerWin();
    }

    /* function saves board */
    public void saveMaze(String path) {
        this.refModel.saveMaze(path);
    }

    /* function loads board */
    public boolean loadMaze(String path) {
        return this.refModel.loadMaze(path);
    }

    /* function generates solution */
    public void solveTheMaze(){this.refModel.solveMaze();}

    /* function exits game */
    public void exit() {
        this.refModel.exit();
    }

    /* function resets board */
    public void reset() {
        currRow.setValue(0);
        currCol.setValue(0);
        this.refModel.reset();}


    /* Getters */

    public int[][] getMaze() {
        return refModel.getMaze();
    }

    public int getPlayerStartPosRow() {
        return refModel.playerStartPosition().getRowIndex();
    }

    public int getPlayerStartPosCol() {
        return refModel.playerStartPosition().getColumnIndex();
    }

    public int getPlayerPosRow() {
        return refModel.playerPosition().getRowIndex();
    }

    public int getPlayerPosCol() {
        return refModel.playerPosition().getColumnIndex();
    }

    public int getGoalPosRow() {
        return refModel.goalPosition().getRowIndex();
    }

    public int getGoalPosCol() {
        return refModel.goalPosition().getColumnIndex();
    }

    public int[][] getSolution(){
        return this.refModel.solutionArray();
    }

    public StringProperty getStringProperty(){
        return statusBar;
    }

}