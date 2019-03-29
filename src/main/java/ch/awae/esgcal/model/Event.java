package ch.awae.esgcal.model;

import java.time.LocalDateTime;

public interface Event {

    String getTitle();

    LocalDateTime getStart();

    LocalDateTime getEnd();

}
