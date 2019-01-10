package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.paint.Color;

/**
 * MazeDisplayer is a controller for displaying the maze and is in charge of:
 * drawing the maze at first, and then redraws every change of the player's position.
 * drawing the path solution
 * clearing the maze
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class MazeDisplayer extends Canvas {

    private GraphicsContext graphicsContext;
    private int[][] maze;
    private int currRow;
    private int currCol;
    private int goalRow;
    private int goalCol;
    private int prevRow;
    private int prevCol;
    private int[][] solution;
    private int startRow;
    private int startCol;

    /* Constructor */
    public void createMazeDisplayer(int[][] maze,  int currRowMaze, int currColMaze, int goalRowMaze, int goalColMaze, int startRowMaze, int startColMaze) {
        this.maze = maze;
        this.currRow = currRowMaze;
        this.currCol = currColMaze;
        this.prevRow = currRowMaze;
        this.prevCol = currColMaze;
        this.goalRow = goalRowMaze;
        this.goalCol = goalColMaze;
        this.startRow = startRowMaze;
        this.startCol = startColMaze;

        redraw();
    }

    /**
     * function draws the board (initiated only at first)
     */
    private void redraw() {
        try {
            double canvasHeight = super.getHeight(), canvasWidth = super.getWidth();
            double cellHeight = (canvasHeight / maze.length), cellWidth = (canvasWidth / maze[0].length);

            Image imageWall = new Image(new FileInputStream(imageForWall.get()));
            Image imagePlayer = new Image(new FileInputStream(imageForPlayer.get()));
            Image imageGoal = new Image(new FileInputStream(imageForGoal.get()));
            Image imageStart = new Image(new FileInputStream(imageForStart.get()));

            graphicsContext = this.getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight); //Clean the canvas
            graphicsContext.drawImage(imageGoal, this.goalCol * cellWidth, this.goalRow * cellHeight, cellWidth, cellHeight);

            if (wasSavedMaze())
                setPastStartMazePos();

            graphicsContext.drawImage(imageStart, this.startCol * cellWidth, this.startRow * cellHeight, cellWidth, cellHeight);

            for (int i = 0; i < maze.length; i++)
                for (int j = 0; j < maze[i].length; j++)
                    if (maze[i][j] == 1)
                        graphicsContext.drawImage(imageWall, j * cellWidth, i * cellHeight, cellWidth, cellHeight);

            graphicsContext.drawImage(imagePlayer, currCol * cellWidth, currRow * cellHeight, cellWidth, cellHeight);

        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
            alert.show();
        }
    }

    /**
     * function draws the new player's position
     */
    private void redrawPlayer() {
        try {
            double canvasHeight = super.getHeight(), canvasWidth = super.getWidth();
            double cellHeight = (canvasHeight / maze.length), cellWidth = (canvasWidth / maze[0].length);
            Image imageStart = new Image(new FileInputStream(imageForStart.get()));
            Image imagePlayer = new Image(new FileInputStream(imageForPlayer.get()));
            graphicsContext = this.getGraphicsContext2D();
            if ((this.prevRow == this.startRow && this.prevCol == this.startCol)) {
                graphicsContext.clearRect(this.prevCol * cellWidth, this.prevRow * cellHeight, cellWidth, cellHeight);
                graphicsContext.drawImage(imageStart, this.startCol * cellWidth, this.startRow * cellHeight, cellWidth, cellHeight);
            }
            else
                graphicsContext.clearRect(this.prevCol * cellWidth, this.prevRow * cellHeight, cellWidth, cellHeight);
            graphicsContext.drawImage(imagePlayer, currCol * cellWidth, currRow * cellHeight, cellWidth, cellHeight);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
            alert.show();
        }
    }

    /**
     * function draws the solution on the board
     */
    public void redrawSolution() {
        double canvasHeight = super.getHeight(), canvasWidth = super.getWidth();
        double cellHeight = (canvasHeight / maze.length), cellWidth = (canvasWidth / maze[0].length);

        graphicsContext = this.getGraphicsContext2D();

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (pointInTheSolution(i, j) && !currPoint(i, j)) {
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillOval(j * cellWidth + cellWidth / 8, i * cellHeight + cellHeight / 8, cellWidth / 2, cellHeight / 2);
                }
            }
        }
    }

    /**
     * function receives 2D array for this instance
     * @param s maze
     */
    public void setSolution(int[][] s) {
        this.solution = s ;
    }

    /**
     * function checks if the solution point is on the maze board point
     * @param row row
     * @param col col
     * @return boolean
     */
    private boolean pointInTheSolution(int row, int col) {
        for (int i = 0; i < solution.length; i++){
            if(this.solution[i][0] == row && this.solution[i][1] == col)
                return true;
        }
        return false;
    }

    /**
     * function checks if the solution point is on the maze board point
     * @param i row
     * @param j col
     * @return boolean
     */
    private boolean currPoint( int i, int j) {
        return (this.currRow == i && this.currCol == j);
    }

    /**
     * function checks if the new save checkpoint is associated with a loaded maze file
     */
    private boolean wasSavedMaze() {
        return this.currRow > 0 && this.currRow < this.maze.length-1 && this.currCol > 0 && this.currCol < this.maze[0].length-1;
    }

    /**
     * function checks if the new save checkpoint is associated with a loaded maze file
     */
    private void setPastStartMazePos() {
        int colFrame = maze[0].length;
        int rowFrame = maze.length;
        for (int i = 0; i < rowFrame; i++) {
            if (maze[i][0] == 0 && goalRow != i && goalCol != 0) {
                this.startRow = i;
                this.startCol = 0;
            }
            if (maze[i][colFrame-1] == 0 &&  goalRow != i && goalCol != colFrame-1) {
                this.startRow = i;
                this.startCol = colFrame - 1;
            }
        }
        for (int j = 0; j < colFrame; j++) {
            if (maze[0][j] == 0 && goalRow != 0 && goalCol != j) {
                this.startRow = 0;
                this.startCol = j;
            }
            if (maze[rowFrame-1][j] == 0 &&  goalRow != rowFrame-1 && goalCol != j) {
                this.startRow = rowFrame - 1;
                this.startCol = j;
            }
        }
    }

    /**
     * function clears maze
     */
    public void clearMaze() {
        double canvasHeight = super.getHeight(), canvasWidth = super.getWidth();
        double cellHeight = (canvasHeight / maze.length), cellWidth = (canvasWidth / maze[0].length);
        graphicsContext = this.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
    }

    /**
     * function sets new player's position on the board
     * @param row row
     * @param col col
     */
    public void setCurrPos(int row, int col) {
        this.prevRow = this.currRow;
        this.prevCol = this.currCol;
        this.currRow = row;
        this.currCol = col;
        if (maze != null)
            redrawPlayer();
    }

    /**
     * function sets goal position on the board
     * @param row row
     * @param col col
     */
    public void setGoalPos(int row, int col) {
        this.goalRow = row;
        this.goalCol = col;
        redrawPlayer();
    }

    /**
     * getter for maze 2D array
     */
    public int[][] getMaze() {
        return maze;
    }

    /* IMAGES */

    private StringProperty imageForWall = new SimpleStringProperty();
    private StringProperty imageForPlayer = new SimpleStringProperty();
    private StringProperty imageForStart = new SimpleStringProperty();
    private StringProperty imageForGoal = new SimpleStringProperty();

    public String getImageForWall() {
        return imageForWall.get();
    }

    public void setImageForWall(String imageForWall) {
        this.imageForWall.set(imageForWall);
    }

    public String getImageForPlayer() {
        return imageForPlayer.get();
    }

    public void setImageForPlayer(String imageForPlayer) {
        this.imageForPlayer.set(imageForPlayer);
    }

    public String getImageForStart() {
        return imageForStart.get();
    }

    public void setImageForStart(String imageForStart) {
        this.imageForStart.set(imageForStart);
    }

    public String getImageForGoal() {
        return imageForGoal.get();
    }

    public void setImageForGoal(String imageForGoal) {
        this.imageForGoal.set(imageForGoal);
    }

}
