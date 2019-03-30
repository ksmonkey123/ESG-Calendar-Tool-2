package ch.awae.esgcal.core.service.api;

public class ApiException extends Exception {

    public ApiException(Throwable cause) {
        super(cause.toString(), cause);
    }

}
