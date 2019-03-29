package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.service.DateService;
import javafx.scene.control.DatePicker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PublishingDateSelectionController extends FxController {

    public DatePicker dateFrom;
    public DatePicker dateTo;

    private final DateService dateService;
    private final RootController rootController;
    private final PublishingRootController publishingRootController;
    private final PublishingCalendarSelectionController calendarSelectionController;

    @Override
    public void initialize() {
        dateFrom.setShowWeekNumbers(true);
        dateTo.setShowWeekNumbers(true);
        dateFrom.setValue(dateService.getBeginningOfYear());
        dateTo.setValue(dateService.getBeginningOfYear().plusYears(1).minusDays(1));
    }

    public void onBack() {
        rootController.showMenu();
    }

    public void onNext() throws Exception {
        calendarSelectionController.fetch(dateFrom.getValue(), dateTo.getValue());
        publishingRootController.showCalendarSelection();
    }

}
