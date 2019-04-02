package ch.awae.esgcal.api.spreadsheet;

public interface SpreadsheetService {

    Workbook emptyWorkbook() throws SpreadsheetException;

    void saveWorkbook(Workbook workbook, String file) throws SpreadsheetException;

}
