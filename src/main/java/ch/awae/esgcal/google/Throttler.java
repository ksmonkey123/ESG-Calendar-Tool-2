package ch.awae.esgcal.google;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Throttler {

    private final ReentrantLock lock = new ReentrantLock(true);
    // milliseconds between calls
    private final int delay;
    private long lastCall = 0L;

    @Autowired
    public Throttler(@Value("${calendar.api.throttle}") int callsPerSecond) {
        this.delay = 1000 / callsPerSecond;
    }

    /**
     * Evaluates and the body code block only once the throttler is ready
     * to accept another call
     */
    <T> T execute(Callable<T> code) throws Exception {
        lock.lock();
        val myEntry = lastCall + delay;
        while (myEntry > System.currentTimeMillis()) {
            Thread.sleep(0);
        }
        lastCall = System.currentTimeMillis();
        lock.unlock();
        return code.call();

    }

}