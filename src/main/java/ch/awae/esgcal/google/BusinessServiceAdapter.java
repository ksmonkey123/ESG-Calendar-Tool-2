package ch.awae.esgcal.google;

import ch.awae.esgcal.PostConstructBean;
import ch.awae.esgcal.api.calendar.*;
import ch.awae.esgcal.cache.ValueCache;
import ch.awae.utils.functional.T2;
import com.google.api.services.calendar.model.CalendarListEntry;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class BusinessServiceAdapter implements CalendarService, EventService, PostConstructBean {

    private final CalendarAgent calendarAgent;

    private ValueCache<List<Calendar>> calendarListCache;

    @Override
    public void postContruct(ApplicationContext context) {
        long cacheTime = context.getEnvironment().getRequiredProperty("google.api.cache-duration", long.class);
        calendarListCache = new ValueCache<>(cacheTime);
    }

    @Override
    public List<Calendar> listCalendars() throws ApiException {
        try {
            return calendarListCache.getOrEvaluate(() -> {
                List<CalendarListEntry> raw = calendarAgent.getCalendarList();
                List<Calendar> result = new ArrayList<>();
                for (CalendarListEntry e : raw) {
                    result.add(new GoogleCalendar(e));
                }
                return result;
            });
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    @Override
    public List<Event> listEvents(Calendar calendar, LocalDate from, LocalDate to) throws ApiException {
        try {
            val start = java.sql.Date.valueOf(from);
            val end = java.sql.Date.valueOf(to);
            val events = calendarAgent.getEventsOfCalendar(((GoogleCalendar) calendar).getBacker(), T2.of(start, end));
            List<Event> result = new ArrayList<>();
            for (val event : events) {
                result.add(new GoogleEvent(event));
            }
            return result;
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    @Override
    public void moveEvents(List<Event> events, Calendar from, Calendar to) throws ApiException {
        try {
            CalendarListEntry c1 = ((GoogleCalendar) from).getBacker();
            CalendarListEntry c2 = ((GoogleCalendar) to).getBacker();
            for (Event event : events) {
                calendarAgent.moveEvent(((GoogleEvent) event).getBacker(), c1, c2);
            }
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
}
