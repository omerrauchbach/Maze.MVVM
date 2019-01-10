package View;


import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;


import java.io.IOException;

/**
 * shows the load option display
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class LoadController {

   private Stage loadStage;
   private String name;
   private MyViewModel viewModel ;
   public TextField filename;
   public Button btn_load;
   public Button btn_cancel;


   public LoadController(MyViewModel m){
       this.viewModel = m;
   }

   public void display() {
       try {
           loadStage = new Stage();
           FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadScreen.fxml"));
           loader.setController(this);
           Parent root = loader.load();
           root.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
           loadStage.setTitle("Load Maze");
           Scene loadScene = new Scene(root, 354, 205);
           loadStage.setScene(loadScene);
           loadScene.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
           loadStage.initModality(Modality.APPLICATION_MODAL);
       }
       catch (IOException e){
           e.printStackTrace();
       }
       loadStage.showAndWait();

   }

   public void loadMaze(){
       name = filename.getText();
       if (!name.equals("")){
           if (this.viewModel.loadMaze("./src/resources/Mazes/" + name)) {
           initString();
           }
           else {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setContentText(String.format("invalid file name"));
               alert.show();
           }
       }
       else {
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setContentText(String.format("please enter a file name"));
           alert.show();

       }
       cancel();
   }

   public void cancel() {
       initString();
       loadStage.close();
   }

    private void initString() {
        name="";
    }

}
