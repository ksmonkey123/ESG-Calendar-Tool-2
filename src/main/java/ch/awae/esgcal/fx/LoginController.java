package ch.awae.esgcal.fx;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.service.AsyncJobService;
import ch.awae.esgcal.service.datasource.LoginService;
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
