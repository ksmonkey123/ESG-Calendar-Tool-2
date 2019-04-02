package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.fx.FxController;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;

@Log
@Controller
@RequiredArgsConstructor
public class PublishingRootController implements FxController {

    public Label title;
    public TabPane tabs;
    public Tab dateSelection;
    public Tab calendarSelection;
    public Tab eventSelection;

    @Getter
    private boolean unpublish = false;

    public void reset(boolean isUnpublish) {
        this.unpublish = isUnpublish;
        updateTitle();
        showDateSelection();
    }

    private void updateTitle() {
        if (unpublish) {
            title.setText("Publikation widerrufen");
        } else {
            title.setText("Publikation erfassen");
        }
    }

    void showCalendarSelection() {
        log.info("showing calendar selection");
        tabs.getSelectionModel().select(calendarSelection);
    }

    void showDateSelection() {
        log.info("showing date selection");
        tabs.getSelectionModel().select(dateSelection);
    }

    void showEventSelection() {
        log.info("showing event selection");
        tabs.getSelectionModel().select(eventSelection);
    }
}
