package ch.awae.esgcal.api.calendar;

public class ApiException extends Exception {

    public ApiException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
