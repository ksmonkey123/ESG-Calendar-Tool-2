package ch.awae.esgcal.core.fx.export;

import ch.awae.esgcal.core.fx.FxController;
import ch.awae.esgcal.core.fx.RootController;
import ch.awae.esgcal.core.service.export.ExportByDateType;
import ch.awae.esgcal.core.service.export.ExportByYearType;
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
        context.getBean(ExportByYearController.class).reset(exportType);
        tabs.getSelectionModel().select(yearTab);
    }

    private void propenplan(ExportByDateType exportType) {
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
