package ch.awae.esgcal.fx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;

@Log
@Controller
public class RootController implements FxController {

    public TabPane tabs;
    public Tab menuTab;
    public Tab exportTab;
    public Tab publishingTab;

    public void showMenu() {
        log.info("showing main menu");
        tabs.getSelectionModel().select(menuTab);
    }

    void showExport() {
        log.info("showing export");
        tabs.getSelectionModel().select(exportTab);
    }

    void showPublishing() {
        log.info("showing (un)publishing");
        tabs.getSelectionModel().select(publishingTab);
    }
}
