package uicontrollers;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import ui.MainGUI;

public class MainGUIController implements Controller{

    @FXML
    private Label selectOptionLbl;

    @FXML
    private Label lblDriver;


    @FXML
    private Button queryRidesBtn;

    @FXML
    private Button createRideBtn;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private MainGUI mainGUI;

    private BlFacade businessLogic;

    public MainGUIController(){};

    public MainGUIController(BlFacade blFacade){
        businessLogic = blFacade;
    }

    @FXML
    void queryRides(ActionEvent event) {
        mainGUI.showQueryRides();
    }

    @FXML
    void createRide(ActionEvent event) {
        mainGUI.showCreateRide();
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
