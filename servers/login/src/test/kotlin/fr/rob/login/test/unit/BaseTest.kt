package fr.rob.login.test.unit

import com.nhaarman.mockitokotlin2.mock
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.game.character.CharacterRepositoryInterface
import fr.rob.login.game.character.create.CharacterCreateProcess
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.test.unit.sandbox.game.character.stand.CharacterCreateProcess_CharacterRepository
import fr.rob.login.test.unit.sandbox.game.character.stand.CharacterStandProcess_CharacterStandRepository
import org.junit.jupiter.api.BeforeEach
import fr.rob.core.test.unit.BaseTest as CoreBaseTest

open class BaseTest : CoreBaseTest() {

    @BeforeEach
    fun setUp() {
        processManager.registerProcess(CharacterCreateProcess::class) {
            CharacterCreateProcess(CharacterCreateProcess_CharacterRepository())
        }

        processManager.registerProcess(AuthenticationProcess::class) {
            DevAuthenticationProcess()
        }

        processManager.registerProcess(CharacterStandProcess::class) {
            CharacterStandProcess(CharacterStandProcess_CharacterStandRepository())
        }

        processManager.registerProcess(SessionInitializerProcess::class) {
            SessionInitializerProcess(mock<CharacterRepositoryInterface>())
        }
    }
}
