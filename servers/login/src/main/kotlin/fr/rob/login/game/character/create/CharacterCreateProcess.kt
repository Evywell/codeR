package fr.rob.login.game.character.create

import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.Character
import fr.rob.login.game.character.CharacterRepositoryInterface
import fr.rob.login.game.character.CharactersHolderInterface

class CharacterCreateProcess(private val characterRepository: CharacterRepositoryInterface) {

    /**
     * Rules are:
     *  1. A name with MIN_CHARACTER_NAME_CHARS to MAX_CHARACTER_NAME_CHARS characters
     *  2. A maximum of MAX_CHARACTERS_PER_USER characters per user
     *  3. A unique character name
     *  4. A name without special characters
     */
    fun canCreate(
        charactersHolder: CharactersHolderInterface,
        characterSkeleton: CharacterCreateProtos.CharacterCreate
    ): CreateCharacterState {
        val characterName = characterSkeleton.name
        val characterNameLength = characterName.length

        if (!characterName.matches(Regex(NAME_REGEX, RegexOption.IGNORE_CASE))) {
            return CreateCharacterState(true, ERR_INVALID_CHARACTER_NAME)
        }

        val isCharacterNameTooSmall = isCharacterNameTooSmall(characterNameLength)
        val isCharacterNameTooBig = isCharacterNameTooBig(characterNameLength)

        if (isCharacterNameTooSmall || isCharacterNameTooBig) {
            return CreateCharacterState(
                true,
                if (isCharacterNameTooSmall) ERR_CHARACTER_NAME_TOO_SMALL else ERR_CHARACTER_NAME_TOO_BIG
            )
        }

        if (charactersHolder.getCharactersNumber() >= MAX_CHARACTERS_PER_USER) {
            return CreateCharacterState(true, ERR_MAX_CHARACTERS_PER_USER)
        }

        // We first check his own characters to avoid a useless query
        if (
            charactersHolder.getCharacterByName(characterName) is Character
            || characterRepository.isCharacterNameTaken(characterName)
        ) {
            return CreateCharacterState(true, ERR_CHARACTER_NAME_ALREADY_TAKEN)
        }

        return CreateCharacterState(false)
    }

    /**
     * No checks are made here, please use `canCreate` method
     */
    fun create(accountId: Int, characterSkeleton: CharacterCreateProtos.CharacterCreate): Character {
        val character = characterRepository.insert(accountId, characterSkeleton)

        // Define the new character as the current one
        characterRepository.setCurrentCharacter(character)

        return character
    }

    private fun isCharacterNameTooSmall(characterNameLength: Int): Boolean =
        characterNameLength < MIN_CHARACTER_NAME_CHARS

    private fun isCharacterNameTooBig(characterNameLength: Int): Boolean =
        characterNameLength > MAX_CHARACTER_NAME_CHARS

    companion object {
        const val MAX_CHARACTERS_PER_USER = 10
        const val MIN_CHARACTER_NAME_CHARS = 3
        const val MAX_CHARACTER_NAME_CHARS = 15

        const val ERR_CHARACTER_NAME_TOO_SMALL = "err_character_name_too_small"
        const val ERR_CHARACTER_NAME_TOO_BIG = "err_character_name_too_big"
        const val ERR_MAX_CHARACTERS_PER_USER = "err_max_characters_per_user"
        const val ERR_CHARACTER_NAME_ALREADY_TAKEN = "err_character_name_already_taken"
        const val ERR_INVALID_CHARACTER_NAME = "err_invalid_character_name"

        private const val NAME_REGEX = "^[a-z]+$"
    }

    data class CreateCharacterState(var hasError: Boolean, var error: String? = null)
}
