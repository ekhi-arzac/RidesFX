package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Button reenableBtn;

    @FXML
    private TableColumn<Ride, Date> qc1;

    @FXML
    private TableColumn<Ride, String> qc2;

    @FXML
    private TableColumn<Ride, String> qc3;
    @FXML
    private Label lblErrorMessage;
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
        tblRides.getSortOrder().add(qc1);

    }

    @FXML
    void initialize() {
        assert tblRides != null : "fx:id=\"tblRides\" was not injected: check your FXML file 'DriverRidePanel.fxml'.";

        // disable arrow key navigation
        tblRides.setRowFactory(tv -> new TableRow<Ride>() {
            protected void updateItem(Ride ride, boolean empty) {
                super.updateItem(ride, empty);
                if (ride == null)
                    setStyle("");
                else if (ride.getDate().before(new Date())) {
                    setStyle("-fx-background-color: lightgrey;");
                }
                else if (ride.getStatus() == Ride.STATUS.CANCELLED) {
                    setStyle("-fx-background-color: red;");

                }
                else if (ride.getStatus() == Ride.STATUS.ACTIVE) {
                    setStyle("-fx-background-color: lightgreen;");
                }


            }
        });
        qc1.setSortType(TableColumn.SortType.DESCENDING);
    }


    @FXML
    void cancelRide() {
        Ride ride = tblRides.getSelectionModel().getSelectedItem();
        if (ride == null) {
            displayMessage("Select a ride to cancel", "error_msg");
            return;
        }

        if (ride.getDate().before(new Date())) {
            displayMessage("Cannot cancel a ride that has already happened", "error_msg");
            return;
        }
        if (ride != null && ride.getStatus() == Ride.STATUS.ACTIVE) {
            businessLogic.cancelRide(ride);
            displayMessage("Ride cancelled", "success_msg");
            updateRides();
        }
    }
    @FXML
    void reenableRide() {
        Ride ride = tblRides.getSelectionModel().getSelectedItem();
        if (ride == null) {
            displayMessage("Select a ride to reenable", "error_msg");
            return;
        }
        if (ride.getDate().before(new Date())) {
            displayMessage("Cannot reenable a ride that has already happened", "error_msg");
            return;
        }
        if (ride.getStatus() == Ride.STATUS.CANCELLED) {
            businessLogic.reenableRide(ride);
            displayMessage("Ride reenabled", "success_msg");
            updateRides();
        }
    }

    public void openChat(ActionEvent actionEvent) {
        Ride ride = tblRides.getSelectionModel().getSelectedItem();
        if (ride == null) {
            displayMessage("Select a ride to open chat", "error_msg");
            return;
        }
        if (ride.getDate().before(new Date())) {
            displayMessage("Cannot open a chat for a completed ride", "error_msg");
            return;
        }
        if (ride != null) {

            mainGUI.showChat(ride);
            businessLogic.getMsgClient().joinChat(ride.getRideNumber(), true);
        }
    }


    public void displayMessage(String message,  String label) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll(label);
        lblErrorMessage.setText(message);
        lblErrorMessage.setVisible(true);
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

}
