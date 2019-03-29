package ch.awae.esgcal.service.datasource;

import ch.awae.esgcal.model.Calendar;
import ch.awae.utils.functional.T2;

import java.util.ArrayList;
import java.util.List;

public interface CalendarService {

    List<Calendar> listCalendars() throws Exception;

    default List<T2<Calendar, Calendar>> getCalendarPairs(String suffix) throws Exception {
        List<T2<Calendar, Calendar>> results = new ArrayList<>();

        List<Calendar> raw = listCalendars();

        for (Calendar c1 : raw) {
            for (Calendar c2 : raw) {
                if ((c1.getName() + suffix).equals(c2.getName()))
                    results.add(T2.of(c1, c2));
            }
        }

        return results;
    }

}
