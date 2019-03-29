package ch.awae.esgcal.fx.export;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.modal.SaveLocationService;
import ch.awae.esgcal.model.JahresExport;
import ch.awae.esgcal.service.DateService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ExportByYearController implements FxController {

    private final DateService dateService;
    private final ExportRootController exportRootController;

    public ComboBox<Integer> year;
    public Label title;

    private JahresExport exportType;

    @Override
    public void initialize() {
        year.getItems().setAll(dateService.getYearsForSelection());
        year.getSelectionModel().select(1);
    }

    public void onBack() {
        exportRootController.reset();
    }

    void reset(JahresExport exportType) {
        this.exportType = exportType;
        this.title.setText(exportType.getText());
        initialize();
    }

    public void onExecute() {
        // TODO
    }
}
