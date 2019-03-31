package ch.awae.esgcal.export;

import ch.awae.utils.functional.T2;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProcessedDate<T> {

    private final LocalDate date;
    private final List<T> events;

    ProcessedDate(T2<LocalDate, List<T>> tuple) {
        this.date = tuple._1;
        this.events = tuple._2;
    }

}
