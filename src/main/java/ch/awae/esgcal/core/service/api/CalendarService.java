package ch.awae.esgcal.core.service.api;

import ch.awae.utils.functional.T2;

import java.util.ArrayList;
import java.util.List;

public interface CalendarService {

    List<Calendar> listCalendars() throws ApiException;

    default List<T2<Calendar, Calendar>> getCalendarPairs(String suffix) throws ApiException {
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
