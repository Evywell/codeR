package fr.rob.login.game.character.stand

import fr.rob.entities.CharacterStandProtos

interface CharacterStandRepositoryInterface {

    fun byUserId(userId: Int): List<CharacterStandProtos.CharacterStand.Character>
    fun getCurrentCharacterId(userId: Int): Int
}
