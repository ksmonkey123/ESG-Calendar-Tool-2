package ch.awae.esgcal.core.export;

public class ExportException extends Exception {

    public ExportException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
