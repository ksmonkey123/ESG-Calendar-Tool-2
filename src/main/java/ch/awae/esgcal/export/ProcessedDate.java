package ch.awae.esgcal.export;

import ch.awae.utils.functional.T2;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProcessedDate<T> {

    private final LocalDate date;
    private final List<T> events;

    public static <T> ProcessedDate<T> of(T2<LocalDate, List<T>> tuple) {
        return new ProcessedDate<>(tuple._1, tuple._2);
    }

}
