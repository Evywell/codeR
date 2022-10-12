package fr.rob.world.service.infra.dependency.characters

import fr.rob.world.service.packages.characters.app.api.DescribeCharacter
import org.koin.dsl.module

val charactersApiModule = module {
    single { DescribeCharacter(get()) }
}
