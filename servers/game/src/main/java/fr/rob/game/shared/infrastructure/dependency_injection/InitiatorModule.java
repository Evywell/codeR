package fr.rob.game.shared.infrastructure.dependency_injection;

import com.google.inject.AbstractModule;
import fr.rob.game.setup.application.AppSetup;
import fr.rob.game.setup.domain.Setup;

public class InitiatorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Setup.class)
            .toInstance(new AppSetup());
    }
}
