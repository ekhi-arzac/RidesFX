package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.time.*;
import java.util.*;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.Dates;

public class QueryRidesController implements Controller {

    @FXML
    private Button createAlertBtn;

    @FXML
    private Button bookRideButton;

    @FXML
    private ComboBox<Integer> numOfPassenger;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnClose;

    @FXML
    private DatePicker datepicker;

    @FXML
    private TableColumn<Ride, String> qc1;

    @FXML
    private TableColumn<Ride, Integer> qc2;

    @FXML
    private Label passengersLbl;

    @FXML
    private TableColumn<Ride, Float> qc3;

    @FXML
    private ComboBox<String> comboArrivalCity;

    @FXML
    private ComboBox<String> comboDepartCity;

    //@FXML
    //private TableView<Event> tblEvents;

    @FXML
    private TableView<Ride> tblRides;


    private MainGUI mainGUI;

    private List<LocalDate> datesWithBooking = new ArrayList<>();

    private BlFacade businessLogic;

    public QueryRidesController(BlFacade bl) {
        businessLogic = bl;
    }


    @FXML
    void closeClick(ActionEvent event) {
        mainGUI.showMain();
    }

    private void setEvents(int year, int month) {
        Date date = Dates.toDate(year, month);

        for (Date day : businessLogic.getEventsMonth(date)) {
            datesWithBooking.add(Dates.convertToLocalDateViaInstant(day));
        }
    }

    // we need to mark (highlight in pink) the events for the previous, current and next month
    // this method will be called when the user clicks on the << or >> buttons
    private void setEventsPrePost(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        setEvents(date.getYear(), date.getMonth().getValue());
        setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
        setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
    }


    private void updateDatePickerCellFactory(DatePicker datePicker) {

        List<Date> dates = businessLogic.getDatesWithRides(comboDepartCity.getValue(), comboArrivalCity.getValue());

        // extract datesWithBooking from rides
        datesWithBooking.clear();
        for (Date day : dates) {
            datesWithBooking.add(Dates.convertToLocalDateViaInstant(day));
        }

        datePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if (datesWithBooking.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            } else {
                                this.setStyle("-fx-background-color: rgb(235, 235, 235)");
                            }
                        }
                    }
                };
            }
        });
    }
    //the initialize method is called when the page is loaded
    @FXML
    void initialize() {

        //initializes the error message label
        lblErrorMessage.setVisible(false);



        //change the elements depending of the user when initializing the page
        User user = businessLogic.getCurrentUser();
        if (user instanceof Traveler) {
            bookRideButton.setVisible(true);
            numOfPassenger.setVisible(true);
            passengersLbl.setVisible(true);
        } else if (user instanceof Driver) {
            bookRideButton.setVisible(false);
            numOfPassenger.setVisible(false);
            passengersLbl.setVisible(false);
        }
        // log instance of user
        System.out.println(user instanceof Traveler);
        createAlertBtn.setVisible(false);

        // Update DatePicker cells when ComboBox value changes
        comboArrivalCity.valueProperty().addListener(
                (obs, oldVal, newVal) -> updateDatePickerCellFactory(datepicker));

        ObservableList<String> departureCities = FXCollections.observableArrayList(new ArrayList<>());
        departureCities.setAll(businessLogic.getDepartCities());

        ObservableList<String> arrivalCities = FXCollections.observableArrayList(new ArrayList<>());

        comboDepartCity.setItems(departureCities);
        comboArrivalCity.setItems(arrivalCities);

        // when the user selects a departure city, update the arrival cities
        comboDepartCity.setOnAction(e -> {
            arrivalCities.clear();
            arrivalCities.setAll(businessLogic.getDestinationCities(comboDepartCity.getValue()));
            comboArrivalCity.setItems(arrivalCities);
        });

        tblRides.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->

        {
            numOfPassenger.getItems().clear();
            // get the number of passengers from selected ride of the table
            Ride selectedRide = tblRides.getSelectionModel().getSelectedItem();

            if (selectedRide != null) {
                numOfPassenger.setDisable(false);
                int numSeats = selectedRide.getNumPlaces();
                for (int i= 0; i < numSeats; i++) {
                    numOfPassenger.getItems().add(i+1);
                }
            }
            else{
                numOfPassenger.setDisable(true);
            }

        });


        // a date has been chosen, update the combobox of Rides
        datepicker.setOnAction(actionEvent -> {
            tblRides.getItems().clear();
            // Vector<eus.ehu.ridesfx.domain.Ride> events = eus.ehu.ridesfx.businessLogic.getEvents(Dates.convertToDate(datepicker.getValue()));
            List<Ride> rides = businessLogic.getRides(comboDepartCity.getValue(), comboArrivalCity.getValue(), Dates.convertToDate(datepicker.getValue()));
            // List<Ride> rides = Arrays.asList(new Ride("Bilbao", "Donostia", Dates.convertToDate(datepicker.getValue()), 3, 3.5f, new Driver("pepe@pepe.com", "pepe")));
            if (rides.isEmpty() && businessLogic.getCurrentUser() instanceof Traveler) {
                createAlertBtn.setVisible(true);
                numOfPassenger.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
            } else {
                createAlertBtn.setVisible(false);
            }
            for (Ride ride : rides) {
                tblRides.getItems().add(ride);
            }
        });

        // if the user is a traveler, he has the option to book an available ride, the ride will be set as pending
        bookRideButton.setOnAction(actionEvent -> {
            if (businessLogic.getCurrentUser() instanceof Traveler) {
                Ride ride = tblRides.getSelectionModel().getSelectedItem();
                if (ride != null) {
                    Integer passengers = numOfPassenger.getValue();
                    //correct  number of passengers
                    if (passengers != null && passengers > 0 && passengers <= ride.getNumPlaces()) {
                        //date is later than today
                        if (datepicker.getValue().isAfter(LocalDate.now())) {
                            businessLogic.bookRide(ride, Dates.convertToDate(datepicker.getValue()), passengers, (Traveler)businessLogic.getCurrentUser());
                            displayMessage("Ride booked successfully", "success_msg");
                        } else {
                            displayMessage("Please select a valid date", "error_msg");
                            System.out.println(">>Please select a valid date");
                        }
                    }
                    else{
                        displayMessage("Please select a valid number of passengers", "error_msg");
                        System.out.println(">>Please select a valid number of passengers");
                    }
                }
                else{
                    displayMessage("Please select a ride", "error_msg");
                    System.out.println(">>Please select a ride");
                }
            }
            else{
                displayMessage("User is not a traveler", "error_msg");
                System.out.println(">>user is not a traveler");

                System.out.println("user is not a traveler");
            }

        });

        datepicker.setOnMouseClicked(e -> {
            //.
            // get a reference to datepicker inner content
            // attach a listener to the  << and >> buttons
            // mark events for the (prev, current, next) month and year shown
            DatePickerSkin skin = (DatePickerSkin) datepicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {


                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();

                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);

                    // print month value
                    System.out.println("Month:" + ym.getMonthValue());

                });
            });
        });

        // show just the driver's name in column1
        qc1.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ride, String> data) {
                Driver driver = data.getValue().getDriver();
                return new SimpleStringProperty(driver != null ? driver.getName() : "<no name>");
            }
        });

        qc2.setCellValueFactory(new PropertyValueFactory<>("numPlaces"));
        qc3.setCellValueFactory(new PropertyValueFactory<>("price"));

    }


/*

  private void setupEventSelection() {
    tblEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {

        tblQuestions.getItems().clear();
        for (Question q : tblEvents.getSelectionModel().getSelectedItem().getQuestions()) {
          tblQuestions.getItems().add(q);
        }
      }
    });
  }
*/

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
    //hides the book ride options or not
    public void hideBookRide(boolean hide) {
        if (hide) {
            bookRideButton.setVisible(false);
            passengersLbl.setVisible(false);
            numOfPassenger.setVisible(false);
        } else {
            bookRideButton.setVisible(true);
            passengersLbl.setVisible(true);
            numOfPassenger.setVisible(true);
        }
    }
    private void displayMessage(String message, String label) {

        RegisterController.displayMsg(message, label, lblErrorMessage);
    }


    @FXML
    void onCreateAlert(ActionEvent event) {
        //we want to store the alert into the database
        try {
            int passengers = numOfPassenger.getValue();
            businessLogic.createAlert((Traveler)businessLogic.getCurrentUser(), comboDepartCity.getValue(), comboArrivalCity.getValue(), Dates.convertToDate(datepicker.getValue()), passengers);
            displayMessage("Alert created successfully", "success_msg");
        } catch (Exception e) {
            displayMessage("Please select every field", "error_msg");
        }



    }
}
