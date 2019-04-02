package ch.awae.esgcal.google;

import ch.awae.esgcal.PostConstructBean;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

@Log
@Component
class Throttler implements PostConstructBean {

    private final ReentrantLock lock = new ReentrantLock(true);
    // milliseconds between calls
    private int delay;
    private long lastCall = 0L;

    @Override
    public void postContruct(ApplicationContext context) {
        this.delay = 1000 / context.getEnvironment().getRequiredProperty("google.api.throttle", int.class);
    }

    /**
     * Evaluates and the body code block only once the throttler is ready
     * to accept another call
     */
    <T> T execute(Callable<T> code) throws Exception {
        lock.lock();
        long myEntry = lastCall + delay;
        while (myEntry > System.currentTimeMillis()) {
            Thread.sleep(0);
        }
        lastCall = System.currentTimeMillis();
        lock.unlock();
        return code.call();

    }

}