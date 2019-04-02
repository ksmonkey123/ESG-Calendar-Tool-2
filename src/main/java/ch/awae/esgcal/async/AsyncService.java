package ch.awae.esgcal.async;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Log
@Service
public class AsyncService {

    private ExecutorService threadPool = Executors.newFixedThreadPool(1);

    private final AtomicInteger index = new AtomicInteger(1);

    public <T> void schedule(Callable<T> callable, Consumer<T> onSuccess, Consumer<Throwable> onFailure) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        String origin = caller.getClassName() + "#" + caller.getMethodName();
        String index = String.format("%04d", this.index.getAndIncrement());

        log.info("scheduling task #" + index + " from " + origin);
        TracedTask<T> task = new TracedTask<>(() -> {
            log.info("starting task #" + index);
            return callable.call();
        });
        task.setOnSucceeded(event -> {
            log.info("task #" + index + " completed");
            //noinspection unchecked
            onSuccess.accept((T) event.getSource().getValue());
        });
        task.setOnFailed(event -> {
            log.info("task #" + index + " failed");
            onFailure.accept(event.getSource().getException());
        });
        threadPool.submit(task);
    }

    public void schedule(VoidCallable callable, Runnable onSuccess, Consumer<Throwable> onFailure) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        String origin = caller.getClassName() + "#" + caller.getMethodName();
        String index = String.format("%04d", this.index.getAndIncrement());

        log.info("scheduling task #" + index + " from " + origin);
        TracedTask<Object> task = new TracedTask<>(() -> {
            log.info("starting task #" + index);
            callable.call();
            return null;
        });
        task.setOnSucceeded(event -> {
            log.info("task #" + index + " completed");
            onSuccess.run();
        });
        task.setOnFailed(event -> {
            log.warning("task #" + index + " failed");
            onFailure.accept(event.getSource().getException());
        });
        threadPool.submit(task);
    }

}
