package ch.awae.esgcal.core.fx.export;

import ch.awae.esgcal.core.fx.FxController;
import ch.awae.esgcal.core.export.ExportByYearType;
import ch.awae.esgcal.core.DateService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ExportByYearController implements FxController {

    private final DateService dateService;
    private final ExportRootController exportRootController;

    public ComboBox<Integer> year;
    public Label title;

    private ExportByYearType exportType;

    @Override
    public void initialize() {
        year.getItems().setAll(dateService.getYearsForSelection());
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
        // TODO
    }
}
