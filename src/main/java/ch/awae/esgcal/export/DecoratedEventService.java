package ch.awae.esgcal.export;

import ch.awae.esgcal.api.calendar.ApiException;
import ch.awae.esgcal.api.calendar.Calendar;
import ch.awae.esgcal.api.calendar.Event;
import ch.awae.esgcal.api.calendar.EventService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DecoratedEventService {

    @Delegate
    private final EventService eventService;
    private final DecoratedCalendarService calendarService;

    public Map<ExportCalendar, List<Event>> listEvents(LocalDate dateFrom, LocalDate dateTo, ExportCalendar... calendars) throws ApiException {
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
