package ch.awae.esgcal.fx;

import ch.awae.esgcal.service.google.AuthenticationService;
import javafx.scene.control.Button;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    public Button loginButton;

    private final AuthenticationService authenticationService;
    private final RootController rootController;

    public void onLogin() {
        loginButton.setDisable(true);
        try {
            authenticationService.authenticate();
            rootController.showMenu();
        } catch (GeneralSecurityException | IOException | InterruptedException e) {
            loginButton.setDisable(false);
            e.printStackTrace();
        }
    }
}
