package ch.awae.esgcal.core.fx.export;

import ch.awae.esgcal.core.export.ExportByDateType;
import ch.awae.esgcal.core.export.ExportByYearType;
import ch.awae.esgcal.core.fx.FxController;
import ch.awae.esgcal.core.fx.RootController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Log
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
        jahresplan(ExportByYearType.ESG);
    }

    public void onJahresplanBern() {
        jahresplan(ExportByYearType.BERN);
    }

    public void onJahresplanZuerich() {
        jahresplan(ExportByYearType.ZUERICH);
    }

    public void onPropenplanBern() {
        propenplan(ExportByDateType.BERN);
    }

    public void onProbenplanZuerich() {
        propenplan(ExportByDateType.ZUERICH);
    }

    public void onGantaetgigeTermine() {
        propenplan(ExportByDateType.GANZTAG);
    }

    private void jahresplan(ExportByYearType exportType) {
        log.info("transitioning to year selection for " + exportType);
        context.getBean(ExportByYearController.class).reset(exportType);
        tabs.getSelectionModel().select(yearTab);
    }

    private void propenplan(ExportByDateType exportType) {
        log.info("transitioning to date selection for " + exportType);
        context.getBean(ExportByDateController.class).reset(exportType);
        tabs.getSelectionModel().select(dateTab);
    }

    public void reset() {
        log.info("transitioning to export menu");
        tabs.getSelectionModel().select(menuTab);
    }

    public void onBack() {
        log.info("returning to main menu");
        rootController.showMenu();
    }
}
