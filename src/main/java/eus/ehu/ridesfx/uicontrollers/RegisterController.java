package eus.ehu.ridesfx.uicontrollers;
import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.ui.MainGUI;
public class RegisterController implements Controller {
    private MainGUI mainGUI;
    private eus.ehu.ridesfx.businessLogic.BlFacade businessLogic;

    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public RegisterController(BlFacade bl) {
        businessLogic = bl;
    }

}
