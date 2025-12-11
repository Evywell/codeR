package fr.rob.game.character

interface CheckCharacterExistInterface {
    fun characterExistsForAccount(characterId: Int, accountId: Int): Boolean
}
