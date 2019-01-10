package View;

import ViewModel.MyViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.io.IOException;

/**
 * shows the controls display
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class ControlsController {

    private Stage controlsStage;
    private MyViewModel viewModel;
    public TableView<ButtonAndAction> tableView;
    public TableColumn<ButtonAndAction, String> ButtonColumn;
    public TableColumn<ButtonAndAction, String> ActionColumn;


    public ControlsController(MyViewModel m) {
        this.viewModel = m;
    }

    public void display() {
        try {
            controlsStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ControlsScreen.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            root.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            controlsStage.setTitle("Controls");
            Scene controlScene = new Scene(root, 546, 500);
            controlsStage.setScene(controlScene);
            controlScene.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
            controlsStage.initModality(Modality.APPLICATION_MODAL);
            ButtonColumn.setCellValueFactory(new PropertyValueFactory<ButtonAndAction, String>("button"));
            ActionColumn.setCellValueFactory(new PropertyValueFactory<ButtonAndAction, String>("action"));
            tableView.setItems(getButtonAndAction());
            //initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
        controlsStage.showAndWait();

    }

    public void done() {
        controlsStage.close();
    }


    public ObservableList<ButtonAndAction> getButtonAndAction()
    {
        ObservableList<ButtonAndAction> rows = FXCollections.observableArrayList();

        rows.add(new ButtonAndAction("Arrows:",""));
        rows.add(new ButtonAndAction("←","Left"));
        rows.add(new ButtonAndAction("→","Right"));
        rows.add(new ButtonAndAction("↑","Up"));
        rows.add(new ButtonAndAction("↓","Down"));
        rows.add(new ButtonAndAction("Letters:",""));
        rows.add(new ButtonAndAction("A","Left"));
        rows.add(new ButtonAndAction("D","Right"));
        rows.add(new ButtonAndAction("W","Up"));
        rows.add(new ButtonAndAction("S","Down"));
        rows.add(new ButtonAndAction("Q","Left and Up"));
        rows.add(new ButtonAndAction("E","Right and Up"));
        rows.add(new ButtonAndAction("Z","Left and Down"));
        rows.add(new ButtonAndAction("C","Right and Down"));
        rows.add(new ButtonAndAction("NumPad:",""));
        rows.add(new ButtonAndAction("NP4","Left"));
        rows.add(new ButtonAndAction("NP6","Right"));
        rows.add(new ButtonAndAction("NP8","Up"));
        rows.add(new ButtonAndAction("NP2","Down"));
        rows.add(new ButtonAndAction("NP7","Left and Up"));
        rows.add(new ButtonAndAction("NP9","Right and Up"));
        rows.add(new ButtonAndAction("NP1","Left and Down"));
        rows.add(new ButtonAndAction("NP3","Right and Down"));
        rows.add(new ButtonAndAction("Mouse:",""));
        rows.add(new ButtonAndAction("Left click","Moves player to your direction"));
        rows.add(new ButtonAndAction("Zoom:",""));
        rows.add(new ButtonAndAction("Ctrl+Scroll","Zooms In/Out"));

        return rows;
    }

}