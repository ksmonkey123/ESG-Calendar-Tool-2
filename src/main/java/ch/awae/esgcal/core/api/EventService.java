package ch.awae.esgcal.core.api;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<Event> listEvents(Calendar calendar, LocalDate from, LocalDate to) throws ApiException;

    void moveEvents(List<Event> events, Calendar from, Calendar to) throws ApiException;

}
