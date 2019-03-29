package ch.awae.esgcal.google;

import ch.awae.esgcal.service.CalendarService;
import ch.awae.esgcal.service.EventService;
import ch.awae.utils.functional.T2;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarAgent implements CalendarService, EventService {

    private final AuthenticationService authenticationService;
    private final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private final Throttler throttler;

    private Calendar api = null;

    private synchronized void init() throws GeneralSecurityException, IOException {
        if (api == null) {
            Credential credentials = authenticationService.getCredentials();
            if (credentials == null)
                throw new IllegalStateException("not yet authorized");
            val httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            api = new Calendar.Builder(httpTransport, jsonFactory, credentials).setApplicationName("ESG Calendar Tool").build();
        }
    }

    private List<CalendarListEntry> getCalendarList() throws Exception {
        init();
        return throttler.execute(() -> api.calendarList().list().execute().getItems());
    }

    private List<Event> getEventsOfCalendar(CalendarListEntry calendar, T2<Date, Date> range) throws Exception {
        init();
        val from = range._1;
        val to = range._2;
        assert (from.before(to));

        return throttler.execute(() -> api.events().list(calendar.getId())
                .setTimeMin(new DateTime(from))
                .setTimeMax(new DateTime(to))
                .execute().getItems());
    }

    private void moveEvent(Event event, CalendarListEntry from, CalendarListEntry to) throws Exception {
        init();
        throttler.execute(() -> api.events().move(from.getId(), event.getId(), to.getId()).execute());
    }

    @Override
    public List<ch.awae.esgcal.model.Calendar> listCalendars() throws Exception {
        List<CalendarListEntry> raw = getCalendarList();
        List<ch.awae.esgcal.model.Calendar> result = new ArrayList<>();
        for (CalendarListEntry e : raw) {
            result.add(new GoogleCalendar(e));
        }
        return result;
    }

    @Override
    public List<ch.awae.esgcal.model.Event> listEvents(ch.awae.esgcal.model.Calendar calendar, LocalDate from, LocalDate to) throws Exception {
        val start = java.sql.Date.valueOf(from);
        val end = java.sql.Date.valueOf(to);
        List<Event> events = getEventsOfCalendar(((GoogleCalendar) calendar).getBacker(), T2.of(start, end));
        List<ch.awae.esgcal.model.Event> result = new ArrayList<>();
        for (Event event : events) {
            result.add(new GoogleEvent(event));
        }
        return result;
    }

    @Override
    public void moveEvents(List<ch.awae.esgcal.model.Event> events, ch.awae.esgcal.model.Calendar from, ch.awae.esgcal.model.Calendar to) throws Exception {
        CalendarListEntry c1 = ((GoogleCalendar) from).getBacker();
        CalendarListEntry c2 = ((GoogleCalendar) to).getBacker();
        for (ch.awae.esgcal.model.Event event : events) {
            moveEvent(((GoogleEvent) event).getBacker(), c1, c2);
        }
    }
}
