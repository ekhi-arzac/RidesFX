package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
    @FXML
    private ScrollPane scrollPane;
    @FXML

    private List<Integer> rideNumbers = new ArrayList<>();
    private record Msg(int rideNumber, String sender, String message, boolean sys) {}


    private ConcurrentMap<Integer, List<Msg>> cache = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, ConcurrentLinkedQueue<User>> onlineUsers = new ConcurrentHashMap<>();
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

    public void addMessage(String sender, String message, boolean isSelf, boolean sysMsg) {
        Label lbl = new Label(sender + ":");
        lbl.setStyle("-fx-padding: 0 0 5 0; -fx-font-weight: bold;");
        Label txt = new Label(message);
        HBox hBox = new HBox();


        if (isSelf) {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            txt.setStyle("-fx-background-color: lightblue; -fx-border-color: #f4f4f4; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-padding: 5px; -fx-text-fill: #000000;");
            lbl.setText("You: ");
        } else if (!sysMsg) {
            hBox.setAlignment(Pos.CENTER_LEFT);
            txt.setStyle("-fx-background-color: white; -fx-border-color: #f4f4f4; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-padding: 5px; -fx-text-fill: #000000;");
        } else {
            hBox.setAlignment(Pos.CENTER);
            txt.setStyle("-fx-padding: 5px; -fx-text-fill: #000000; -fx-underline: true; -fx-font-weight: bold;");
        }
        int messageLength = message.split(" ").length;
        txt.setWrapText(true);
        txt.setMaxWidth(300);
        txt.setMinHeight(Region.USE_PREF_SIZE);
        hBox.getChildren().addAll(lbl, txt);
        chatMessages.getChildren().add(hBox);
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
        chatName.setText( "[#"+ ride.getRideNumber() + "] " + ride.getFromLocation() + " - " + ride.getToLocation() + " (" + ride.getDate()+ ")");
        this.ride = ride;
    }
    public void loadCache() {
        Platform.runLater(() -> {
                    List<Msg> tempMsgList = new ArrayList<>(cache.get(ride.getRideNumber()));
                    tempMsgList.forEach(msg -> {
                        addMessage(msg.sender(), msg.message(), msg.sender().equals(businessLogic.getCurrentUser().getName()), msg.sys());
                    });
        });
    }
    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        if (ride == null || message.getText().isEmpty()) {
            return;
        }
        businessLogic.sendMessage(ride.getRideNumber(), message.getText());
        message.clear();
    }

    @FXML
    void initialize() {
        assert message != null : "fx:id=\"message\" was not injected: check your FXML file 'CarPoolChat.fxml'.";
        assert chatMessages != null : "fx:id=\"chatMessages\" was not injected: check your FXML file 'CarPoolChat.fxml'.";
        message.setPromptText("Send a message");
        message.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                sendMessage(null);
            }
        } );

        scrollPane.vvalueProperty().bind(chatMessages.heightProperty());

    }


    public void back(ActionEvent actionEvent) {
        businessLogic.getMsgClient().joinChat(ride.getRideNumber(), false);
        mainGui.showSceneInCenter("dRidePanel");
    }
    public Ride getRide() {
        return this.ride;
    }

}
