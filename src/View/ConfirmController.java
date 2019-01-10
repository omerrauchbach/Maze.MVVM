package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * shows the confirmation box display when:
 * (1) exiting
 * (2) clearing
 *
 * allows to save before confirming action
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class ConfirmController{

    private Stage confirmStage;
    private static int ans; //0 - no, 1 - yes, 2 - save

    public ConfirmController(){
        confirmStage = new Stage();
    }

    public int confirm() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmScreen.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            root.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            confirmStage.setTitle("Confirmation");
            Scene confirmScene = new Scene(root, 249, 102);
            confirmStage.setScene(confirmScene);
            confirmScene.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            confirmStage.initModality(Modality.APPLICATION_MODAL);
        }
        catch(Exception E) {
            new Alert(Alert.AlertType.INFORMATION,"Wasn't able to load, please try again.");
            return 0;
        }
        confirmStage.showAndWait();
        return this.ans;
    }

    public void saveMaze() {
        this.ans = 2;
        confirmStage.close();
    }

    public void yes() {
        this.ans = 1;
        confirmStage.close();
    }

    public void no() {
        this.ans = 0;
        confirmStage.close();
    }
}
