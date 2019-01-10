package Model;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

/**
 * Interface class represents the MVVM model.
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public interface IModel {

    /**
     * function generates the maze
     * @param row number of rows to generate
     * @param col number of columns to generate
     */
    void generateMaze(int row, int col);

    /**
     * function returns the current maze
     * @return current maze 2D array
     */
    int[][] getMaze();

    /**
     * @return Position player's position
     * @see  Position
     */
    Position playerPosition();

    /**
     * @return Position player's start position
     * @see  Position
     */
    Position playerStartPosition();

    /**
     * @return Position goals position
     * @see  Position
     */
    Position goalPosition();

    /**
     * function changes row and column relatively to the board
     * @param rowChange row inc/dec
     * @param columnChange col inc/dec
     */
    void changePos(int rowChange, int columnChange);

    /**
     * @return if player reached goal position
     */
    boolean playerWin();

    /**
     * function resets board
     */
    void reset();

    /**
     * @return 2D array of the solution path
     */
    int[][] solutionArray();


    /**
     * function solves the board
     */
    void solveMaze();

    /**
     * function returns the solution of the board
     * @return maze solution
     * @see Solution
     */
    Solution getSolution();

    /**
     * function does an orderly exit
     */
    void exit();

    /**
     * function saves the board
     * @param fileName name of the file to be saved
     */
    boolean saveMaze(String fileName);

    /**
     * function loads a board that was saved
     * @param fileName name of the file to be loaded
     */
    boolean loadMaze(String fileName);
}
