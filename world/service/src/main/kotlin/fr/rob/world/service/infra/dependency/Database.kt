package fr.rob.world.service.infra.dependency

import fr.rob.core.database.ConnectionManager
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {
    single { ConnectionManager(get(qualifier = named("EVENT_MANAGER_DATABASE"))) }
}
