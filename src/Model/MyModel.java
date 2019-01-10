package Model;


import algorithms.mazeGenerators.*;
import algorithms.search.Solution;
import algorithms.search.AState;
import Server.*;
import Client.*;
import IO.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Model layer is responsible for:
 * Communication with servers that are in charge of creating and solving mazes.
 * Using Best First Search, Breadth First Search, Depth First Search algorithms.
 * Saves the maze and the current played position of the user.
 *
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class MyModel extends Observable implements IModel {

    private ExecutorService Executor;
    private Maze maze; // represents the current Maze, start Position, Goal position ans the size of the Maze
    private Position playerCurrPos; // represents the current position of the player
    private Position playerStartPos;
    private Position playerGoalPos; // represents the goal position of the maze
    private Solution mazeSolution; // the solution of the current Maze.
    private Server mazeGeneratingServer; // using the server to generate a maze
    private Server solveSearchProblemServer;// using the server to solve the current maze
    private boolean playerWin;

    /**
     * Constructor
     */
    public MyModel() {
        Executor =  Executors.newFixedThreadPool(Configurations.getNumberOfThreads());
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        playerCurrPos = null;
        playerStartPos = null;
        playerGoalPos = null;
        mazeSolution = null;
        playerWin = false;
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }

    /**
     * function generates the maze
     * @param row number of rows to generate
     * @param col number of columns to generate
     */
    public void generateMaze(int row, int col) {
        //Validating Position
        if(row > 120)
            row = 120;
        else if(row < 10)
            row = Configurations.getMinRow();
        if(col > 120 )
            col = 120;
        else if(col < 10)
            col = Configurations.getMinColumn();

        final int fRow = row;
        final int fCol = col;

        //Starts server for generating the maze
        Executor.execute(()-> {
            try
            {
                Client client = new Client(InetAddress.getLocalHost(), 5400,(inFromServer, outToServer) -> {
                    try
                    {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{fRow,fCol};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[fRow*fCol+13 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        this.maze = new Maze(decompressedMaze);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });  //new client
                client.communicateWithServer();
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
            playerCurrPos = new Position(this.maze.getStartPosition().getRowIndex(),this.maze.getStartPosition().getColumnIndex());
            playerStartPos = new Position(this.maze.getStartPosition().getRowIndex(),this.maze.getStartPosition().getColumnIndex());
            playerGoalPos = new Position(this.maze.getGoalPosition().getRowIndex(),this.maze.getGoalPosition().getColumnIndex());
            setChanged();
            notifyObservers("Model Done Generating Maze");
        }); //executor
    }

    /**
     * function resets board
     */
    public void reset() {
        this.maze = null;
        this.playerWin = false;
        this.mazeSolution = null;
        this.playerStartPos = null;
        this.playerCurrPos = null;
        this.playerGoalPos = null;
        setChanged();
        notifyObservers("Model Done Reset");
    }

    /**
     * @return 2D array of the solution path
     */
    public int[][] solutionArray() {

        if(this.mazeSolution!=null){
            ArrayList<AState> arrayList = this.mazeSolution.getSolutionPath();
            int[][] array = new int[arrayList.size()][2];
            int index = 0;
            for(AState a: arrayList){
                array[index][0]= a.getCurrPos().getRowIndex();
                array[index][1]= a.getCurrPos().getColumnIndex();
                index++;
            }
            return array;
        }
        return null;

    }

    /**
     * @return if player reached goal position
     */
    public boolean playerWin() {
        return this.playerWin;
    }

    /**
     * function returns the current maze
     * @return current maze 2D array
     */
    public int[][] getMaze() {
        return this.maze.getMazeArray();
    }

    /**
     * @return Position player's position
     * @see  Position
     */
    public Position playerPosition(){
        return playerCurrPos;
    }

    /**
     * @return Position goals position
     * @see  Position
     */
    public Position goalPosition() {
        return playerGoalPos;
    }

    /**
     * @return Position player's start position
     * @see  Position
     */
    public Position playerStartPosition(){
        return playerStartPos;
    }

    /**
     * @return boolean if player got to the goal position
     */
    private boolean checkIfWon() {
        if(playerCurrPos.getRowIndex() == this.maze.getGoalPosition().getRowIndex() && playerCurrPos.getColumnIndex() == this.maze.getGoalPosition().getColumnIndex()){
            playerWin = true;
            return playerWin;
        }
        else {
            playerWin = false;
            return false;
        }
    }

    /**
     * @param row number of rows
     * @param col number of columns
     * @return boolean if player got to the goal position
     */
    private boolean checkInBound(int row , int col) {
        if (row < this.maze.getMazeArray().length && row >= 0 && col < this.maze.getMazeArray()[0].length && col >=0 )
            return true;
        else
            return false;
    }

    /**
     * function changes row and column relatively to the board
     * @param newRow row inc/dec
     * @param newCol col inc/dec
     */
    public void changePos(int newRow, int newCol) {
        if(checkInBound(playerCurrPos.getRowIndex() + newRow, playerCurrPos.getColumnIndex() + newCol )) {
            if (Math.abs(newRow) <= 1 && Math.abs(newCol) <= 1) {
                int newRowIndex = playerCurrPos.getRowIndex() + newRow;
                int newColIndex = playerCurrPos.getColumnIndex() + newCol;
                int k = this.maze.getMazeArray()[newRowIndex][newColIndex];
                if (this.maze.getMazeArray()[newRowIndex][newColIndex] == 0) {
                    playerCurrPos.setRow(newRowIndex);
                    playerCurrPos.setCol(newColIndex);
                    if (checkIfWon()) {
                        setChanged();
                        notifyObservers("Model Player Won!");
                    }
                    else {
                        setChanged();
                        notifyObservers("Model Player On The Move.");
                    }
                    return;
                }
                setChanged();
                notifyObservers("Can't Move There's A Wall!");
                return;
            }
            setChanged();
            notifyObservers("Invalid move!");
            return;
        }
        setChanged();
        notifyObservers("Out Of Bounds!");
    }

    /**
     * function solves the board
     */
    public void solveMaze() {
        Executor.execute(()-> {
            this.maze.setStartPosition(this.playerCurrPos);
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5401, ( inFromServer, outToServer) ->{ {
                    try
                    {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(this.maze);
                        toServer.flush();
                        mazeSolution = (Solution)fromServer.readObject();
                    }
                    catch (Exception var10)
                    {
                        var10.printStackTrace();
                    }
                }
                });
                client.communicateWithServer();
            } catch (UnknownHostException var1) {
                var1.printStackTrace();
            }
            setChanged();
            notifyObservers("Done Solving Maze.");
        });
    }

    /**
     * function does an orderly exit
     */
    public void exit() {
        Executor.shutdown(); //join to the Thread
        solveSearchProblemServer.stop(); // stop Solve server
        mazeGeneratingServer.stop(); // stop generate server
    }

    /**
     * function saves the board
     * @param fileName name of the file to be saved
     */
    public boolean saveMaze(String fileName) {
        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(fileName));
            this.maze.setStartPosition(this.playerCurrPos);
            out.write(this.maze.toByteArray());
            out.flush();
            out.close();
        }
        catch(IOException e) {
            return false;
        }
        return true;
    }

    /**
     * function loads a board that was saved
     * @param fileName name of the file to be loaded
     */
    public boolean loadMaze(String fileName) {
        File mazeFile = new File(fileName);
        if (mazeFile.isFile()) {
            try {
                InputStream input = new MyDecompressorInputStream(new FileInputStream(fileName));
                byte[] returnMaze = new byte[120 * 120 + 12]; // max size of the maze
                input.read(returnMaze);
                this.maze = new Maze(returnMaze);
                input.close();
            } catch (IOException e) {
                return false;
            }
            /*
            this.playerStartPos = maze.getStartPosition();
            this.playerGoalPos = maze.getGoalPosition();
            this.playerCurrPos = maze.getStartPosition();
            */
            Position start = new Position(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
            Position goal = new Position(maze.getGoalPosition().getRowIndex(),maze.getGoalPosition().getColumnIndex());
            //this.playerStartPos = start;
            this.playerGoalPos = goal;
            this.playerCurrPos = start;
            this.playerWin = false;

            setChanged();
            notifyObservers("Model Done Generating Maze");
            return true;
        }
        return false;
    }

    /**
     * function returns the solution of the board
     * @return maze solution
     * @see Solution
     */
    public Solution getSolution() {
        return mazeSolution;
    }

}

