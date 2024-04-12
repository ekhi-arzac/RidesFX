package eus.ehu.ridesfx.uicontrollers;


import java.net.URL;
import java.util.ResourceBundle;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.scene.image.ImageView;

public class MainGUIController implements Controller {

    @FXML
    private Label selectOptionLbl;

    @FXML
    private Label lblUser;


    @FXML
    private Button queryRidesBtn;

    @FXML
    private Button createRideBtn;

    @FXML
    private Button logregbtn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane mainPane;

    @FXML
    private ImageView user_icon;

    @FXML
    private Button dRidePanelBtn;

    private MainGUI mainGUI;

    private BlFacade businessLogic;

    public MainGUIController(){};

    public MainGUIController(BlFacade blFacade){
        businessLogic = blFacade;
    }

    /**
     * This method sets the mainGUI to the mainGUIController
     * @param event
     */
    @FXML
    void showQueryRides(ActionEvent event) {
        mainGUI.showSceneInCenter("queryRides");
    }
    /**
     * This method sets the mainGUI to the mainGUIController
     * @param event
     */
    @FXML
    void showCreateRide(ActionEvent event) {
        mainGUI.showSceneInCenter("createRide");
    }
    /**
     * This method sets the mainGUI to the mainGUIController
     * @param event
     */
    @FXML
    void showLogin(ActionEvent event) {
        mainGUI.showSceneInCenter("login");
    }

    @FXML
    void showDRidePanel(ActionEvent event) {
        mainGUI.showSceneInCenter("dRidePanel");
    }

    @FXML
    void initialize() {
            // set current driver name
            lblUser.setText(businessLogic.getCurrentUser().getName());
            user_icon.setVisible(false);
            if (businessLogic.getCurrentUser() instanceof eus.ehu.ridesfx.domain.Guest) {
                createRideBtn.setVisible(false);
                dRidePanelBtn.setVisible(false);
            }
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        mainGUI.setMainPane(mainPane);
    }
    /**
     * This method removes the logregbtn from the mainGUI once the user has logged in
     */
    public void removeLogRegButton() {
        logregbtn.setVisible(false);
    }
    /**
     * This method sets the driver name to the label lblDriver to show once the user has logged in
     * @param name the name of the driver
     */
    public void setUserName(String name) {
        lblUser.setText(name);
    }

    public void showUserIcon() {
        user_icon.setVisible(true);
    }

    public void hideQueryRides() {
        queryRidesBtn.setVisible(false);
    }

    public void showCreateRideBtn() {
        createRideBtn.setVisible(true);
    }

    public void showDriverRidePanel() {
        dRidePanelBtn.setVisible(true);
    }

    public void hideCreateRideBtn() {
        createRideBtn.setVisible(false);
    }
    public void hideDriverRidePanel() {
        dRidePanelBtn.setVisible(false);
    }
}
