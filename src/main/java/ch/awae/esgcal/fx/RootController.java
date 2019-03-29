package ch.awae.esgcal.fx;

import ch.awae.esgcal.FxController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Controller;

@Controller
public class RootController extends FxController {

    public TabPane tabs;
    public Tab menuTab;
    public Tab exportTab;

    void showMenu() {
        tabs.getSelectionModel().select(menuTab);
    }

    void showExport() {
        tabs.getSelectionModel().select(exportTab);
    }
}
