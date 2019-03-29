package ch.awae.esgcal.fx;

import ch.awae.esgcal.service.LoginService;
import javafx.scene.control.Button;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginController {

    public Button loginButton;

    private final LoginService loginService;
    private final RootController rootController;

    public void onLogin() {
        loginButton.setDisable(true);
        try {
            loginService.login();
            rootController.showMenu();
        } catch (Exception e) {
            loginButton.setDisable(false);
            e.printStackTrace();
        }
    }
}
