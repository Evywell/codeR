package fr.rob.game.domain.character

interface CheckCharacterExistInterface {
    fun characterExistsForAccount(characterId: Int, accountId: Int): Boolean
}
