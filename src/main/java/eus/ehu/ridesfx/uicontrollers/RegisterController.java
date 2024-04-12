package eus.ehu.ridesfx.uicontrollers;
import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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
    private TextField repeatPasswordField;

    @FXML
    private TextField usernameField;

    @FXML
    private ComboBox<String> role;

    /**
     * This method sets verifies the registration once the button is clicked. It will display an error message if the registration fails. There are several failure cases:
     * - The email is already in use
     * - The email is invalid
     * - The username is invalid
     * - The password is invalid
     * - The fields are empty
     * If the registration is successful, the user will be redirected to the queryRides scene.
     */
    @FXML
    void onRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        String username = usernameField.getText();
        String role = this.role.getValue();
        switch (businessLogic.register(email, username, password, repeatPassword, role)) {
            case "success" -> {
                System.out.println("Register successful");
                if (role.equals("TRAVELER")) {
                    businessLogic.setCurrentUser(new Traveler(email, username));
                    mainGUI.hideCreateRide();
                    mainGUI.hideDriverRidePanel();
                    mainGUI.hideBookRide(false);
                } else {
                    businessLogic.setCurrentUser(new Driver(email, username));
                    mainGUI.showCreateRide();
                    mainGUI.showDriverRidePanel();
                }
                mainGUI.removeLogRegButton();
                mainGUI.setUserName(username);
                mainGUI.showUserIcon();
                mainGUI.showSceneInCenter("queryRides");
            }
            case "emptyFields" -> {
                this.displayMessage("All fields are compulsory", "error_msg");
                System.out.println("Empty fields");
            }
            case "invalidEmail" -> {
                this.displayMessage("Invalid email", "error_msg");
                System.out.println("Invalid email");
            }
            case "emailExists" -> {
                this.displayMessage("Email already in use", "error_msg");
                System.out.println("Email already in use");
            }
            case "invalidName" -> {
                this.displayMessage("Username must have less than 20 characters", "error_msg");
                System.out.println("Invalid username");
            }
            case "invalidPassword" -> {
                this.displayMessage("Password must have at least 6 characters", "error_msg");
                System.out.println("Invalid password");
            }
            case "passwordMismatch" -> {
                this.displayMessage("Passwords do not match", "error_msg");
                System.out.println("Passwords do not match");
            }
            case "msgClientError" -> {
                this.displayMessage("Client error", "error_msg");
                System.out.println("Client error");
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

    /**
     * This method displays a message in the label lblErrorMessage
     * @param message the message to be displayed
     * @param label the style of the message
     */
    public void displayMessage(String message,  String label) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll(label);
        lblErrorMessage.setText(message);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                lblErrorMessage.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @FXML
    public void initialize() {
        ObservableList<String> items =  FXCollections.observableArrayList("DRIVER", "TRAVELER");
        role.setItems(items);
    }

}
