package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.Dates;

public class CreateRideController implements Controller {

    public CreateRideController(BlFacade bl) {
        this.businessLogic = bl;
    }

    private BlFacade businessLogic;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker datePicker;

    private MainGUI mainGUI;


    @FXML
    private Label lblErrorMessage;

    @FXML
    private Label lblErrorMinBet;


    @FXML
    private Button btnCreateRide;

    @FXML
    private ComboBox<String> departBox;

    @FXML
    private ComboBox<String> arrivalBox;

    @FXML
    private Spinner<Integer> nSeatsSpinner;

    @FXML
    private TextField txtPrice;


    @FXML
    void closeClick(ActionEvent event) {
        clearErrorLabels();
        mainGUI.showMain();
    }

    private void clearErrorLabels() {
        lblErrorMessage.setText("");
        lblErrorMinBet.setText("");
        lblErrorMinBet.getStyleClass().clear();
        lblErrorMessage.getStyleClass().clear();
    }


    private String field_Errors() {

        try {

            if ((departBox.getSelectionModel().isEmpty()) || arrivalBox.getSelectionModel().isEmpty() || (datePicker.getValue() == null
                    || nSeatsSpinner.getValue() == 0 || (txtPrice.getText().isEmpty())))
                return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");
            else {

                // trigger an exception if the introduced string is not a number
                int inputSeats = nSeatsSpinner.getValue();

                if (inputSeats <= 0) {
                    return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.SeatsMustBeGreaterThan0");
                } else {
                    float price = Float.parseFloat(txtPrice.getText());
                    if (price <= 0)
                        return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.PriceMustBeGreaterThan0");

                    else
                        return null;

                }
            }
        } catch (java.lang.NumberFormatException e1) {

            return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorNumber");
        } catch (Exception e1) {

            e1.printStackTrace();
            return null;

        }
    }

    void displayMessage(String message, String label){
        lblErrorMessage.setVisible(true);
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll(label);
        lblErrorMessage.setText(message);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                lblErrorMessage.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @FXML
    void createRideClick(ActionEvent e) {

        clearErrorLabels();

        //  Event event = comboEvents.getSelectionModel().getSelectedItem();
        String errors = field_Errors();

        if (errors != null) {
            // eus.ehu.ridesfx.businessLogic.createQuestion(event, inputQuestion, inputPrice);
            displayMessage(errors, "error_msg");

        } else {
            try {

                int inputSeats = Integer.parseInt(nSeatsSpinner.getValue().toString());

                float price = Float.parseFloat(txtPrice.getText());
                User user = businessLogic.getCurrentUser();
                Ride r = businessLogic.createRide(departBox.getValue(), arrivalBox.getValue(), Dates.convertToDate(datePicker.getValue()), inputSeats, price, user.getEmail());
                displayMessage(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideCreated"), "success_msg");


            } catch (RideMustBeLaterThanTodayException e1) {
                displayMessage(e1.getMessage(), "error_msg");
            } catch (RideAlreadyExistException e1) {
                displayMessage(e1.getMessage(), "error_msg");
            }
        }

    }

    private List<LocalDate> holidays = new ArrayList<>();



    @FXML
    void initialize() {

        // get a reference to datepicker inner content
        // attach a listener to the  << and >> buttons
        // mark events for the (prev, current, next) month and year shown
        datePicker.setOnMouseClicked(e -> {
            DatePickerSkin skin = (DatePickerSkin) datePicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {
                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();
                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);
                    // setEventsPrePost(ym.getYear(), ym.getMonthValue());
                });
            });


        });
        departBox.getItems().addAll(businessLogic.getDepartCities());

        departBox.setOnAction(e -> {
            arrivalBox.getItems().clear();
            arrivalBox.getItems().addAll(businessLogic.getDestinationCities(departBox.getValue()));
        });
        // nseats spinner value between 1 and max of inf
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        nSeatsSpinner.setValueFactory(valueFactory);

        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if (holidays.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            }
                        }
                    }
                };
            }
        });


    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }


}
