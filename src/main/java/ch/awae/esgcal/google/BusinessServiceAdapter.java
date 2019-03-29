package ch.awae.esgcal.google;

import ch.awae.esgcal.model.Calendar;
import ch.awae.esgcal.model.Event;
import ch.awae.esgcal.service.datasource.CalendarService;
import ch.awae.esgcal.service.datasource.EventService;
import ch.awae.utils.functional.T2;
import com.google.api.services.calendar.model.CalendarListEntry;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class BusinessServiceAdapter implements CalendarService, EventService {

    private final CalendarAgent calendarAgent;

    @Override
    public List<Calendar> listCalendars() throws Exception {
        List<CalendarListEntry> raw = calendarAgent.getCalendarList();
        List<Calendar> result = new ArrayList<>();
        for (CalendarListEntry e : raw) {
            result.add(new GoogleCalendar(e));
        }
        return result;
    }

    @Override
    public List<ch.awae.esgcal.model.Event> listEvents(Calendar calendar, LocalDate from, LocalDate to) throws Exception {
        val start = java.sql.Date.valueOf(from);
        val end = java.sql.Date.valueOf(to);
        val events = calendarAgent.getEventsOfCalendar(((GoogleCalendar) calendar).getBacker(), T2.of(start, end));
        List<ch.awae.esgcal.model.Event> result = new ArrayList<>();
        for (val event : events) {
            result.add(new GoogleEvent(event));
        }
        return result;
    }

    @Override
    public void moveEvents(List<Event> events, Calendar from, Calendar to) throws Exception {
        CalendarListEntry c1 = ((GoogleCalendar) from).getBacker();
        CalendarListEntry c2 = ((GoogleCalendar) to).getBacker();
        for (Event event : events) {
            calendarAgent.moveEvent(((GoogleEvent) event).getBacker(), c1, c2);
        }
    }
}
