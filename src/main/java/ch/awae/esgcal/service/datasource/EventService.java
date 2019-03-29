package ch.awae.esgcal.service.datasource;

import ch.awae.esgcal.model.Calendar;
import ch.awae.esgcal.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<Event> listEvents(Calendar calendar, LocalDate from, LocalDate to) throws Exception;

    void moveEvents(List<Event> events, Calendar from, Calendar to) throws Exception;

}
