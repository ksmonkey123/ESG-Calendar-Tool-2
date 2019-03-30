package ch.awae.esgcal.core.fx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Controller;

@Controller
public class RootController implements FxController {

    public TabPane tabs;
    public Tab menuTab;
    public Tab exportTab;
    public Tab publishingTab;

    public void showMenu() {
        tabs.getSelectionModel().select(menuTab);
    }

    void showExport() {
        tabs.getSelectionModel().select(exportTab);
    }

    void showPublishing() {
        tabs.getSelectionModel().select(publishingTab);
    }
}
