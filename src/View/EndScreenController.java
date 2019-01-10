package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

/**
 * shows the end screen display for winning
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class EndScreenController {

    private Stage winnerStage;
    private MazeDisplayer mazeDisplayer;
    private MyViewController viewController;
    private MyViewModel viewModel;
    private Media pauseMusic;
    private MediaPlayer pauseMediaPlayer;
    private Media startMusic;
    private MediaPlayer startMediaPlayer;

    /* Constructor */
    public EndScreenController(MyViewModel vModel, MyViewController vController, MazeDisplayer mazeDisplay) {
        winnerStage = new Stage();
        viewModel = vModel;
        viewController = vController;
        mazeDisplayer = mazeDisplay;
        startMusic = new Media(new File("./src/resources/sounds/winner.mp3").toURI().toString());
        startMediaPlayer = new MediaPlayer(startMusic);
        pauseMusic = new Media(new File("./src/resources/sounds/pause.mp3").toURI().toString());
        pauseMediaPlayer = new MediaPlayer(pauseMusic);
    }

    public void displayScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndScreen.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            root.getStylesheets().add(getClass().getResource("TransitionScreen.css").toExternalForm());
            Scene endScene = new Scene(root,620,370);
            winnerStage.setTitle("Congratulations... You Are The Winner!");
            winnerStage.setScene(endScene);
            endScene.getStylesheets().add(getClass().getResource("TransitionScreen.css").toExternalForm());
            winnerStage.initModality(Modality.APPLICATION_MODAL);
            winnerStage.show();
            playSound();
        }
        catch(Exception E) {
            E.printStackTrace();
        }
    }

    private void playSound() {
        pauseMediaPlayer.setStartTime(Duration.seconds(3.6));
        pauseMediaPlayer.setAutoPlay(true);
        pauseMediaPlayer.setOnEndOfMedia(()->playStartSound());
    }

    private void playStartSound() {
        pauseMediaPlayer.stop();
        startMediaPlayer.setVolume(0.4);
        startMediaPlayer.setAutoPlay(true);
        startMediaPlayer.play();
        startMediaPlayer.setStartTime(Duration.seconds(0));
        startMediaPlayer.setOnEndOfMedia(()->stopSound());
    }

    private void stopSound() {
        startMediaPlayer.stop();
    }

    public void playAgain() {
        mazeDisplayer.clearMaze();
        viewModel.reset();
        winnerStage.close();
    }

    public void credits() {
        AboutController AC = new AboutController();
        AC.displayScene();
    }

}
