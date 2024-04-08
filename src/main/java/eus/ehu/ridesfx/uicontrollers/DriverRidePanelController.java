package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class DriverRidePanelController implements Controller {
    private MainGUI mainGUI;
    @FXML
    private TableView<Ride> tblRides;

    private BlFacade businessLogic;

    @FXML
    private TableColumn<Ride, Date> qc1;

    @FXML
    private TableColumn<Ride, String> qc2;

    @FXML
    private TableColumn<Ride, String> qc3;
    public DriverRidePanelController(BlFacade bussinessLogic) {
        this.businessLogic = bussinessLogic;
    }
    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public void updateRides() {
        tblRides.getItems().clear();
        List<Ride> rides = businessLogic.getRidesFromDriver(businessLogic.getCurrentUser().getEmail());
        for (var ride : rides) {
            tblRides.getItems().add(ride);
        }
        qc1.setCellValueFactory(new PropertyValueFactory<>("date"));
        qc2.setCellValueFactory(new PropertyValueFactory<>("fromLocation"));
        qc3.setCellValueFactory(new PropertyValueFactory<>("toLocation"));
    }

    @FXML
    void initialize() {

    }
}
