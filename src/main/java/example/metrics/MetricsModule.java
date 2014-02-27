package example.metrics;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricsRegistry;

import javax.annotation.concurrent.Immutable;

/**
 * Guice module for binding codahale metric stuff.
 */
@Immutable
public final class MetricsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MetricsRegistry.class).toInstance(Metrics.defaultRegistry());

        // binding DefaultMetricsService because Metrics.defaultRegistry() is
        // automatically initialized through static code in Metrics class
        bind(MetricsService.class).to(DefaultMetricsService.class).in(Singleton.class);
    }
}