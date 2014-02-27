package example.service;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;

/**
 * Simple scheduled service, that on each iteration updates some metrics
 * that can be later accessed via JMX.
 */
public final class DefaultTestService extends AbstractScheduledService implements TestService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTestService.class);

    private static final Random RANDOM = new Random();

    private Map<String, Counter> counters;
    private ThreadPoolExecutor executor;
    private Timer timer;

    @Override
    protected void startUp() throws Exception {
        super.startUp();
        counters = Maps.newHashMap();
        counters.put("TestServiceExecutor-0", Metrics.newCounter(DefaultTestService.class, "thread1"));
        counters.put("TestServiceExecutor-1", Metrics.newCounter(DefaultTestService.class, "thread2"));
        counters.put("TestServiceExecutor-2", Metrics.newCounter(DefaultTestService.class, "thread3"));
        counters.put("TestServiceExecutor-3", Metrics.newCounter(DefaultTestService.class, "thread4"));
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("TestServiceExecutor-%d").build();
        executor = new ThreadPoolExecutor(4, 8, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(10));
        executor.setThreadFactory(threadFactory);

        timer = Metrics.newTimer(DefaultTestService.class, "default-timer");
    }

    @Override
    protected void runOneIteration() throws Exception {
        for (int i = 0; i < 3; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
                    LOG.debug("hello from thread");
                    // update counters
                    counters.get(Thread.currentThread().getName()).inc();

                    // update timer
                    final TimerContext context = timer.time();
                    try {
                        final long sleepTime = (RANDOM.nextLong() / Long.MAX_VALUE) * 1000;
                        sleepUninterruptibly(sleepTime, TimeUnit.MILLISECONDS);
                    } finally {
                        context.stop();
                    }
                }
            });
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 2, TimeUnit.SECONDS);
    }

    @Override
    protected void shutDown() {
        executor.shutdown();
    }
}
