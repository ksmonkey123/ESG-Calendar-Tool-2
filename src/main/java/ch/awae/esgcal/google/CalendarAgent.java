package ch.awae.esgcal.google;

import ch.awae.utils.functional.T2;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

@Log
@Service
@RequiredArgsConstructor
class CalendarAgent {

    private final AuthorizationService authorizationService;
    private final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private final Throttler throttler;

    private Calendar api = null;

    private void verifyInitialized() throws GeneralSecurityException, IOException {
        if (api == null) {
            initialize();
        }
    }

    private synchronized void initialize() throws GeneralSecurityException, IOException {
        log.info("initializing API");
        Credential credentials = authorizationService.getCredentials();
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        api = new Calendar.Builder(httpTransport, jsonFactory, credentials).setApplicationName("ESG Calendar Tool").build();
    }

    List<CalendarListEntry> getCalendarList() throws Exception {
        verifyInitialized();
        log.info("request: list calendars");
        return throttler.execute(() -> api.calendarList().list().execute().getItems());
    }

    List<Event> getEventsOfCalendar(CalendarListEntry calendar, T2<Date, Date> range) throws Exception {
        verifyInitialized();
        val from = range._1;
        val to = range._2;
        assert (from.before(to));

        log.info("request: events in calendar " + calendar.getId());
        return throttler.execute(() -> api.events().list(calendar.getId())
                .setTimeMin(new DateTime(from))
                .setTimeMax(new DateTime(to))
                .execute().getItems());
    }

    void moveEvent(Event event, CalendarListEntry from, CalendarListEntry to) throws Exception {
        verifyInitialized();
        log.info("request: move event " + event.getId() + " (" + from.getId() + " -> " + to.getId() + ")");
        throttler.execute(() -> api.events().move(from.getId(), event.getId(), to.getId()).execute());
    }
}
