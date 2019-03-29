package ch.awae.esgcal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateExport {

    BERN("Probenplan BK"),
    ZUERICH("Probenplan ZKP"),
    GANZTAG("Ganztägige Termine");

    private final String text;

}
