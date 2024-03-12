package uicontrollers;
import businessLogic.BlFacade;
import ui.MainGUI;
public class RegisterController implements Controller {
    private MainGUI mainGUI;
    private businessLogic.BlFacade businessLogic;

    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public RegisterController(BlFacade bl) {
        businessLogic = bl;
    }

}
