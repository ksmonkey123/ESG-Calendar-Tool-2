package ch.awae.esgcal.core.fx.export;

import ch.awae.esgcal.core.fx.FxController;
import ch.awae.esgcal.core.service.export.ExportByDateType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ExportByDateController implements FxController {

    public Label title;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Button executeButton;

    private ExportByDateType exportType;

    private final ExportRootController exportRootController;

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
        this.exportType = exportType;
        this.title.setText(exportType.getText());
        initialize();
    }

    public void onExecute() {
        // TODO
    }

}
