package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.RideBook;
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
    private Button cancelBtn;

    @FXML
    private Button reenableBtn;

    @FXML
    private TableView<Ride> tblRides;

    @FXML
    private TableColumn<Ride, Date> qc1;

    @FXML
    private TableColumn<Ride, String> qc2;

    @FXML
    private TableColumn<Ride, String> qc3;

    @FXML
    private TableView<RideBook> tblBooksOfRide;

    @FXML
    private TableColumn<RideBook, Date> qc4;

    @FXML
    private TableColumn<RideBook, String> qc5;

    @FXML
    private TableColumn<RideBook, String> qc6;

    @FXML
    private Button acceptBookBtn;

    @FXML
    private Button rejectBookBtn;

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

    public void updateRideBooks(Ride ride){
        tblBooksOfRide.getItems().clear();
        List<RideBook> rideBooks = businessLogic.getRideBooks(ride);
        for (var rideBook : rideBooks) {
            tblBooksOfRide.getItems().add(rideBook);
        }
        qc4.setCellValueFactory(new PropertyValueFactory<>("travelerName"));
        qc5.setCellValueFactory(new PropertyValueFactory<>("travelerEmail"));
        qc6.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblBooksOfRide.getSortOrder().add(qc4);
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


        acceptBookBtn.setOnAction(event -> {
            RideBook rideBook = tblBooksOfRide.getSelectionModel().getSelectedItem();
            if (rideBook == null) {
                displayMessage("Select a book to accept", "error_msg");
                return;
            }
            if (rideBook.getStatus() == RideBook.STATUS.PENDING) {
                //change the DB
                businessLogic.manageBook(rideBook, RideBook.STATUS.ACCEPTED);
                displayMessage("Book accepted", "success_msg");
                updateRideBooks(tblRides.getSelectionModel().getSelectedItem());
            }
        });

        rejectBookBtn.setOnAction(event -> {
            RideBook rideBook = tblBooksOfRide.getSelectionModel().getSelectedItem();
            if (rideBook == null) {
                displayMessage("Select a book to reject", "error_msg");
                return;
            }
            if (rideBook.getStatus() == RideBook.STATUS.PENDING) {
                //change the DB
                businessLogic.manageBook(rideBook, RideBook.STATUS.CANCELLED);
                displayMessage("Book rejected", "success_msg");
                updateRideBooks(tblRides.getSelectionModel().getSelectedItem());
            }
        });

        //when selecting a ride in the table, show the ride books for that ride in the other table
        tblRides.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateRideBooks(newSelection);
            }
        });
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
    //when selecting a ride in the table, show the ride books for that ride in the other table



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
