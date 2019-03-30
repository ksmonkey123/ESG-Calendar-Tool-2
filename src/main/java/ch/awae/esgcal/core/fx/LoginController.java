package ch.awae.esgcal.core.fx;

import ch.awae.esgcal.core.api.LoginService;
import ch.awae.esgcal.core.fx.modal.ErrorReportService;
import javafx.scene.control.Button;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginController implements FxController {

    public Button loginButton;

    private final ErrorReportService errorReportService;
    private final LoginService loginService;
    private final RootController rootController;

    public void onLogin() {
        loginButton.setDisable(true);
        try {
            loginService.login();
            rootController.showMenu();
        } catch (Exception e) {
            errorReportService.report(e);
            loginButton.setDisable(false);
        }
    }
}
