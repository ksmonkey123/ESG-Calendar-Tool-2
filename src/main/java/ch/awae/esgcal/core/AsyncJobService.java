package ch.awae.esgcal.core;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AsyncJobService {

    private ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public void schedule(Runnable job) {
        threadPool.submit(job);
    }

}
