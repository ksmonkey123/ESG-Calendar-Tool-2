package ch.awae.esgcal.fx.modal;

import javafx.stage.FileChooser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class SaveLocationService {

    public Optional<String> prompt(String name, String suffix) {
        Optional<File> file = askUser(name + suffix);
        return file.map(f -> {
            String ff = f.getAbsolutePath();
            if (suffix == null || ff.endsWith(suffix))
                return ff;
            return ff + suffix;
        });
    }

    private Optional<File> askUser(String name) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName(name);
        return Optional.ofNullable(fileChooser.showSaveDialog(null));
    }

}