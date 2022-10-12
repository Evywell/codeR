package fr.rob.world.service.infra.dependency

import fr.rob.core.event.EventManager
import fr.rob.core.event.EventManagerInterface
import org.koin.core.qualifier.named
import org.koin.dsl.module

val miscModule = module {
    single<EventManagerInterface>(named("EVENT_MANAGER_DATABASE")) { EventManager() }
}
