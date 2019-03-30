package ch.awae.esgcal.core.export;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExportByDateType {

    BERN("Probenplan BK"),
    ZUERICH("Probenplan ZKP"),
    GANZTAG("Ganztägige Termine");

    private final String text;

}
