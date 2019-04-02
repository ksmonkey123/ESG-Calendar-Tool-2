package ch.awae.esgcal.api.export;

public class ExportException extends Exception {

    public ExportException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
