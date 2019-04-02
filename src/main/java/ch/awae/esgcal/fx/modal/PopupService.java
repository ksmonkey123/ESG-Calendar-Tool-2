package ch.awae.esgcal.fx.modal;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log
@Service
public class PopupService {

    public void info(String content) {
        log.info("showing information popup: " + content);
        alert(Alert.AlertType.INFORMATION, "Hinweis", content);
    }

    public void warn(String content) {
        log.info("showing warning popup: " + content);
        alert(Alert.AlertType.WARNING, "Warnung", content);
    }

    public void error(String content) {
        log.info("showing error popup: " + content);
        alert(Alert.AlertType.ERROR, "Fehler", content);
    }

    public boolean confirm(String content) {
        log.info("showing confirmation popup: " + content);
        return alert(Alert.AlertType.CONFIRMATION, "Bitte Bestätigen", content).filter(ButtonType.OK::equals).isPresent();
    }

    private Optional<ButtonType> alert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Optional<ButtonType> button = alert.showAndWait();
        log.info("popup closed");
        return button;
    }

}
