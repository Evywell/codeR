package fr.rob.world.service.infra.dependency.characters

import fr.rob.world.service.packages.characters.domain.character.CharacterRepositoryInterface
import fr.rob.world.service.packages.characters.infra.repository.characters.MySQLCharacterRepository
import org.koin.dsl.module

val charactersRepositoryModule = module {
    single<CharacterRepositoryInterface> { MySQLCharacterRepository() }
}
