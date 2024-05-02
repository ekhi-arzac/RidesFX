package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.ui.MainGUI;
import jakarta.persistence.Entity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class QueryAlertsController implements Controller {

    private BlFacade businessLogic;
    @FXML
    private Button bookRideButton;

    @FXML
    private Label lblErrorMsg;

    @FXML
    private TableColumn<Alert, String> qc1;

    @FXML
    private TableColumn<Alert, String> qc2;

    @FXML
    private TableColumn<Alert, String> qc3;

    @FXML
    private TableColumn<Alert, Integer> qc4;

    @FXML
    private TableColumn<Alert, String> qc5;

    @FXML
    private TableView<Alert> tblAlerts;
    private MainGUI mainGUI;

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;}

    public QueryAlertsController(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
    public QueryAlertsController(BlFacade bl) {
         this.businessLogic = bl;
    }
}
