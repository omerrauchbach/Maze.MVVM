package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * shows the about display
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class AboutController {

    private Stage aboutStage;

    public AboutController(){
        aboutStage = new Stage();
    }

    public void displayScene() {
        try {
            FXMLLoader fxmlCredits = new FXMLLoader(getClass().getResource("AboutScreen.fxml"));
            fxmlCredits.setController(this);
            Parent root = fxmlCredits.load();
            root.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            Scene aboutScene = new Scene(root,563,251);
            aboutStage.setTitle("About");
            aboutStage.setScene(aboutScene);
            aboutScene.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            aboutStage.initModality(Modality.APPLICATION_MODAL);
            aboutStage.show();
        }
        catch(Exception E){
            E.printStackTrace();
        }
    }

    public void done() {
        aboutStage.close();
    }

}
