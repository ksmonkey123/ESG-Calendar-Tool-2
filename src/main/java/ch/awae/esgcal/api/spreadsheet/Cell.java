package ch.awae.esgcal.api.spreadsheet;

import java.time.LocalDate;

public interface Cell {

    void write(String value);

    void write(LocalDate date);

    void write(int dayOfMonth);
}
