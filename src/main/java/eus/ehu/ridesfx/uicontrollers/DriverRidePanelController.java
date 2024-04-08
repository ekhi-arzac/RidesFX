package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class DriverRidePanelController implements Controller {
    private MainGUI mainGUI;
    private BlFacade businessLogic;
    public DriverRidePanelController(BlFacade bussinessLogic) {
        this.businessLogic = bussinessLogic;
    }

    @FXML
    private TableView<Ride> tblRides;
    @FXML
    private Button cancelBtn;


    @FXML
    private TableColumn<Ride, Date> qc1;

    @FXML
    private TableColumn<Ride, String> qc2;

    @FXML
    private TableColumn<Ride, String> qc3;
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
        assert tblRides != null : "fx:id=\"tblRides\" was not injected: check your FXML file 'DriverRidePanel.fxml'.";
        tblRides.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cancelBtn.setDisable(false);
            } else {
                cancelBtn.setDisable(true);
            }
        });

        tblRides.setRowFactory(tv -> new TableRow<Ride>() {
            protected void updateItem(Ride ride, boolean empty) {
                super.updateItem(ride, empty);
                if (ride == null)
                    setStyle("");
                else if (ride.getStatus() == Ride.STATUS.CANCELLED) {
                    setStyle("-fx-background-color: red;");
                    setDisable(true);
                }
                else if (ride.getDate().before(new Date())) {
                    setDisable(true);
                    setStyle("-fx-background-color: lightgrey;");
                }
            }
        });
    }


    @FXML
    void cancelRide() {
        Ride ride = tblRides.getSelectionModel().getSelectedItem();
        if (ride != null) {
            businessLogic.cancelRide(ride);
            updateRides();
        }
    }
}
