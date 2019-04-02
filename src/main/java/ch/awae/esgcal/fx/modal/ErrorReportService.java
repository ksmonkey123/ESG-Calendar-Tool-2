package ch.awae.esgcal.fx.modal;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Log
@Service
public class ErrorReportService {

    public void report(Throwable ex) {
        log.info("reporting error: " + ex.toString());
        ex.printStackTrace(System.err);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ex.getClass().getName());
        alert.setHeaderText("Es ist ein Fehler aufgetreten!");
        alert.setContentText(ex.toString());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = reshuffle(sw.toString());

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
        log.info("error report closed");
    }

    private String reshuffle(String exceptionText) {
        // reshuffle Caused-By clauses
        Deque<List<String>> blocks = new ArrayDeque<>();
        List<String> list = new ArrayList<>();
        String[] lines = exceptionText.split("\n");
        boolean first = true;
        for (String line : lines) {
            if (first) {
                list.add(line);
                first = false;
            } else if (line.startsWith("Caused by: ")) {
                blocks.push(list);
                first = true;
                list = new ArrayList<>();
                list.add(line.substring(11));
            } else {
                list.add(line);
            }
        }
        blocks.push(list);

        // print the blocks in reverse order
        boolean firstLine = true;
        StringBuilder sb = new StringBuilder();
        for (List<String> block : blocks) {
            boolean firstOfBlock = true;
            for (String line : block) {
                if (firstOfBlock && !firstLine) {
                    sb.append("Caused: ");
                }
                firstLine = false;
                firstOfBlock = false;
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

}
