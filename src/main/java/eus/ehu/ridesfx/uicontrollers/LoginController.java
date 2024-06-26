package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.configuration.Config;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import eus.ehu.ridesfx.ui.MainGUI;

public class LoginController implements Controller {


    private MainGUI mainGUI;
    private final BlFacade businessLogic;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Label lblErrorMessage;

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

    /**
     * This method sets verifies the login once the button is clicked.
     * It will display an error message if the login fails. The error case is:
     * - The email is not registered yet
     * If the login is successful, the user will be redirected to the queryRides scene.
     */
    @FXML
    void onLogin() {
        String email = emailField.getText();
        String name = passwordField.getText();
        User user = businessLogic.login(email, name);
        if (user != null) {
            System.out.println("Login successful");
            businessLogic.setCurrentUser(user);
            mainGUI.removeLogRegButton();
            mainGUI.setUserName(user.getName());
            mainGUI.showSceneInCenter("queryRides");
            mainGUI.showUserIcon();
            mainGUI.showLogoutButton();
            //this.displayMessage("Login successful", "success_msg");
            if (businessLogic.getCurrentUser() instanceof Driver) {
                mainGUI.showCreateRide();
                mainGUI.showDriverRidePanel();
                mainGUI.hideBookRide(true);
                mainGUI.hideQueryRides();
                mainGUI.showSceneInCenter("dRidePanel");
            } else if (businessLogic.getCurrentUser() instanceof Traveler) {
                mainGUI.hideCreateRide();
                mainGUI.hideDriverRidePanel();
                mainGUI.hideBookRide(false);
                mainGUI.showViewBooksBtn(true);
                mainGUI.showQueryAlerts();
            }

        } else {
            lblErrorMessage.setVisible(true);
            this.displayMessage("This email is not registered", "error_msg");
            System.out.println("Login failed");

        }
    }
  
    /**
     * This method displays a message in the label lblErrorMessage
     * @param message the message to be displayed
     * @param label the style of the message
     */
    private void displayMessage(String message, String label) {
        lblErrorMessage.setVisible(true);
        lblErrorMessage.setText(message);
        lblErrorMessage.getStyleClass().add(label);
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

    public void clearFields() {
        emailField.clear();
        passwordField.clear();
    }
    @FXML
    void initialize() {
        Config config = Config.getInstance();
        emailField.setText(config.getMail());
        passwordField.setText(config.getPassword());


    }
}
