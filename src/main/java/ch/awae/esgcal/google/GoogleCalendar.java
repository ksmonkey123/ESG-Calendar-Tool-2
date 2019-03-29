package ch.awae.esgcal.google;

import ch.awae.esgcal.model.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class GoogleCalendar implements Calendar {

    @Getter(AccessLevel.PACKAGE)
    private final CalendarListEntry backer;

    @Override
    public String getName() {
        return backer.getSummary();
    }

}
