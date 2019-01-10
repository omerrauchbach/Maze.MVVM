package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * shows the home screen display
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class HomeScreenController {

    private Button btn_play;
    private Button btn_about;
    public MyViewController viewController;
    public MyViewModel viewModel;
    public Stage viewControllerStage = new Stage();
    public Stage primaryStage;

    public HomeScreenController(MyViewController viewC, MyViewModel viewM, Stage primStage) {
        viewController = viewC;
        viewModel = viewM;
        primaryStage = primStage;

        //Exits home screen window properly
        primaryStage.setOnCloseRequest( event -> {
            event.consume();
            ExitController EC = new ExitController();
            int ans = EC.confirm();
            if (ans == 1) {
                primaryStage.close();
                viewM.exit();
            }
        });
    }

    public void play() throws IOException {
        //FXML initialization
        FXMLLoader loaderMyView = new FXMLLoader(getClass().getResource("MyView.fxml"));
        loaderMyView.setController(viewController);
        Parent root = loaderMyView.load();
        viewController.setStatus();

        //Stage initialization
        viewControllerStage.setTitle("Get Peter To The Donut Game");
        Scene viewScene = new Scene(root, 677, 452);
        viewScene.getStylesheets().add(getClass().getResource("MyView.css").toExternalForm());
        viewControllerStage.setScene(viewScene);
        viewController.setStage(viewControllerStage);
        viewController.setResizeEvent(viewScene);
        viewControllerStage.show();
        viewScene.getRoot().requestFocus();
        primaryStage.close();

    }

    public void about() {
        AboutController aboutController = new AboutController();
        aboutController.displayScene();
    }

}
