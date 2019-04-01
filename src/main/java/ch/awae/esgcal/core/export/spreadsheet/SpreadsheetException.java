package ch.awae.esgcal.core.export.spreadsheet;

public class SpreadsheetException extends Exception {
    public SpreadsheetException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
