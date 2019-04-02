package ch.awae.esgcal.api.export;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExportByYearType {

    ESG("Jahresplan ESG"), BERN("Jahresplan BK"), ZUERICH("Jahresplan ZKP");

    private final String text;

}
