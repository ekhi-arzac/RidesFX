package uicontrollers;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import ui.MainGUI;

public class LoginController implements Controller {
    private MainGUI mainGUI;
    private businessLogic.BlFacade businessLogic;
    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public LoginController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void showRegister() {
        mainGUI.showSceneInCenter("register");
    }
}
