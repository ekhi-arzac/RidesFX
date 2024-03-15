package eus.ehu.ridesfx.uicontrollers;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Ride;
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
    private TextField txtArrivalCity;

    @FXML
    private TextField txtDepartCity;

    @FXML
    private TextField txtNumberOfSeats;

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
            if ((txtDepartCity.getText().length() == 0) || (txtArrivalCity.getText().length() == 0) || (txtNumberOfSeats.getText().length() == 0) || (txtPrice.getText().length() == 0))
                return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");
            else {

                // trigger an exception if the introduced string is not a number
                int inputSeats = Integer.parseInt(txtNumberOfSeats.getText());

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
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll(label);
        lblErrorMessage.setText(message);
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

                int inputSeats = Integer.parseInt(txtNumberOfSeats.getText());
                float price = Float.parseFloat(txtPrice.getText());
                Driver driver = businessLogic.getCurrentDriver();
                Ride r = businessLogic.createRide(txtDepartCity.getText(), txtArrivalCity.getText(), Dates.convertToDate(datePicker.getValue()), inputSeats, price, driver.getEmail());
                displayMessage(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideCreated"), "success_msg");


            } catch (RideMustBeLaterThanTodayException e1) {
                displayMessage(e1.getMessage(), "error_msg");
            } catch (RideAlreadyExistException e1) {
                displayMessage(e1.getMessage(), "error_msg");
            }
        }

/*
    if (lblErrorMinBet.getText().length() > 0 && showErrors) {
      lblErrorMinBet.getStyleClass().setAll("lbl", "lbl-error_msg");
    }
    if (lblErrorQuestion.getText().length() > 0 && showErrors) {
      lblErrorQuestion.getStyleClass().setAll("lbl", "lbl-error_msg");
    }
 */
    }

    private List<LocalDate> holidays = new ArrayList<>();

  /*private void setEventsPrePost(int year, int month) {
    LocalDate date = LocalDate.of(year, month, 1);
    setEvents(date.getYear(), date.getMonth().getValue());
    setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
    setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
  }*/

 /* private void setEvents(int year, int month) {

    Date date = Dates.toDate(year, month);

    for (Date day : eus.ehu.ridesfx.businessLogic.getEventsMonth(date)) {
      holidays.add(Dates.convertToLocalDateViaInstant(day));
    }
  }*/

    @FXML
    void initialize() {


        // btnCreateRide.setDisable(true);

        // only show the text of the event in the combobox (without the id)
/*
    Callback<ListView<Event>, ListCell<Event>> factory = lv -> new ListCell<>() {
      @Override
      protected void updateItem(Event item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.getDescription());
      }
    };


     comboEvents.setCellFactory(factory);
    comboEvents.setButtonCell(factory.call(null));

 */


        // setEventsPrePost(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());


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

        // when a date is selected...
        datePicker.setOnAction(actionEvent -> {
     /* comboEvents.getItems().clear();

      oListEvents = FXCollections.observableArrayList(new ArrayList<>());
      oListEvents.setAll(eus.ehu.ridesfx.businessLogic.getEvents(Dates.convertToDate(datePicker.getValue())));

      comboEvents.setItems(oListEvents);

      if (comboEvents.getItems().size() == 0)
        btnCreateRide.setDisable(true);
      else {
         btnCreateRide.setDisable(false);
        // select first option
        comboEvents.getSelectionModel().select(0);
      }
*/
        });

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }


}
