package ch.awae.esgcal.core.api;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface Event {

    String getTitle();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    default boolean isFullDay() {
        long start = getStart().toEpochSecond(ZoneOffset.UTC);
        long end = getEnd().toEpochSecond(ZoneOffset.UTC);
        return (end - start) == 86400;
    }

}
