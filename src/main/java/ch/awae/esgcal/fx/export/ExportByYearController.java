package ch.awae.esgcal.fx.export;

import ch.awae.esgcal.DateService;
import ch.awae.esgcal.PostConstructBean;
import ch.awae.esgcal.api.export.ExportByYearService;
import ch.awae.esgcal.api.export.ExportByYearType;
import ch.awae.esgcal.async.AsyncService;
import ch.awae.esgcal.fx.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.fx.modal.PopupService;
import ch.awae.esgcal.fx.modal.SaveLocationService;
import javafx.scene.control.ComboBox;
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
public class ExportByYearController implements FxController, PostConstructBean {

    private final DateService dateService;
    private final ExportRootController exportRootController;
    private final ExportByYearService exportByYearService;
    private final PopupService popupService;
    private final ErrorReportService errorReportService;
    private final RootController rootController;
    private final AsyncService asyncService;
    private final SaveLocationService saveLocationService;

    public ComboBox<Integer> year;
    public Label title;
    public BorderPane pane;

    private ExportByYearType exportType;

    private String fileSuffix;
    private String fileSuffixESG;

    @Override
    public void postContruct(ApplicationContext context) {
        fileSuffix = context.getEnvironment().getRequiredProperty("export.format", String.class);
        fileSuffixESG = context.getEnvironment().getRequiredProperty("export.formatESG", String.class);
    }

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
        Optional<String> saveFile = saveLocationService.prompt(exportType.getText(), exportType == ExportByYearType.ESG ? fileSuffixESG : fileSuffix);
        if (!saveFile.isPresent()) {
            pane.setDisable(false);
            return;
        }
        runExport(saveFile.get());
    }

    private void runExport(String file) {
        asyncService.schedule(
                () -> exportByYearService.performExport(exportType, file, year.getValue()),
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
