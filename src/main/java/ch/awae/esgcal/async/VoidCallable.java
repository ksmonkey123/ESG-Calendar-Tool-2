package ch.awae.esgcal.async;

@FunctionalInterface
public interface VoidCallable {
    void call() throws Exception;
}
