package ch.awae.esgcal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JahresExport {

    ESG("Jahresplan ESG"), BERN("Jahresplan BK"), ZUERICH("Jahresplan ZKP");

    private final String text;

}
