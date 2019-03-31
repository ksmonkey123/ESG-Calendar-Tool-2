package ch.awae.esgcal.export;

import ch.awae.esgcal.core.api.ApiException;
import ch.awae.esgcal.core.api.Calendar;
import ch.awae.esgcal.core.api.Event;
import ch.awae.esgcal.core.api.EventService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.apache.poi.ss.formula.functions.Even;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
class DecoratedEventService {

    @Delegate
    private final EventService eventService;
    private final DecoratedCalendarService calendarService;

    Map<ExportCalendar, List<Event>> listEvents(LocalDate dateFrom, LocalDate dateTo, ExportCalendar... calendars) throws ApiException {
        Map<ExportCalendar, List<Event>> map = new HashMap<>();

        for (ExportCalendar calendar : calendars) {
            List<Event> events = new ArrayList<>();

            for (Calendar cal : calendarService.getCalendars(calendar)) {
                events.addAll(eventService.listEvents(cal, dateFrom, dateTo));
            }

            map.put(calendar, events);
        }

        return map;
    }

}
