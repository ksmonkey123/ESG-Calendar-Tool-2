package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.PostConstructBean;
import ch.awae.esgcal.api.calendar.ApiException;
import ch.awae.esgcal.api.calendar.Calendar;
import ch.awae.esgcal.api.calendar.CalendarService;
import ch.awae.esgcal.fx.FxController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.utils.functional.T2;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log
@Controller
@RequiredArgsConstructor
public class PublishingCalendarSelectionController implements FxController, PostConstructBean {

    public ListView<ListEntry> calendarList;

    private final ObservableList<ListEntry> calendarPairs = FXCollections.observableArrayList();
    private LocalDate startDate, endDate;

    private String suffix;
    private final CalendarService calendarService;
    private final PublishingRootController publishingRootController;
    private final PublishingEventSelectionController publishingEventSelectionController;
    private final ErrorReportService errorReportService;

    @Override
    public void postContruct(ApplicationContext context) {
        suffix = context.getEnvironment().getRequiredProperty("calendar.planning-suffix");
        log.config("calendarSuffix = '" + suffix + "'");
    }

    void fetch(LocalDate startDate, LocalDate endDate) throws ApiException {
        this.startDate = startDate;
        this.endDate = endDate;
        log.info("fetching calendar pairs");
        boolean unpublish = publishingRootController.isUnpublish();
        List<T2<Calendar, Calendar>> pairs = calendarService.getCalendarPairs(suffix);
        this.calendarPairs.clear();
        for (T2<Calendar, Calendar> pair : pairs) {
            if (unpublish) {
                this.calendarPairs.add(new ListEntry(pair._1, pair._2, new SimpleBooleanProperty()));
            } else {
                this.calendarPairs.add(new ListEntry(pair._2, pair._1, new SimpleBooleanProperty()));
            }
        }
        log.info("found " + this.calendarPairs.size() + " calendar pairs");
        calendarList.setItems(this.calendarPairs);
        calendarList.setCellFactory(CheckBoxListCell.forListView(ListEntry::getSelection));
    }

    public void onBack() {
        log.info("returning to date selection");
        publishingRootController.showDateSelection();
    }

    public void onNext() {
        log.info("transitioning to event selection");
        List<T2<Calendar, Calendar>> selected = new ArrayList<>();

        for (ListEntry entry : calendarPairs)
            if (entry.selection.get())
                selected.add(T2.of(entry.from, entry.to));

        try {
            publishingEventSelectionController.fetch(selected, startDate, endDate);
            publishingRootController.showEventSelection();
        } catch (Exception e) {
            errorReportService.report(e);
            log.info("transition failed");
        }
    }

    @AllArgsConstructor
    private static class ListEntry {
        private final Calendar from, to;
        @Getter
        private final BooleanProperty selection;

        @Override
        public String toString() {
            return from.getName() + " -> " + to.getName();
        }

    }

}
