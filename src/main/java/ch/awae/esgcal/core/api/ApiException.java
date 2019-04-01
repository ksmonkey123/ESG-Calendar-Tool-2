package ch.awae.esgcal.core.api;

public class ApiException extends Exception {

    public ApiException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
