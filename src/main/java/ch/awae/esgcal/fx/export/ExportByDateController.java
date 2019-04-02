package ch.awae.esgcal.fx.export;

import ch.awae.esgcal.PostConstructBean;
import ch.awae.esgcal.api.export.ExportByDateService;
import ch.awae.esgcal.api.export.ExportByDateType;
import ch.awae.esgcal.async.AsyncService;
import ch.awae.esgcal.fx.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.fx.modal.PopupService;
import ch.awae.esgcal.fx.modal.SaveLocationService;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Log
@Controller
@RequiredArgsConstructor
public class ExportByDateController implements FxController, PostConstructBean {

    public Label title;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Button executeButton;
    public BorderPane pane;

    private ExportByDateType exportType;

    private final ExportRootController exportRootController;
    private final ExportByDateService exportByDateService;
    private final PopupService popupService;
    private final ErrorReportService errorReportService;
    private final RootController rootController;
    private final SaveLocationService saveLocationService;
    private final AsyncService asyncService;

    private String fileSuffix;

    @Override
    public void postContruct(ApplicationContext context) {
        fileSuffix = context.getEnvironment().getRequiredProperty("export.format", String.class);
    }

    @Override
    public void initialize() {
        dateFrom.setShowWeekNumbers(true);
        dateTo.setShowWeekNumbers(true);
        dateFrom.valueProperty().addListener((a, b, c) -> dateChanged());
        dateTo.valueProperty().addListener((a, b, c) -> dateChanged());
        dateChanged();
    }

    private void dateChanged() {
        executeButton.setDisable(dateFrom.getValue() == null
                || dateTo.getValue() == null
                || dateFrom.getValue().isAfter(dateTo.getValue()));
    }

    public void onBack() {
        exportRootController.reset();
    }

    void reset(ExportByDateType exportType) {
        log.info("resetting for " + exportType.getText());
        this.exportType = exportType;
        this.title.setText(exportType.getText());
        initialize();
    }

    public void onExecute() {
        pane.setDisable(true);
        Optional<String> saveFile = saveLocationService.prompt(exportType.getText(), fileSuffix);
        if (!saveFile.isPresent()) {
            pane.setDisable(false);
            return;
        }
        runExport(saveFile.get());
    }

    private void runExport(String file) {
        asyncService.schedule(
                () -> exportByDateService.performExport(exportType, file, dateFrom.getValue(), dateTo.getValue()),
                this::onExportComplete,
                this::onExportFailed);
    }

    private void onExportComplete() {
        popupService.info("Export ausgeführt");
        rootController.showMenu();
        pane.setDisable(false);
    }

    private void onExportFailed(Throwable error) {
        errorReportService.report(error);
        pane.setDisable(false);
    }
}
