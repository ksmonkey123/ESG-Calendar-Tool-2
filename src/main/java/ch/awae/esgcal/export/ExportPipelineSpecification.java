package ch.awae.esgcal.export;

import ch.awae.esgcal.core.api.ApiException;
import ch.awae.esgcal.core.api.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

interface ExportPipelineSpecification<T> {

    /**
     * fetch the relevant raw data.
     * @param fromDate
     * @param toDate
     */
    Map<ExportCalendar, List<Event>> fetchEvents(LocalDate fromDate, LocalDate toDate) throws ApiException;

    /**
     * extract relevant data from the event. return {@code null} if the event should be ignored.
     */
    T extractData(ExportCalendar exportCalendar, Event event);

    /**
     * filter out data. this method is called after the extracted event has been fragmented into one entry per day.
     */
    default boolean filterData(LocalDate localDate, T data) {
        return true;
    }

    /**
     * merge events that coincide within one day.
     */
    default List<T> mergeEvents(LocalDate localDate, List<T> events) {
        return events;
    }

}
