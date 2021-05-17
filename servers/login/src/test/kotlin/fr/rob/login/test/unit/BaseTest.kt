package fr.rob.login.test.unit

import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.test.unit.sandbox.game.character.stand.CharacterStandProcess_CharacterStandRepository
import org.junit.Before
import fr.rob.core.test.unit.BaseTest as CoreBaseTest

open class BaseTest: CoreBaseTest() {

    @Before
    fun setUp() {
        processManager.registerProcess(AuthenticationProcess::class) {
            DevAuthenticationProcess()
        }

        processManager.registerProcess(CharacterStandProcess::class) {
            CharacterStandProcess(CharacterStandProcess_CharacterStandRepository())
        }
    }
}
