package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * shows the confirmation box display when exiting:
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class ExitController{

    private Stage exitStage;
    private static int ans; //0 - no, 1 - yes

    public ExitController(){
        exitStage = new Stage();
    }

    public int confirm() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExitScreen.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            root.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            exitStage.setTitle("Confirmation");
            Scene confirmScene = new Scene(root, 249, 102);
            exitStage.setScene(confirmScene);
            confirmScene.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            exitStage.initModality(Modality.APPLICATION_MODAL);
        }
        catch(Exception E) {
            new Alert(Alert.AlertType.INFORMATION,"Wasn't able to load, please try again.");
            return 0;
        }
        exitStage.showAndWait();
        return this.ans;
    }

    public void yes() {
        this.ans = 1;
        exitStage.close();
    }

    public void no() {
        this.ans = 0;
        exitStage.close();
    }
}
