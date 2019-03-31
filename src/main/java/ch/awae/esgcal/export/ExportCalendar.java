package ch.awae.esgcal.export;

import lombok.Getter;

@Getter
public enum ExportCalendar {

    BERN("Bern", "Berner Kantorei", "Berner Kantorei - Planung"),
    ZUERICH("Zürich", "Zürcher Kantorei", "Zürcher Kantorei - Planung"),
    KONZERTE("Konzerte", "Konzerte", "Konzerte - Planung"),
    ;

    private final String title;
    private final String[] calendars;

    ExportCalendar(String title, String... calendars) {
        this.title = title;
        this.calendars = calendars;
    }

}
