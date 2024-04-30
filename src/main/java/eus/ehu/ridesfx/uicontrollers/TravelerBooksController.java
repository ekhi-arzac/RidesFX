package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.RideBook;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.print.Book;
import java.util.Date;

public class TravelerBooksController implements Controller {
    private MainGUI mainGUI;
    private BlFacade businessLogic;

    @FXML
    private Button bookRideButton;

    @FXML
    private ComboBox<?> comboArrivalCity;

    @FXML
    private ComboBox<?> comboDepartCity;

    @FXML
    private DatePicker datepicker;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private ComboBox<?> numOfPassenger;

    @FXML
    private Label passengersLbl;

    @FXML
    private TableColumn<?, ?> qc1;

    @FXML
    private TableColumn<?, ?> qc2;

    @FXML
    private TableColumn<?, ?> qc3;

    @FXML
    private TableView<?> tblRides;

    public TravelerBooksController(BlFacade bl) {
        businessLogic = bl;
    }

    public void updateBooks() {
        // TODO implement here
    }
    public void init() {
        // TODO implement here
    }
    void initialize() {

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
