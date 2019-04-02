package ch.awae.esgcal.fx;

import ch.awae.esgcal.api.calendar.LoginService;
import ch.awae.esgcal.async.AsyncService;
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
    private final AsyncService asyncService;

    public void onLogin() {
        log.info("starting login process");
        loginButton.setDisable(true);
        asyncService.schedule(
                loginService::login,
                this::onLoginComplete,
                this::onLoginFailed);
    }

    private void onLoginComplete() {
        log.info("login complete");
        rootController.showMenu();
    }

    private void onLoginFailed(Throwable error) {
        errorReportService.report(error);
        log.info("login failed");
        loginButton.setDisable(false);
    }
}
