package ch.awae.esgcal.fx.export;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.model.DateExport;
import ch.awae.esgcal.model.JahresExport;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ExportRootController implements FxController {

    public TabPane tabs;
    public Tab menuTab;
    public Tab yearTab;
    public Tab dateTab;

    private final RootController rootController;
    private final ApplicationContext context;

    public void onJahresplanESG() {
        jahresplan(JahresExport.ESG);
    }

    public void onJahresplanBern() {
        jahresplan(JahresExport.BERN);
    }

    public void onJahresplanZuerich() {
        jahresplan(JahresExport.ZUERICH);
    }

    public void onPropenplanBern() {
        propenplan(DateExport.BERN);
    }

    public void onProbenplanZuerich() {
        propenplan(DateExport.ZUERICH);
    }

    public void onGantaetgigeTermine() {
        propenplan(DateExport.GANZTAG);
    }

    private void jahresplan(JahresExport exportType) {
        context.getBean(ExportByYearController.class).reset(exportType);
        tabs.getSelectionModel().select(yearTab);
    }

    private void propenplan(DateExport exportType) {
        context.getBean(ExportByDateController.class).reset(exportType);
        tabs.getSelectionModel().select(dateTab);
    }

    public void reset() {
        tabs.getSelectionModel().select(menuTab);
    }

    public void onBack() {
        rootController.showMenu();
    }
}
