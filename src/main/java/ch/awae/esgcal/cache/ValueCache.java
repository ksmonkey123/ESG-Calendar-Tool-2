package ch.awae.esgcal.cache;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValueCache<T> {

    // configuration
    private final long lifetime;

    // data
    private long lastWritten = 0L;
    private T value;

    public T getOrEvaluate(ValueCacheSupplier<T> supplier) throws Exception {
        if (lastWritten + lifetime > System.currentTimeMillis())
            return value;
        // value not existing or too old => calculate new value
        value = supplier.get();
        lastWritten = System.currentTimeMillis();
        return value;
    }

}
