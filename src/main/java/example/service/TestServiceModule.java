package example.service;

import com.google.inject.AbstractModule;

public final class TestServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TestService.class).to(DefaultTestService.class);
    }
}
