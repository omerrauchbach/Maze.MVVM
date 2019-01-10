package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Welcome to the Maze Game.
 * Main class sets observers for the MVVM model and initializes the first opening scene
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class Main extends Application {


    public void start(Stage primaryStage) throws Exception {

        //ViewModel observers Model
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);

        //View observers ViewModel
        MyViewController viewController = new MyViewController(viewModel);
        viewController.playStartGroundSound();
        viewModel.addObserver(viewController);

        //FXML initialization
        FXMLLoader loaderHome = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
        HomeScreenController homeScreenController = new HomeScreenController(viewController,viewModel,primaryStage);
        loaderHome.setController(homeScreenController);
        Parent root = loaderHome.load();
        primaryStage.setTitle("Welcome");
        Scene primaryScene = new Scene(root, 620, 445);
        primaryScene.getStylesheets().add(getClass().getResource("TransitionScreen.css").toExternalForm());
        primaryStage.setScene(primaryScene);
        primaryStage.show();
        primaryScene.getRoot().requestFocus();

    }

}