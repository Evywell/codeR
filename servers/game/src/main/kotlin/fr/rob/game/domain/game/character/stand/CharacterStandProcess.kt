package fr.rob.game.domain.game.character.stand

import fr.rob.entities.CharacterStandProtos.CharacterStand
import fr.rob.game.domain.network.session.Session

class CharacterStandProcess(private val characterRepository: CharacterStandRepositoryInterface) {

    fun createStandFromSession(session: Session): CharacterStand {
        session.isAuthenticatedOrThrowException()

        val userId = session.userId

        val characters = characterRepository.byUserId(userId!!)

        return CharacterStand
            .newBuilder()
            .setNumCharacters(characters.size)
            .addAllCharacters(characters)
            .build()
    }
}