package ch.awae.esgcal.cache;

@FunctionalInterface
public interface ValueCacheSupplier<T> {

    T get() throws Exception;

}
