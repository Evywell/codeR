package fr.rob.game.domain.game.character.stand

import fr.rob.core.network.session.Session
import fr.rob.entities.CharacterStandProtos.CharacterStand

class CharacterStandProcess(private val characterRepository: CharacterStandRepositoryInterface) {

    fun createStandFromSession(session: Session): CharacterStand {
        session.isAuthenticatedOrThrowException()

        val userId = session.userId

        val characters = characterRepository.byUserId(userId!!)
        val currentCharacterId = characterRepository.getCurrentCharacterId(userId)

        return CharacterStand
            .newBuilder()
            .setNumCharacters(characters.size)
            .addAllCharacters(characters)
            .setCurrentCharacterId(currentCharacterId)
            .build()
    }
}
