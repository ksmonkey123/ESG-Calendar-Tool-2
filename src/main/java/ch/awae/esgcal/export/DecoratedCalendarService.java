package ch.awae.esgcal.export;

import ch.awae.esgcal.api.calendar.ApiException;
import ch.awae.esgcal.api.calendar.Calendar;
import ch.awae.esgcal.api.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
class DecoratedCalendarService {

    @Delegate
    private final CalendarService calendarService;

    List<Calendar> getCalendars(ExportCalendar calendar) throws ApiException {
        List<Calendar> result = new ArrayList<>();
        List<String> names = Arrays.asList(calendar.getCalendars());

        for (Calendar cal : calendarService.listCalendars()) {
            if (names.contains(cal.getName())) {
                result.add(cal);
            }
        }

        return result;
    }

}
