package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.model.Calendar;
import ch.awae.esgcal.model.Event;
import ch.awae.esgcal.service.EventService;
import ch.awae.utils.functional.T2;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.CheckBoxListCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublishingEventSelectionController implements FxController {

    @Getter
    private final ErrorReportService errorReportService;
    private final PublishingRootController publishingRootController;
    private final RootController rootController;
    private final EventService eventService;

    public TabPane tabs;

    private List<T2<T2<Calendar, Calendar>, List<ListEntry>>> listEntries;

    void fetch(List<T2<Calendar, Calendar>> calendarPairs, LocalDate startDate, LocalDate endDate) throws Exception {
        tabs.getTabs().clear();
        listEntries = new ArrayList<>();

        for (T2<Calendar, Calendar> pair : calendarPairs) {
            List<ListEntry> list = new ArrayList<>();
            listEntries.add(T2.of(pair, list));
            List<Event> events = eventService.listEvents(pair._1, startDate, endDate);

            Tab tab = new Tab();
            tab.setClosable(false);
            tab.setText(pair._1.getName());
            ListView<ListEntry> listView = new ListView<>();
            ObservableList<ListEntry> entries = FXCollections.observableArrayList();

            for (Event event : events) {
                ListEntry e = new ListEntry(pair._1, pair._2, event, new SimpleBooleanProperty());
                entries.add(e);
                list.add(e);
            }

            listView.setItems(entries);
            listView.setCellFactory(CheckBoxListCell.forListView(ListEntry::getSelection));
            tab.setContent(listView);

            tabs.getTabs().add(tab);
        }
    }

    private final static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy (HH:mm) ");

    @Data
    @AllArgsConstructor
    private static class ListEntry {
        private final Calendar from, to;
        private final Event event;
        private BooleanProperty selection;

        @Override
        public String toString() {
            return event.getStart().format(format) + event.getTitle();
        }

    }

    public void onExecute() {
        try {
            for (T2<T2<Calendar, Calendar>, List<ListEntry>> calendar : listEntries) {
                List<Event> events = new ArrayList<>();
                for (ListEntry entry : calendar._2) {
                    if (entry.getSelection().get()) {
                        events.add(entry.getEvent());
                    }
                }
                eventService.moveEvents(events, calendar._1._1, calendar._1._2);
            }
            rootController.showMenu();
        } catch (Exception e) {
            errorReportService.report(e);
        }
    }

    public void onBack() {
        publishingRootController.showCalendarSelection();
    }
}
