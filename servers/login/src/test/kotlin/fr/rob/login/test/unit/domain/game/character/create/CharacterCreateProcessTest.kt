package fr.rob.login.test.unit.domain.game.character.create

import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.create.CharacterCreateProcess
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_CHARACTER_NAME_ALREADY_TAKEN
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_CHARACTER_NAME_TOO_BIG
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_CHARACTER_NAME_TOO_SMALL
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_INVALID_CHARACTER_NAME
import fr.rob.login.game.character.create.CharacterCreateProcess.Companion.ERR_MAX_CHARACTERS_PER_USER
import fr.rob.login.security.account.Account
import fr.rob.login.test.unit.sandbox.game.character.create.CharacterCreateProcess_CharacterRepository
import fr.rob.login.test.unit.sandbox.game.character.create.CharacterCreateProcess_CharacterRepository2
import fr.rob.login.test.unit.sandbox.game.character.create.CharacterCreateProcess_CharactersHolder
import fr.rob.login.test.unit.sandbox.game.character.create.CharacterCreateProcess_CharactersHolder2
import fr.rob.login.test.unit.sandbox.game.character.create.CharacterCreateProcess_CharactersHolder3
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterCreateProcessTest {

    @Test
    fun `create a character`() {
        // Arrange
        val repository = CharacterCreateProcess_CharacterRepository()
        val process = CharacterCreateProcess(repository)
        val session = LoginSessionFactory.buildAuthenticatedSession()

        session.account = Account(1)

        val characterCreateRequest = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName("Chris")
            .build()

        // Act
        val character = process.create(session.account.id!!, characterCreateRequest)

        // Assert
        assertEquals("Chris", character.name)
        assertEquals(1, character.level)
    }

    @ParameterizedTest
    @ValueSource(strings = ["Chris", "Evywell", "Conan"])
    fun `can create a character is success`(characterName: String) {
        val state = getCanCreateState(characterName)

        assertFalse(state.hasError)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "Chr'is", "Chr1s", "Chrîs", "xXChrisXx123", "16516516", "ê\$aa'\"\\", "its chris"])
    fun `try to create a character with invalid name`(characterName: String) {
        val state = getCanCreateState(characterName)

        // Assert
        assertTrue(state.hasError)
        assertEquals(ERR_INVALID_CHARACTER_NAME, state.error)
    }

    @ParameterizedTest
    @ValueSource(strings = ["c", "Ch"])
    fun `try to create a character with short name`(characterName: String) {
        val state = getCanCreateState(characterName)

        // Assert
        assertTrue(state.hasError)
        assertEquals(ERR_CHARACTER_NAME_TOO_SMALL, state.error)
    }

    @ParameterizedTest
    @ValueSource(strings = ["Christopherdelanotche"])
    fun `try to create a character with long name`(characterName: String) {
        val state = getCanCreateState(characterName)

        // Assert
        assertTrue(state.hasError)
        assertEquals(ERR_CHARACTER_NAME_TOO_BIG, state.error)
    }

    @ParameterizedTest
    @MethodSource("charactersWithNamesAlreadyUsedProvider")
    fun `try to create a character with an already used name`(
        characterName: String,
        charactersHolderVersion: Int,
        repositoryVersion: Int
    ) {
        val state = getCanCreateState(characterName, charactersHolderVersion, repositoryVersion)

        // Assert
        assertTrue(state.hasError)
        assertEquals(ERR_CHARACTER_NAME_ALREADY_TAKEN, state.error)
    }

    fun charactersWithNamesAlreadyUsedProvider(): Stream<Arguments> {
        return Stream.of(
            arguments("Chris", 2, 1),
            arguments("Chris", 1, 2)
        )
    }

    @Test
    fun `try to create a character with already max characters`() {
        val state = getCanCreateState("Chris", 3)

        // Assert
        assertTrue(state.hasError)
        assertEquals(ERR_MAX_CHARACTERS_PER_USER, state.error)
    }

    private fun getCanCreateState(
        characterName: String,
        charactersHolderVersion: Int = 1,
        repositoryVersion: Int = 1
    ): CharacterCreateProcess.CreateCharacterState {
        // Arrange
        val repository =
            when (repositoryVersion) {
                2 -> CharacterCreateProcess_CharacterRepository2()
                else -> {
                    CharacterCreateProcess_CharacterRepository()
                }
            }

        val charactersHolder =
            when (charactersHolderVersion) {
                2 -> CharacterCreateProcess_CharactersHolder2()
                3 -> CharacterCreateProcess_CharactersHolder3()
                else -> {
                    CharacterCreateProcess_CharactersHolder()
                }
            }

        val process = CharacterCreateProcess(repository)

        val characterCreateRequest = CharacterCreateProtos.CharacterCreate.newBuilder()
            .setName(characterName)
            .build()

        // Act
        return process.canCreate(charactersHolder, characterCreateRequest)
    }
}
