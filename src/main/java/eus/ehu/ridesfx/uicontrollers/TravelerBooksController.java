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
    private Label totBooksCount;

    @FXML
    private TableView<RideBook> rideBooksTbl;

    @FXML
    private TableColumn<RideBook, ?> qc1;

    @FXML
    private TableColumn<RideBook, ?> qc2;

    public TravelerBooksController(BlFacade bl) {
        businessLogic = bl;
    }

    public void updateBooks() {
        rideBooksTbl.getItems().clear();
        rideBooksTbl.getItems().addAll(businessLogic.getTravelerRideBooks());
        totBooksCount.setText("Total books: " + rideBooksTbl.getItems().size());
    }
    void initialize() {
        // update the tableview to display all the books
        updateBooks();
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
