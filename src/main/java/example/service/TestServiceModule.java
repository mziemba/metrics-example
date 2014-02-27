package example.service;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class TestServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TestService.class).to(DefaultTestService.class).in(Singleton.class);
    }
}
