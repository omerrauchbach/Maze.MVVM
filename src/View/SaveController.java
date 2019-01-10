package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;


import java.io.IOException;

/**
 * shows the save option display
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class SaveController {

   public TextField filename;
   private MyViewModel viewModel;

   private Stage saveStage;
   private String name;

   public SaveController(MyViewModel viewModel) {
       this.viewModel = viewModel;
   }

   public void display() {

      try {
          saveStage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("SaveScreen.fxml"));
          loader.setController(this);
          Parent root = loader.load();
          root.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
          saveStage.setTitle("Save Maze");
          Scene saveScene = new Scene(root, 350, 150);
          saveStage.setScene(saveScene);
          saveScene.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
          saveStage.initModality(Modality.APPLICATION_MODAL);
      }
      catch (IOException e) {
          e.printStackTrace();
      }
      saveStage.showAndWait();
   }

   public void saveFile() {
       name = filename.getText();
       if(!name.equals("")){
           this.viewModel.saveMaze("./src/resources/Mazes/" + name);
           initString();
           saveStage.close();
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
       saveStage.close();

   }

   private void initString() {
       name="";
   }

}
