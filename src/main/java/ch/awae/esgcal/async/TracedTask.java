package ch.awae.esgcal.async;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

class TracedTask<V> extends Task<V> {

    private final StackTraceElement[] trace;
    private final Callable<V> task;

    TracedTask(Callable<V> task) {
        this.task = task;
        List<StackTraceElement> elements = new ArrayList<>(Arrays.asList(Thread.currentThread().getStackTrace()));
        elements.remove(0);
        elements.remove(0);
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).getClassName().startsWith("ch.awae.esgcal")) {
                break;
            } else {
                elements.remove(i);
            }
        }
        trace = elements.toArray(new StackTraceElement[0]);
    }

    @Override
    protected V call() throws Exception {
        try {
            return task.call();
        } catch (Exception e) {
            throw new AsyncTaskException(e, trace);
        }
    }

}
