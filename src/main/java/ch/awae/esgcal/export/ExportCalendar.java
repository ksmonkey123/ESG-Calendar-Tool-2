package ch.awae.esgcal.export;

import lombok.Getter;

@Getter
public enum ExportCalendar {

    BERN("Bern", "Berner Kantorei", "Berner Kantorei - Planung"),
    ZUERICH("Zürich", "Zürcher Kantorei", "Zürcher Kantorei - Planung"),
    KONZERTE("Konzerte", "Konzerte", "Konzerte - Planung"),
    GASTVESPERN("Gastvespern", "Gastvespern"),
    FERIEN_BE("Ferien Bern", "Ferien Bern"),
    FERIEN_ZH("Ferien Zürich", "Ferien Zürich"),
    FERIEN_ML("Ferien Musikalischer Leiter", "Ferien Musikalischer Leiter"),
    ABWESENHEITEN_ML("Abwesenheiten Musikalischer Leiter", "Abwesenheiten Musikalischer Leiter");

    private final String title;
    private final String[] calendars;

    ExportCalendar(String title, String... calendars) {
        this.title = title;
        this.calendars = calendars;
    }

}
