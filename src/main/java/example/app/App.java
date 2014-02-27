package example.app;

import java.util.concurrent.CountDownLatch;

/**
 * Interface of a standard java application.
 */
public interface App {

    /**
     * Starts application.
     *
     * @param stopWaitLatch synchronization aid, used for graceful shutdown
     */
    void start(CountDownLatch stopWaitLatch);

    /**
     * Stops application.
     */
    void stop();
}
