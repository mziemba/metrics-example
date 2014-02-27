package example.metrics;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Singleton;

@Singleton
public final class DefaultMetricsService extends AbstractIdleService implements MetricsService {

    @Override
    protected void startUp() throws Exception {
    }

    @Override
    protected void shutDown() throws Exception {
    }
}
