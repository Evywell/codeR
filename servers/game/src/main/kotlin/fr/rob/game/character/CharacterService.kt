package fr.rob.game.character

class CharacterService(
    private val checkCharacterExist: CheckCharacterExistInterface,
    private val fetchCharacter: FetchCharacterInterface,
) {
    /**
     * Blocking database call: run it inside `GameCoroutines.database {}`, never directly on the world thread.
     */
    fun checkCharacterBelongsToAccount(characterId: Int, accountId: Int): Boolean =
        checkCharacterExist.characterExistsForAccount(characterId, accountId)

    /**
     * Blocking database call: run it inside `GameCoroutines.database {}`, never directly on the world thread.
     */
    fun loadFromCharacterIdForAccountId(accountId: Int, characterId: Int): Character? {
        if (!checkCharacterBelongsToAccount(characterId, accountId)) {
            return null
        }

        return fetchCharacter.retrieveCharacter(characterId)
    }
}
