package eus.ehu.ridesfx.uicontrollers;
import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class RegisterController implements Controller {

    @FXML
    private Label lblErrorMessage;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void onRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String username = usernameField.getText();
        switch (businessLogic.register(email, username, password)) {
            case "success" -> {
                System.out.println("Register successful");
                businessLogic.setCurrentDriver(new Driver(email, username));
                mainGUI.removeLogRegButton();
                mainGUI.setDriverName(username);
                mainGUI.showSceneInCenter("queryRides");
            }
            case "emailExists" -> {
                this.displayMessage("Email already in use", "error_msg");
                System.out.println("Email already in use");
            }
            case "invalidEmail" -> {
                this.displayMessage("Invalid email", "error_msg");
                System.out.println("Invalid email");
            }
            case "invalidName" -> {
                this.displayMessage("Username must have less than 10 characters", "error_msg");
                System.out.println("Invalid username");
            }
            case "invalidPassword" -> {
                this.displayMessage("Password must have at least 6 characters", "error_msg");
                System.out.println("Invalid password");
            }
            case "emptyFields" -> {
                this.displayMessage("All fields are compulsory", "error_msg");
                System.out.println("Empty fields");
            }
        }
    }

    @FXML
    void showLogin() {
        mainGUI.showSceneInCenter("login");
    }
    private MainGUI mainGUI;
    private eus.ehu.ridesfx.businessLogic.BlFacade businessLogic;

    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public RegisterController(BlFacade bl) {
        businessLogic = bl;
    }

    public void displayMessage(String message,  String label) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll(label);
        lblErrorMessage.setText(message);
    }

}
