package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.RideBook;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;

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
    private TableColumn<RideBook, Integer> qc1;

    @FXML
    private TableColumn<RideBook, String> qc2;

    @FXML
    private TableColumn<RideBook, Integer> qc3;

    @FXML
    private TableColumn<RideBook, String> qc4;

    @FXML
    private TableColumn<RideBook, Date> qc5;

    @FXML
    private TableColumn<RideBook, String> qc6;



    public TravelerBooksController(BlFacade bl) {
        businessLogic = bl;
    }

    public void updateBooks() {
        rideBooksTbl.getItems().clear();
        List<RideBook> books = businessLogic.getTravelerRideBooks();

        for (RideBook book : books){
            System.out.println(">>" + book);
            rideBooksTbl.getItems().add(book);
        }
        ObservableList<RideBook> observableBooks = FXCollections.observableArrayList(books);
        //rideBooksTbl.setItems(observableBooks);
        totBooksCount.setText("" + rideBooksTbl.getItems().size());
    }
    @FXML
    void initialize() {
        qc1.setCellValueFactory(new PropertyValueFactory<RideBook, Integer>("bookId"));
        qc2.setCellValueFactory(new PropertyValueFactory<RideBook, String>("driverName"));
        qc3.setCellValueFactory(new PropertyValueFactory<RideBook, Integer>("passengers"));
        qc4.setCellValueFactory(new PropertyValueFactory<RideBook, String>("status"));
        qc5.setCellValueFactory(new PropertyValueFactory<RideBook, Date>("date"));
        qc6.setCellValueFactory(new PropertyValueFactory<RideBook, String>("contactEmail"));



    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
