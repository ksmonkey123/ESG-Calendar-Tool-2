package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.model.Calendar;
import ch.awae.esgcal.service.CalendarService;
import ch.awae.utils.functional.T2;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublishingCalendarSelectionController implements FxController {

    public ListView<ListEntry> calendarList;

    private ObservableList<ListEntry> calendarPairs = FXCollections.observableArrayList();
    private LocalDate startDate;
    private LocalDate endDate;

    private final CalendarService calendarService;
    private final PublishingRootController publishingRootController;
    private final PublishingEventSelectionController publishingEventSelectionController;
    private final ErrorReportService errorReportService;

    void fetch(LocalDate startDate, LocalDate endDate) throws Exception {
        this.startDate = startDate;
        this.endDate = endDate;
        boolean unpublish = publishingRootController.isUnpublish();
        List<T2<Calendar, Calendar>> pairs = calendarService.getCalendarPairs(" - Planung");
        this.calendarPairs.clear();
        for (T2<Calendar, Calendar> pair : pairs) {
            if (unpublish) {
                this.calendarPairs.add(new ListEntry(pair._1, pair._2, new SimpleBooleanProperty()));
            } else {
                this.calendarPairs.add(new ListEntry(pair._2, pair._1, new SimpleBooleanProperty()));
            }
        }
        calendarList.setItems(this.calendarPairs);
        calendarList.setCellFactory(CheckBoxListCell.forListView(this::listCallback));
    }

    private ObservableValue<Boolean> listCallback(ListEntry entry) {
        return entry.selection;
    }

    public void onBack() {
        publishingRootController.showDateSelection();
    }

    public void onNext() {
        List<T2<Calendar, Calendar>> selected = new ArrayList<>();

        for (ListEntry entry : calendarPairs) {
            if (entry.selection.get()) {
                selected.add(T2.of(entry.from, entry.to));
            }
        }

        try {
            publishingEventSelectionController.fetch(selected, startDate, endDate);
            publishingRootController.showEventSelection();
        } catch (Exception e) {
            errorReportService.report(e);
        }
    }

    @Data
    @AllArgsConstructor
    private static class ListEntry {
        private final Calendar from, to;
        private BooleanProperty selection;

        @Override
        public String toString() {
            return from.getName() + " -> " + to.getName();
        }

    }

}
