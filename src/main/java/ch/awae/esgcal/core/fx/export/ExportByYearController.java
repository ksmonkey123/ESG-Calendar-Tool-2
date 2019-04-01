package ch.awae.esgcal.core.fx.export;

import ch.awae.esgcal.core.DateService;
import ch.awae.esgcal.core.export.ExportByYearService;
import ch.awae.esgcal.core.export.ExportByYearType;
import ch.awae.esgcal.core.fx.FxController;
import ch.awae.esgcal.core.fx.RootController;
import ch.awae.esgcal.core.fx.modal.ErrorReportService;
import ch.awae.esgcal.core.fx.modal.PopupService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

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
        this.exportType = exportType;
        this.title.setText(exportType.getText());
        initialize();
    }

    public void onExecute() {
        pane.setDisable(true);
        try {
            if (exportByYearService.performExport(exportType, year.getValue())) {
                popupService.info("Export ausgeführt");
                rootController.showMenu();
            }
        } catch (Exception e) {
            errorReportService.report(e);
        }
        pane.setDisable(false);
    }
}
