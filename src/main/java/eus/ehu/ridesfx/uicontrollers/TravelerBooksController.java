package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.RideBook;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.print.Book;
import java.util.Date;

public class TravelerBooksController implements Controller {
    private MainGUI mainGUI;
    private BlFacade businessLogic;

    @FXML
    private TableView<Ride> tblRides;
    @FXML
    private Button cancelBtn;

    @FXML
    private Button reenableBtn;

    @FXML
    private TableColumn<RideBook, Date> qc1;

    @FXML
    private TableColumn<RideBook, String> qc2;

    @FXML
    private TableColumn<RideBook, String> qc3;
    @FXML
    private Label lblErrorMessage;

    public TravelerBooksController() {
    }

    public void updateBooks() {
        // TODO implement here
    }
    public void init() {
        // TODO implement here
    }
    void initialize() {
        // TODO implement here
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
