package ch.awae.esgcal.core.export.spreadsheet;

public interface SpreadsheetService {

    Workbook emptyWorkbook() throws SpreadsheetException;

    void saveWorkbook(Workbook workbook, String file) throws SpreadsheetException;

}
