package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CarPoolChatController implements Controller {


    private MainGUI mainGui;
    private BlFacade businessLogic;
    private Ride ride;

    @FXML
    private TextField message;

    @FXML
    private VBox chatMessages;
    public CarPoolChatController(BlFacade businessLogic) {
        this.businessLogic = businessLogic;

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGui = mainGUI;
    }

    public void setChatMessages(String message) {
        TextField txt = new TextField(message);
        txt.setEditable(false);
        txt.setPrefWidth(message.length() * 7);
        // place text to right in frame

        txt.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #f4f4f4; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-padding: 5px; -fx-text-fill: #000000;");

        chatMessages.getChildren().add(txt);
    }
    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        System.out.println("Sending message");
        setChatMessages(message.getText());
        businessLogic.sendMessage(ride.getRideNumber(), message.getText());
    }

    @FXML
    void initialize() {
        assert message != null : "fx:id=\"message\" was not injected: check your FXML file 'CarPoolChat.fxml'.";
        assert chatMessages != null : "fx:id=\"chatMessages\" was not injected: check your FXML file 'CarPoolChat.fxml'.";
    }




}
