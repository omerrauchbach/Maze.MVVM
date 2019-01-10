package View;

import javafx.beans.property.SimpleStringProperty;

/**
 * Button and Action class for controls
 *
 * @author Edo Lior & Omer Rauhbach
 * @version 1.0
 * @since 13/6/18
 */

public class ButtonAndAction {

   public SimpleStringProperty button ;
   public SimpleStringProperty action ;

    public ButtonAndAction(String button, String action) {
        this.button = new SimpleStringProperty(button);
        this.action = new SimpleStringProperty(action);
    }

    public String getButton() {
        return button.get();
    }

    public String getAction() {
        return action.get();
    }

}
