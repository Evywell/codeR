package fr.rob.login.game.character.stand

import fr.rob.core.network.session.Session
import fr.rob.login.game.character.Character
import fr.rob.login.network.LoginSession
import fr.rob.entities.CharacterStandProtos.CharacterStand as CharacterStand
import fr.rob.entities.CharacterStandProtos.CharacterStand.Character as CharacterStandCharacter

class CharacterStandProcess(private val characterRepository: CharacterStandRepositoryInterface) {

    fun createStandFromSession(session: Session): CharacterStand {
        session as LoginSession

        session.isAuthenticatedOrThrowException()

        val userId = session.userId!!

        val characters = session.characters

        if (characters.isEmpty()) {
            return CharacterStand.newBuilder()
                .setNumCharacters(0)
                .setCurrentCharacterId(0)
                .build()
        }

        val tmpCurrentCharacterId = characterRepository.getCurrentCharacterId(userId)

        val currentCharacterId =
            if (tmpCurrentCharacterId == 0 && characters.isNotEmpty()) characters[0].id else tmpCurrentCharacterId

        return CharacterStand.newBuilder()
            .setNumCharacters(characters.size)
            .addAllCharacters(createStandFromCharacters(characters))
            .setCurrentCharacterId(currentCharacterId!!)
            .build()
    }

    private fun createStandFromCharacters(characters: List<Character>): List<CharacterStandCharacter> {
        val charactersInStand = ArrayList<CharacterStandCharacter>()

        for (character in characters) {
            charactersInStand.add(
                CharacterStandCharacter.newBuilder()
                    .setId(character.id!!)
                    .setName(character.name)
                    .setLevel(character.level!!)
                    .build()
            )
        }

        return charactersInStand
    }
}
