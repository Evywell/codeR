package fr.rob.login.game.character.stand

import fr.rob.core.network.session.Session
import fr.rob.entities.CharacterStandProtos

class CharacterStandProcess(private val characterRepository: CharacterStandRepositoryInterface) {

    fun createStandFromSession(session: Session): CharacterStandProtos.CharacterStand {
        session.isAuthenticatedOrThrowException()

        val userId = session.userId

        val characters = characterRepository.byUserId(userId!!)
        val tmpCurrentCharacterId = characterRepository.getCurrentCharacterId(userId)

        val currentCharacterId =
            if (tmpCurrentCharacterId == 0 && characters.isNotEmpty()) characters[0].id else tmpCurrentCharacterId

        return CharacterStandProtos.CharacterStand.newBuilder()
            .setNumCharacters(characters.size)
            .addAllCharacters(characters)
            .setCurrentCharacterId(currentCharacterId)
            .build()
    }
}
