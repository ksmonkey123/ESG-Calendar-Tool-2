package ch.awae.esgcal.fx.export;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.modal.SaveLocationService;
import ch.awae.esgcal.service.DateService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ExportByYearController extends FxController {

    private final DateService dateService;
    private final SaveLocationService saveLocationService;

    public ComboBox<Integer> year;
    public Label saveLocationLabel;

    @Override
    public void initialize() {
        year.getItems().setAll(dateService.getYearsForSelection());
        year.getSelectionModel().select(1);
    }

    public void onChooseLocation() {
        Optional<String> result = saveLocationService.prompt("Jahresplan", ".xlsm");
        saveLocationLabel.setText("Speichern unter: " + result.orElse("(bitte wählen)"));
    }
}
