package ch.awae.esgcal.google;

import ch.awae.esgcal.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class GoogleEvent implements Event {

    @Getter(AccessLevel.PACKAGE)
    private final com.google.api.services.calendar.model.Event backer;

    @Override
    public String getTitle() {
        return backer.getSummary();
    }

    private ZonedDateTime extractDateTimeFromEvent(EventDateTime date) {
        val d = (date.getDate() == null) ? date.getDateTime() :  date.getDate();
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(d.getValue());
        return cal.getTime().toInstant().atZone(d.isDateOnly() ? ZoneId.of("GMT") : ZoneId.systemDefault());
    }

    @Override
    public LocalDateTime getStart() {
        return extractDateTimeFromEvent(backer.getStart()).toLocalDateTime();
    }

    @Override
    public LocalDateTime getEnd() {
        return extractDateTimeFromEvent(backer.getEnd()).toLocalDateTime();
    }

}
