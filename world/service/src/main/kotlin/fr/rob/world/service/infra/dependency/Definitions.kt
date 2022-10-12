package fr.rob.world.service.infra.dependency

import fr.rob.world.service.infra.dependency.characters.charactersApiModule
import fr.rob.world.service.infra.dependency.characters.charactersRepositoryModule

val modules = arrayOf(
    miscModule,
    databaseModule,
    // Characters Package
    charactersRepositoryModule,
    charactersApiModule
)
