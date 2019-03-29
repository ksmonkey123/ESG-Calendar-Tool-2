package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.FxController;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PublishingRootController extends FxController {

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
        tabs.getSelectionModel().select(calendarSelection);
    }

    void showDateSelection() {
        tabs.getSelectionModel().select(dateSelection);
    }

    void showEventSelection() {
        tabs.getSelectionModel().select(eventSelection);
    }
}
