package ch.awae.esgcal.fx.publish;

import ch.awae.esgcal.api.calendar.ApiException;
import ch.awae.esgcal.api.calendar.Calendar;
import ch.awae.esgcal.api.calendar.Event;
import ch.awae.esgcal.api.calendar.EventService;
import ch.awae.esgcal.async.AsyncService;
import ch.awae.esgcal.fx.FxController;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.fx.modal.PopupService;
import ch.awae.utils.functional.T2;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log
@Controller
@RequiredArgsConstructor
public class PublishingEventSelectionController implements FxController {

    private final ErrorReportService errorReportService;
    private final PublishingRootController publishingRootController;
    private final RootController rootController;
    private final EventService eventService;
    private final PopupService popupService;
    private final AsyncService asyncService;

    public TabPane tabs;
    public BorderPane pane;

    private List<T2<T2<Calendar, Calendar>, List<ListEntry>>> listEntries;

    void fetch(List<T2<Calendar, Calendar>> calendarPairs, LocalDate startDate, LocalDate endDate) throws Exception {
        log.info("fetching events");
        boolean unpublish = publishingRootController.isUnpublish();
        Platform.runLater(() -> tabs.getTabs().clear());
        listEntries = new ArrayList<>();

        for (T2<Calendar, Calendar> pair : calendarPairs) {
            generateTab(startDate, endDate, pair, unpublish);
        }
    }

    private void generateTab(LocalDate startDate, LocalDate endDate, T2<Calendar, Calendar> pair, boolean unpublish) throws Exception {
        List<ListEntry> list = new ArrayList<>();
        listEntries.add(T2.of(pair, list));
        List<Event> events = eventService.listEvents(pair._1, startDate, endDate);

        Platform.runLater(() -> {
            Tab tab = new Tab();
            tab.setClosable(false);
            tab.setText(pair._1.getName());
            ListView<ListEntry> listView = new ListView<>();
            ObservableList<ListEntry> entries = FXCollections.observableArrayList();

            for (Event event : events) {
                ListEntry e = new ListEntry(pair._1, pair._2, event, new SimpleBooleanProperty());
                if (!unpublish)
                    e = e.selected();
                entries.add(e);
                list.add(e);
            }

            listView.setItems(entries);
            listView.setCellFactory(CheckBoxListCell.forListView(ListEntry::getSelection));
            tab.setContent(listView);
            tabs.getTabs().add(tab);
            log.info("added tab for calendar " + pair._1.getName() + " (" + events.size() + " events)");
        });
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

        ListEntry selected() {
            selection.set(true);
            return this;
        }

    }

    public void onBack() {
        log.info("returning to calendar selection");
        publishingRootController.showCalendarSelection();
    }

    public void onExecute() {
        if (publishingRootController.isUnpublish()) {
            log.info("performing unpublishing");
        } else {
            log.info("performing publishing");
        }
        pane.setDisable(true);
        asyncService.schedule(
                this::doPublish,
                this::onPublishingComplete,
                this::onPublishingFailed);
    }

    private void doPublish() throws ApiException {
        for (T2<T2<Calendar, Calendar>, List<ListEntry>> calendar : listEntries) {
            List<Event> events = new ArrayList<>();
            for (ListEntry entry : calendar._2)
                if (entry.getSelection().get())
                    events.add(entry.getEvent());
            eventService.moveEvents(events, calendar._1._1, calendar._1._2);
        }
    }

    private void onPublishingComplete() {
        popupService.info("Ereignisse erfolgreich verschoben.");
        log.info("(un)publishing done");
        rootController.showMenu();
        pane.setDisable(false);
    }

    private void onPublishingFailed(Throwable e) {
        log.info("(un)publishing failed");
        errorReportService.report(e);
        pane.setDisable(false);
    }

}