package ch.awae.esgcal.core.fx;

import ch.awae.esgcal.core.fx.modal.ErrorReportService;
import ch.awae.esgcal.core.service.AsyncJobService;
import ch.awae.esgcal.core.service.api.LoginService;
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
    private final AsyncJobService async;

    public void onLogin() {
        loginButton.setDisable(true);
        async.schedule(() -> {
            try {
                loginService.login();
                rootController.showMenu();
            } catch (Exception e) {
                errorReportService.report(e);
                loginButton.setDisable(false);
            }
        });
    }
}
