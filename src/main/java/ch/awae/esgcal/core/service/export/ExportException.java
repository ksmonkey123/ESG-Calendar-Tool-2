package ch.awae.esgcal.core.service.export;

public class ExportException extends Exception {

    public ExportException(Throwable cause) {
        super(cause.toString(), cause);
    }

}
