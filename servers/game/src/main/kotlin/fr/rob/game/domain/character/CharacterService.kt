package fr.rob.game.domain.character

class CharacterService(private val checkCharacterExist: CheckCharacterExistInterface) {
    fun checkCharacterBelongsToAccount(characterId: Int, accountId: Int): Boolean =
        checkCharacterExist.characterExistsForAccount(characterId, accountId)
}
