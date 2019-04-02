package ch.awae.esgcal.fx;

import ch.awae.esgcal.api.calendar.LoginService;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import javafx.scene.control.Button;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;

@Log
@Controller
@RequiredArgsConstructor
public class LoginController implements FxController {

    public Button loginButton;

    private final ErrorReportService errorReportService;
    private final LoginService loginService;
    private final RootController rootController;

    public void onLogin() {
        log.info("starting login process");
        loginButton.setDisable(true);
        try {
            log.info("login complete");
            loginService.login();
            rootController.showMenu();
        } catch (Exception e) {
            errorReportService.report(e);
            log.info("login failed");
            loginButton.setDisable(false);
        }
    }
}
