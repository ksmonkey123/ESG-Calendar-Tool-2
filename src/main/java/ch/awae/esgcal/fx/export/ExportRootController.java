package ch.awae.esgcal.fx.export;

import ch.awae.esgcal.FxController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Controller;

@Controller
public class ExportRootController extends FxController {

    public TabPane tabs;
    public Tab menuTab;
    public Tab yearTab;

    public void onJahresplanESG() {
        tabs.getSelectionModel().select(yearTab);
    }

    public void onJahresplanBern() {
    }

    public void onJahresplanZuerich() {
    }

    public void onPropenplanBern() {
    }

    public void onProbenplanZuerich() {
    }

    public void onGantaetgigeTermine() {
    }

    public void showMenu() {
        tabs.getSelectionModel().select(menuTab);
    }

}
