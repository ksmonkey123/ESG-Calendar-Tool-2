package ch.awae.esgcal.core.service.api;

import java.time.LocalDateTime;

public interface Event {

    String getTitle();

    LocalDateTime getStart();

    LocalDateTime getEnd();

}
