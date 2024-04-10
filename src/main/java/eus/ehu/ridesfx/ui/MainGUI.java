package eus.ehu.ridesfx.ui;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.uicontrollers.DriverRidePanelController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import eus.ehu.ridesfx.uicontrollers.Controller;
import eus.ehu.ridesfx.uicontrollers.MainGUIController;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainGUI {

    private Window mainLag, createRideLag, queryRidesLag, loginLag, registerLag, dRidePanelLag;

    private BlFacade businessLogic;
    private Stage stage;
    private Scene scene;
    @FXML
    private BorderPane mainPane;

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    public void setBusinessLogic(BlFacade afi) {
        businessLogic = afi;
    }

    public MainGUI(BlFacade bl) {
        Platform.startup(() -> {
            try {
                setBusinessLogic(bl);
                init(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void hideQueryRides() {
        ((MainGUIController) mainLag.c).hideQueryRides();
    }

    public void hideCreateRide() {
        ((MainGUIController) mainLag.c).hideCreateRide();
    }

    public void hideDriverRidePanel() {
        ((MainGUIController) mainLag.c).hideDriverRidePanel();
    }


    class Window {
        Controller c;
        Parent ui;

        public Parent getUi() {
            return ui;
        }
    }

    private Window load(String fxmlfile) throws IOException {
        Window window = new Window();
        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxmlfile), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
        loader.setControllerFactory(controllerClass -> {
            try {
                return controllerClass
                        .getConstructor(BlFacade.class)
                        .newInstance(businessLogic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        window.ui = loader.load();
        ((Controller) loader.getController()).setMainApp(this);
        window.c = loader.getController();
        return window;
    }

    public void init(Stage stage) throws IOException {

        this.stage = stage;
        mainLag = load("/views/MainGUI.fxml");
        queryRidesLag = load("/views/QueryRides.fxml");
        createRideLag = load("/views/CreateRide.fxml");
        loginLag = load("/views/Login.fxml");
        registerLag = load("/views/Register.fxml");
        dRidePanelLag = load("/views/DriverRidePanel.fxml");
        showMain();

    }


    public void showMain() {
        setupScene(mainLag.ui, "MainTitle", 1050, 500);
    }

    public void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
    }
    /**
     * This method sets the scene to the center of the mainPane: queryRides or createRide
     * @param window the window to be shown
     */
    public void showSceneInCenter(String window) {
        switch (window) {
            case "queryRides" -> mainPane.setCenter(queryRidesLag.ui);
            case "createRide" -> mainPane.setCenter(createRideLag.ui);
            case "login" -> mainPane.setCenter(loginLag.ui);
            case "register" -> mainPane.setCenter(registerLag.ui);
            case "dRidePanel" -> {
                mainPane.setCenter(dRidePanelLag.ui);
                ((DriverRidePanelController) dRidePanelLag.c).updateRides();
            }
        }

    }
    private void setupScene(Parent ui, String title, int width, int height) {

        if (scene == null) {
            scene = new Scene(ui, width, height);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fontstyle.css")).toExternalForm());

            stage.setScene(scene);
        }
        stage.setMinWidth(width);
        stage.setMinHeight(height);

        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString(title));

        scene.setRoot(ui);
        stage.show();
    }

    public void removeLogRegButton() {
        ((MainGUIController) mainLag.c) .removeLogRegButton();
    }

    public void showUserIcon() {
        ((MainGUIController) mainLag.c) .showUserIcon();
    }

    public void setUserName(String name) {
        ((MainGUIController) mainLag.c) .setUserName(name);
    }

}
