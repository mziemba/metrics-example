package example.app;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import example.metrics.MetricsModule;
import example.metrics.MetricsService;
import example.service.TestService;
import example.service.TestServiceModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Standard application bound with Guice.
 */
public final class DefaultApp implements App {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultApp.class);

    private static final List<? extends Module> MODULES = Lists.newArrayList(
            new MetricsModule(),
            new TestServiceModule()
    );

    private ServiceManager serviceManager;

    @Override
    public void start(final CountDownLatch stopWaitLatch) {
        guiceStart(stopWaitLatch);
        final Injector injector = Guice.createInjector(MODULES);
        final Set<Service> services = Sets.newHashSet(
                injector.getInstance(MetricsService.class),
                injector.getInstance(TestService.class)
        );
        serviceManager = new ServiceManager(services);
        LOG.debug("starting: {}", serviceManager);
        serviceManager.startAsync().awaitHealthy();
        LOG.debug("started: {}", serviceManager.startupTimes());
    }

    @Override
    public void stop() {
        LOG.debug("stopping {}", serviceManager);
        serviceManager.stopAsync().awaitStopped();
        LOG.debug("stopped {}", serviceManager);
    }

    private void guiceStart(final CountDownLatch stopWaitLatch) {
        // register for CTRL-C
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook Thread") {
            @Override
            public void run() {
                // stop everything
                DefaultApp.this.stop();

                // release main thread
                stopWaitLatch.countDown();
            }
        });
    }
}
