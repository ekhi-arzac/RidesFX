package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
public class CarPoolChatController implements Controller {

    @FXML
    private Label chatName;
    @FXML
    private ListView<String> onlineUsersTable;
    private MainGUI mainGui;
    private BlFacade businessLogic;
    private Ride ride;

    @FXML
    private TextField message;

    @FXML
    private VBox chatMessages;
    @FXML
    private ScrollPane scrollPane;

    private List<Integer> rideNumbers = new ArrayList<>();

    public void addOnline(String ride, String user) {
        Platform.runLater(() -> {
            int rideNum = Integer.parseInt(ride);
            if (!onlineUsers.containsKey(rideNum)) {
                ObservableList<String> users = FXCollections.observableArrayList();
                users.addListener((ListChangeListener<String>) c -> onlineUsersTable.setItems(users));
                onlineUsers.put(rideNum, users);
            }
            onlineUsers.get(rideNum).add(user);
        });

    }

    public void removeOnline(String ride, String user) {
        Platform.runLater(() -> {
            int rideNum = Integer.parseInt(ride);
            onlineUsers.getOrDefault(rideNum, FXCollections.emptyObservableList()).remove(user);
        });
    }

    private record Msg(int rideNumber, String sender, String message, boolean sys) {}
    private class Message {
        String sender;
        String content;
    }

    private final ConcurrentMap<Integer, List<Msg>> cache = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, ObservableList<String>> onlineUsers = new ConcurrentHashMap<>();
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
        txt.setWrapText(true);
        txt.setMaxWidth(300);
        txt.setMinHeight(Region.USE_PREF_SIZE);
        hBox.getChildren().addAll(lbl, txt);
        chatMessages.getChildren().add(hBox);
    }
    public void setRide(Ride ride) {
        if (this.ride != null && Objects.equals(this.ride.getRideNumber(), ride.getRideNumber())){
            return;
        }
        chatMessages.getChildren().clear();

        if (!cache.containsKey(ride.getRideNumber())) {
            cache.put(ride.getRideNumber(), Collections.synchronizedList(new ArrayList<>()));
            String jsonData = businessLogic.getMsgClient().getChat(ride.getRideNumber());

            if (jsonData != null) {

                Gson gson = new Gson();
                List<Message> messages = List.of(gson.fromJson(jsonData, Message[].class));
                for (var msg : messages) {
                    cache.get(ride.getRideNumber()).add(new Msg(ride.getRideNumber(), msg.sender, msg.content, msg.sender.equals("sys")));
                }
            }
        }

        loadCache();
        onlineUsersTable.setItems(onlineUsers.get(ride.getRideNumber()));
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

    @FXML
    public void back(ActionEvent actionEvent) {
        businessLogic.getMsgClient().joinChat(ride.getRideNumber(), false);
        mainGui.showSceneInCenter("dRidePanel");
    }
    public Ride getRide() {
        return this.ride;
    }

    public void clearCache() {
        cache.clear();
        onlineUsers.clear();
    }

}
