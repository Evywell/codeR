package fr.rob.game.domain.game.character.stand

import fr.rob.entities.CharacterStandProtos.CharacterStand.Character


interface CharacterStandRepositoryInterface {

    fun byUserId(userId: Int): List<Character>
}