package example.metrics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public final class MetricsServiceTest {

    private MetricsService service;

    @Before
    public void setUp() {
        service = new DefaultMetricsService();
        service.startAsync().awaitRunning();
    }

    @After
    public void tearDown() {
        service.stopAsync().awaitTerminated();
    }

    @Test
    public void isRunning() {
        assertThat(service.isRunning()).isTrue();
    }
}
