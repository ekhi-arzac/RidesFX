package uicontrollers;

import businessLogic.BlFacade;
import domain.Driver;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ui.MainGUI;

public class LoginController implements Controller {
    private MainGUI mainGUI;
    private final BlFacade businessLogic;

    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;

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

    @FXML
    void onLogin() {
        String email = emailField.getText();
        String name = nameField.getText();
        Driver driver = businessLogic.login(email, name);
        if (driver != null) {
            System.out.println("Login successful");
            businessLogic.setCurrentDriver(driver);
            mainGUI.removeLogRegButton();
            mainGUI.setDriverName(driver.getName());
            mainGUI.showSceneInCenter("queryRides");

        } else {
            System.out.println("Login failed");

        }
    }
}
