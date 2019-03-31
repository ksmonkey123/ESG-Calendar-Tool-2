package ch.awae.esgcal.export;

import ch.awae.utils.functional.T2;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
class ProcessedEvent<T> {

    private final LocalDate date;
    private final List<T> events;

    ProcessedEvent(T2<LocalDate, List<T>> tuple) {
        this.date = tuple._1;
        this.events = tuple._2;
    }

}
