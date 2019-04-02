package ch.awae.esgcal.async;

class AsyncTaskException extends Exception {

    AsyncTaskException(Throwable cause, StackTraceElement[] fakeTrace) {
        super(cause.getMessage(), cause);
        this.setStackTrace(fakeTrace);
    }

}
