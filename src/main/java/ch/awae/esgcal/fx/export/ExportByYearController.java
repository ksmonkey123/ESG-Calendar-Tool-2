package ch.awae.esgcal.fx.export;

import ch.awae.esgcal.DateService;
import ch.awae.esgcal.api.export.ExportByYearService;
import ch.awae.esgcal.api.export.ExportByYearType;
import ch.awae.esgcal.fx.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.fx.modal.PopupService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;

@Log
@Controller
@RequiredArgsConstructor
public class ExportByYearController implements FxController {

    private final DateService dateService;
    private final ExportRootController exportRootController;
    private final ExportByYearService exportByYearService;
    private final PopupService popupService;
    private final ErrorReportService errorReportService;
    private final RootController rootController;

    public ComboBox<Integer> year;
    public Label title;
    public BorderPane pane;

    private ExportByYearType exportType;

    @Override
    public void initialize() {
        year.getItems().setAll(dateService.getYearsForSelection(10));
        year.getSelectionModel().select(1);
    }

    public void onBack() {
        exportRootController.reset();
    }

    void reset(ExportByYearType exportType) {
        log.info("resetting for " + exportType.getText());
        this.exportType = exportType;
        this.title.setText(exportType.getText());
        initialize();
    }

    public void onExecute() {
        log.info("executing export");
        pane.setDisable(true);
        try {
            if (exportByYearService.performExport(exportType, year.getValue())) {
                popupService.info("Export ausgeführt");
                rootController.showMenu();
            }
        } catch (Exception e) {
            errorReportService.report(e);
            log.info("export failed");
        }
        pane.setDisable(false);
    }
}