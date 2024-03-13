package uicontrollers;


import java.net.URL;
import java.util.ResourceBundle;

import businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import ui.MainGUI;

public class MainGUIController implements Controller {

    @FXML
    private Label selectOptionLbl;

    @FXML
    private Label lblDriver;


    @FXML
    private Button queryRidesBtn;

    @FXML
    private Button createRideBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane mainPane;
    private MainGUI mainGUI;

    private BlFacade businessLogic;

    public MainGUIController(){};

    public MainGUIController(BlFacade blFacade){
        businessLogic = blFacade;
    }

    @FXML
    void queryRides(ActionEvent event) {
        mainPane.setCenter(mainGUI.getQueryRidesLag().getUi());
    }

    @FXML
    void createRide(ActionEvent event) {
        mainPane.setCenter(mainGUI.getCreateRideLag().getUi());
    }

    @FXML
    void identification(ActionEvent event) {
        mainPane.setCenter(mainGUI.getLoginLag().getUi());
    }

    @FXML
    void initialize() {
            // set current driver name
            lblDriver.setText(businessLogic.getCurrentDriver().getName());
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}