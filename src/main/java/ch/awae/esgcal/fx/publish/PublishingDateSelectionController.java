package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.DateService;
import ch.awae.esgcal.async.AsyncService;
import ch.awae.esgcal.fx.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;

@Log
@Controller
@RequiredArgsConstructor
public class PublishingDateSelectionController implements FxController {

    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Button nextButton;

    private final DateService dateService;
    private final RootController rootController;
    private final PublishingRootController publishingRootController;
    private final PublishingCalendarSelectionController calendarSelectionController;
    private final ErrorReportService errorReportService;
    private final AsyncService asyncService;

    @Override
    public void initialize() {
        dateFrom.setShowWeekNumbers(true);
        dateTo.setShowWeekNumbers(true);
        dateFrom.setValue(dateService.getBeginningOfYear());
        dateTo.setValue(dateService.getBeginningOfYear().plusYears(1).minusDays(1));
        dateFrom.valueProperty().addListener((a, b, c) -> dateChanged());
        dateTo.valueProperty().addListener((a, b, c) -> dateChanged());
        dateChanged();
    }

    private void dateChanged() {
        nextButton.setDisable(dateFrom.getValue() == null
                || dateTo.getValue() == null
                || dateFrom.getValue().isAfter(dateTo.getValue()));
    }

    public void onBack() {
        log.info("returning to menu");
        rootController.showMenu();
    }

    public void onNext() {
        log.info("transitioning to calendar selection");
        asyncService.schedule(
                () -> calendarSelectionController.fetch(dateFrom.getValue(), dateTo.getValue()),
                publishingRootController::showCalendarSelection,
                this::onTransitionFailed);
    }

    private void onTransitionFailed(Throwable e) {
        errorReportService.report(e);
        log.info("transition failed");
    }
}
