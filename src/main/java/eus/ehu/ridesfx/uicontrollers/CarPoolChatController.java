package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class CarPoolChatController implements Controller {

    @FXML
    private Label chatName;
    private MainGUI mainGui;
    private BlFacade businessLogic;
    private Ride ride;

    @FXML
    private TextField message;

    @FXML
    private VBox chatMessages;
    private List<Integer> rideNumbers = new ArrayList<>();
    private record Msg(int rideNumber, String sender, String message) {}


    private ConcurrentMap<Integer, List<Msg>> cache = new ConcurrentHashMap<>();
    public CarPoolChatController(BlFacade businessLogic) {
        this.businessLogic = businessLogic;
        for (var ride : businessLogic.getRidesFromDriver(businessLogic.getCurrentUser().getEmail())) {
            rideNumbers.add(ride.getRideNumber());
        }
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGui = mainGUI;
    }

    public void addMessage(String sender, String message, boolean isSelf) {
        Label lbl = new Label(sender + ":");
        lbl.setStyle("-fx-padding: 0 0 5 0; -fx-font-weight: bold;");
        TextField txt = new TextField(message);
        txt.setEditable(false);

        if (isSelf) {
            chatMessages.setAlignment(Pos.CENTER_RIGHT);
            txt.setStyle("-fx-background-color: lightblue; -fx-border-color: #f4f4f4; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-padding: 5px; -fx-text-fill: #000000;");
            lbl.setText("You: ");
        } else {
            chatMessages.setAlignment(Pos.CENTER_LEFT);
            txt.setStyle("-fx-background-color: white; -fx-border-color: #f4f4f4; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-padding: 5px; -fx-text-fill: #000000;");
        }
        txt.setMaxWidth(7 * message.length() +30);



        cache.get(ride.getRideNumber()).add(new Msg(ride.getRideNumber(), sender, message));
        chatMessages.getChildren().addAll(lbl,txt);
    }
    public void setRide(Ride ride) {
        if (this.ride != null && this.ride.getRideNumber() == ride.getRideNumber()){
            return;
        }
        chatMessages.getChildren().clear();
        if (!cache.containsKey(ride.getRideNumber())) {
            cache.put(ride.getRideNumber(), Collections.synchronizedList(new ArrayList<>()));
        } else {
            loadCache();
        }
        chatName.setText( "[#"+ ride.getRideNumber() + "]" + ride.getFromLocation() + " - " + ride.getToLocation() + " (" + ride.getDate()+ ")");
        this.ride = ride;
    }
    public void loadCache() {
        Platform.runLater(() -> {
                    List<Msg> tempMsgList = new ArrayList<>(cache.get(ride.getRideNumber()));
                    tempMsgList.forEach(msg -> {
                        addMessage(msg.sender(), msg.message(), msg.sender().equals(businessLogic.getCurrentUser().getName()));
                    });
        });
    }
    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        businessLogic.sendMessage(ride.getRideNumber(), message.getText());
    }

    @FXML
    void initialize() {
        assert message != null : "fx:id=\"message\" was not injected: check your FXML file 'CarPoolChat.fxml'.";
        assert chatMessages != null : "fx:id=\"chatMessages\" was not injected: check your FXML file 'CarPoolChat.fxml'.";
    }


    public void back(ActionEvent actionEvent) {
        mainGui.showSceneInCenter("dRidePanel");
    }
    public Ride getRide() {
        return this.ride;
    }
}
