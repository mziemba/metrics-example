package example;

import example.app.App;
import example.app.DefaultApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("----- starting app -----");
        final App app = new DefaultApp();
        try {
            final CountDownLatch latch = new CountDownLatch(1);
            app.start(latch);
            latch.await();
            app.stop();
        } catch (Exception ex) {
            LOG.error("exception in main()", ex);
            app.stop();
        }
        LOG.info("----- stopping app -----");
    }
}
