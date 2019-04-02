package ch.awae.esgcal.api.spreadsheet;

public class SpreadsheetException extends Exception {
    public SpreadsheetException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
