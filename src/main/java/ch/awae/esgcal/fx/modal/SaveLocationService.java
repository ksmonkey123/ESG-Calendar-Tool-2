package ch.awae.esgcal.fx.modal;

import javafx.stage.FileChooser;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Log
@Service
public class SaveLocationService {

    public Optional<String> prompt(String name, String suffix) {
        log.info("prompting for save location");
        Optional<File> file = askUser(name + suffix);
        Optional<String> string = file.map(f -> {
            String ff = f.getAbsolutePath();
            if (suffix == null || ff.endsWith(suffix))
                return ff;
            return ff + suffix;
        });
        if (string.isPresent()) {
            log.info("save location: " + string.get());
        } else {
            log.info("save file prompt aborted");
        }
        return string;
    }

    private Optional<File> askUser(String name) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName(name);
        return Optional.ofNullable(fileChooser.showSaveDialog(null));
    }

}