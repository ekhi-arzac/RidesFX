package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import eus.ehu.ridesfx.ui.MainGUI;

public class LoginController implements Controller {

    @FXML
    private Label lblErrorMessage;
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
            //this.displayMessage("Login successful", "success_msg");

        } else {
            this.displayMessage("Login failed", "error_msg");
            System.out.println("Login failed");

        }
    }

    private void displayMessage(String label, String message) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll(label);
        lblErrorMessage.setText(message);
    }
}
