package fr.rob.login.game.character.stand

interface CharacterStandRepositoryInterface {

    fun getCurrentCharacterId(accountId: Int): Int
}
