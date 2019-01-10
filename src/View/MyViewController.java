package View;

import Server.Configurations;
import ViewModel.MyViewModel;

import java.io.File;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.Observable;

import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


/**
 * MyViewController is in charge of operating and representing the main stage where we have the maze.
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class MyViewController implements Observer, IView {

    @FXML
    public MyViewModel refViewModel;
    public MazeDisplayer mazeDisplayer;
    public MenuItem file_new;
    public MenuItem file_save;
    public MenuItem file_load;
    public MenuItem file_exit;
    public MenuItem settings_control;
    public MenuItem help_about;
    public Button btn_clear;
    public TextField textField_RowNumber;
    public TextField textField_ColNumber;
    public Button btn_start;
    public Label lbl_PosRowNumber;
    public Label lbl_PosColNumber;
    public Button btn_iGiveUp;
    public Button btn_solution;
    public Button btn_controls;
    public CheckBox check_mute;
    public Label lbl_status;
    private Media startMusic;
    private MediaPlayer startMediaPlayer;
    private Media backGroundtMusic;
    private MediaPlayer backGroundMediaPlayer;
    private boolean didStartMaze = false;
    private int startRow = Configurations.getMinRow();
    private int startCol = Configurations.getMinColumn();
    private Scene scene;
    private Stage primaryStage;

    /* Constructor */
    public MyViewController(MyViewModel viewModel) {
        this.refViewModel = viewModel;
        startMusic = new Media(new File("./src/resources/sounds/opening.mp3").toURI().toString());
        startMediaPlayer = new MediaPlayer(startMusic);
        backGroundtMusic = new Media(new File("./src/resources/sounds/background.mp3").toURI().toString());
        backGroundMediaPlayer = new MediaPlayer(backGroundtMusic);
    }

    /**
     * Observer: update function receives updates regarding to:
     * (1) maze generation
     * (2) player winning
     * (3) player moving on the board
     * (4) player tries to go through a wall
     * (5) maze is reset
     * (6) maze solver
     */
    public void update(Observable o, Object arg) {
        if (o == refViewModel) {
            if (arg == "ViewModel Done Generating Maze")
                displayMaze();
            else
                if (arg == "ViewModel Player Won!") {
                displayMaze();
                setWinner();
            }
            else
                if (arg == "ViewModel Player On The Move.")
                    Play();
            else
                if (arg == "trying to solve the maze") {
                    mazeDisplayer.setSolution(refViewModel.getSolution());
                    mazeDisplayer.redrawSolution();
            }
            else
            if (arg == "ViewModel Done Reset") {
                if (!check_mute.isSelected())
                    playBackGroundSound();
                didStartMaze = false;
            }
        }
    }

    /**
     * function of generating the maze
     * receives int row and int column from the text field.
     */
    public void startMaze() {
        btn_clear.setDisable(true);
        btn_start.setDisable(true);
        didStartMaze = true;
        try {
            int rows = Integer.valueOf(textField_RowNumber.getText());
            int cols = Integer.valueOf(textField_ColNumber.getText());
            if (rows <= 0 || cols <= 0)
                lbl_status.setText("Created a default maze");
            refViewModel.generateMaze(rows,cols);

        } catch (Exception e) {
            showAlert("Try entering 10 or above.");
            btn_start.setDisable(false);
            btn_clear.setDisable(false);
        }
    }

    /**
     * function draws the board after the server generates it
     */
    public void displayMaze() {
        mazeDisplayer.createMazeDisplayer(refViewModel.getMaze(),refViewModel.getPlayerPosRow(),refViewModel.getPlayerPosCol(),refViewModel.getGoalPosRow(),refViewModel.getGoalPosCol(),refViewModel.getPlayerStartPosRow(),refViewModel.getPlayerStartPosCol());
        btn_start.setDisable(false);
        btn_clear.setDisable(false);
    }

    /**
     * function draws the player's movement on each step
     */
    private void Play() {
        mazeDisplayer.setCurrPos(refViewModel.getPlayerPosRow(), refViewModel.getPlayerPosCol());
    }

    /**
     * function clears the board
     */
    public void clear() {
        if (didStartMaze) {
            btn_clear.setDisable(true);
            ConfirmController CB = new ConfirmController();
            int ans = CB.confirm();
            if(ans == 1) { //clear
                didStartMaze = false;
                mazeDisplayer.clearMaze();
                refViewModel.reset();
                btn_clear.setDisable(false);
            }
            else if (ans == 2) { //save and exit
                saveMaze();
                btn_clear.setDisable(false);
            }
            else if (ans == 0) { //save and exit
                btn_clear.setDisable(false);
            }
        }
        else
            showAlert("You must start a maze inorder to clear");
    }


    /**
     * function in charge of displaying messages in the status line.
     * uses binding
     */
    public void setStatus() {
        lbl_status.textProperty().bind(refViewModel.getStringProperty());
        lbl_PosRowNumber.textProperty().bind(refViewModel.currRow.asString());
        lbl_PosColNumber.textProperty().bind(refViewModel.currCol.asString());
    }

    /* Key Event Functions */
    public void MouseEventHandler(MouseEvent m) {
        synchronized (this) {
            if (refViewModel.getMaze() != null && !refViewModel.playerWin())
                try {
                    double cellHeight = mazeDisplayer.getHeight() / refViewModel.getMaze().length;
                    double cellWidth = mazeDisplayer.getWidth() / refViewModel.getMaze()[0].length;
                    int xPosition = (int)m.getX();
                    int yPosition = (int)m.getY();
                    int newPlayerPositionX = (int) (xPosition / cellWidth) - refViewModel.getPlayerPosCol();
                    int newPlayerPositionY = (int) (yPosition / cellHeight) - refViewModel.getPlayerPosRow();
                    refViewModel.changePos(newPlayerPositionY, newPlayerPositionX);
                } catch (Exception e) {   }
            m.consume();//don't want the event to be dispatched to any further event listeners
        }
    }

    private boolean checkZoom(double zoomWidth , double zoomHeight) {
        boolean checkMin = false;
        boolean checkMax = false;
        double maxWidth = scene.getWidth()*0.85 ;
        double maxHeight = scene.getHeight()*0.9;
        double minWidth = scene.getWidth()*0.35 ;
        double minHeight =  scene.getHeight()*0.35;
        if(minWidth < zoomWidth && minHeight < zoomHeight)
            checkMin = true;
        if(maxWidth > zoomWidth && maxHeight > zoomHeight)
            checkMax = true;

        return (checkMax&&checkMin);
    }

    public void zoom(ScrollEvent event) {

        boolean control = event.isControlDown();

        if(control && scene != null) {
            double zoomFactor = 1.15;
            double deltaY = event.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            double zoomWidth = mazeDisplayer.getWidth() * zoomFactor;
            double zoomHeight = mazeDisplayer.getHeight() * zoomFactor;
            if(checkZoom(zoomWidth , zoomHeight))
            {
                mazeDisplayer.setHeight(zoomHeight);
                mazeDisplayer.setWidth(zoomWidth);
                displayMaze();
            }
        }
        event.consume();
    }

    public synchronized void KeyEventHandler(KeyEvent k) {
        synchronized (this) {
            if (refViewModel.getMaze() != null && !refViewModel.playerWin()) {
                try {
                    KeyCode key = k.getCode();
                    if (key.equals(KeyCode.LEFT) || key.equals(KeyCode.NUMPAD4) || key.equals(KeyCode.A)) {
                        refViewModel.changePos(0, -1);
                    } else if (key.equals(KeyCode.DOWN) || key.equals(KeyCode.NUMPAD2) || key.equals(KeyCode.S)) {
                        refViewModel.changePos(1, 0);
                    } else if (key.equals(KeyCode.UP) || key.equals(KeyCode.NUMPAD8) || key.equals(KeyCode.W)) {
                        refViewModel.changePos(-1, 0);
                    } else if (key.equals(KeyCode.RIGHT) || key.equals(KeyCode.NUMPAD6) || key.equals(KeyCode.D)){
                        refViewModel.changePos(0, 1);
                    } else if (key.equals(KeyCode.NUMPAD7) || key.equals(KeyCode.Q)) {
                        refViewModel.changePos(-1, -1);
                    } else if (key.equals(KeyCode.NUMPAD9) || key.equals(KeyCode.E)) {
                        refViewModel.changePos(-1, 1);
                    } else if (key.equals(KeyCode.NUMPAD1) || key.equals(KeyCode.Z)) {
                        refViewModel.changePos(1, -1);
                    } else if (key.equals(KeyCode.NUMPAD3) || key.equals(KeyCode.C)) {
                        refViewModel.changePos(1, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            k.consume();//don't want the event to be dispatched to any further event listeners
        }
    }

    /**
     * Action Event Button Functions
     */

    /**
     * function runs 'about' display
     */
    public void about() {
        try {
            AboutController aboutController = new AboutController();
            aboutController.displayScene();
        } catch (Exception e) {
            lbl_status.setText("Wasn't able to load, please try again.");
        }
    }

    /**
     * function runs 'exit' option
     */
    public void exit() {
        if (didStartMaze) {
            ConfirmController CB = new ConfirmController();
            int ans = CB.confirm();
            if (ans == 1) {
                this.refViewModel.exit();
                primaryStage.close();
            } else if (ans == 2) {
                saveMaze();
            }
        } else {
            ExitController EC = new ExitController();
            int ans = EC.confirm();
            if (ans == 1) {
                this.refViewModel.exit();
                primaryStage.close();
            }
        }
    }

    /**
     * function runs 'control' display
     */
    public void controls() {
        ControlsController controls = new ControlsController(this.refViewModel);
        controls.display();
    }

    /**
     * function runs 'save' option
     */
    public void saveMaze() {
        if (!didStartMaze) {
            showAlert("You must start a maze inorder to save one.");
        }
        else {
            SaveController save = new SaveController(refViewModel);
            save.display();
        }
    }

    /**
     * function runs 'load' option
     */
    public void loadMaze() {
        LoadController load = new LoadController(this.refViewModel);
        load.display();
    }

    /**
     * function runs 'gaveUp' option which displays the solution of the maze
     */
    public void gaveUp() {
        if (didStartMaze)
            refViewModel.solveTheMaze();
        else
            showAlert("You must start a maze first.");

    }

    /**
     * function sets the stage
     * when the stage is closed, we exit properly.
     * @param newStage new stage
     */
    public void setStage(Stage newStage) {
        primaryStage = newStage;
        primaryStage.setOnCloseRequest( event -> {
                    event.consume();
                    exit();
        });
    }

    /**
     * function displays end screen if the user is victorious
     */
    private void setWinner() {
        try {
            startMediaPlayer.stop();
            backGroundMediaPlayer.pause();
            EndScreenController endScreenController = new EndScreenController(refViewModel,this,mazeDisplayer);
            endScreenController.displayScene();
        } catch (Exception e) {
            lbl_status.setText("Wasn't able to load, please try again.");
        }
    }

    /**
     * function resizes and adapts the screen when maximizing or restoring to its default size
     * @param scene scene to resize
     */
    public void setResizeEvent(Scene scene) {
        this.scene = scene;

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(mazeDisplayer.getMaze() != null) { //Added since resizing before generate threw an exception
                mazeDisplayer.setWidth(scene.getWidth() * 0.7);
                displayMaze();
            }
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(mazeDisplayer.getMaze() != null) { //Added since resizing before generate threw an exception
                mazeDisplayer.setHeight(scene.getHeight() * 0.7);
                displayMaze();
            }
        });
    }

    /* Music functions */
    public void playStartGroundSound() {
        startMediaPlayer.setStartTime(Duration.seconds(5));
        startMediaPlayer.setAutoPlay(true);
        startMediaPlayer.setVolume(0.3);
        startMediaPlayer.setOnEndOfMedia(()->playBackGroundSound());
    }

    public void playBackGroundSound() {
        backGroundMediaPlayer.setMute(false);
        if(check_mute == null){
            backGroundMediaPlayer.setAutoPlay(true);
            backGroundMediaPlayer.setVolume(0.1);
            backGroundMediaPlayer.setOnEndOfMedia(()->backGroundMediaPlayer.seek(Duration.seconds(0)));
        }
        else if(!check_mute.isSelected()){
            backGroundMediaPlayer.setAutoPlay(true);
            backGroundMediaPlayer.setVolume(0.1);
            backGroundMediaPlayer.setOnEndOfMedia(()->backGroundMediaPlayer.seek(Duration.seconds(0)));

        }
    }

    /**
     * function in charge of the mute check box
     */
    public void mute(ActionEvent actionEvent){
        if (check_mute.isSelected()) {
            startMediaPlayer.setMute(true);
            backGroundMediaPlayer.setMute(true);
        } else {
            playBackGroundSound();
        }
    }

    /**
     * function shows alerts
     * @param alertMessage alert message
     */
    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

}
