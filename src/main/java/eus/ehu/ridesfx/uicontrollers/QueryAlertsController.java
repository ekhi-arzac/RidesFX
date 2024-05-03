package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Alert;
import eus.ehu.ridesfx.domain.RideBook;
import eus.ehu.ridesfx.ui.MainGUI;
import jakarta.persistence.Entity;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;


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
    private TableColumn<Alert, Date> qc3;

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

    @FXML
    public void initialize() {
        qc1.setCellValueFactory(new PropertyValueFactory<Alert, String>("fromLocation"));
        qc2.setCellValueFactory(new PropertyValueFactory<Alert, String>("toLocation"));
        qc3.setCellValueFactory(new PropertyValueFactory<Alert, Date>("date"));
        qc4.setCellValueFactory(new PropertyValueFactory<Alert, Integer>("numPlaces"));
        qc5.setCellValueFactory(new PropertyValueFactory<Alert, String>("status"));
        List<Alert> alerts = businessLogic.getAlerts();
        if (alerts != null) {
            tblAlerts.getItems().setAll(alerts);
        } else {
            lblErrorMsg.setText("No alerts found");
        }
    }

    public void updateAlerts() {
        List<Alert> alerts = businessLogic.getAlerts();
        if (alerts != null) {
            tblAlerts.getItems().setAll(alerts);
        } else {
            lblErrorMsg.setText("No alerts found");
        }
    }
}
