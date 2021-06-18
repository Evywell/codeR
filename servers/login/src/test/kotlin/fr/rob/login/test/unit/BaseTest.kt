package fr.rob.login.test.unit

import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.game.character.create.CharacterCreateProcess
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.security.strategy.StrategyProcess
import fr.rob.login.test.unit.sandbox.game.account.AccountProcess_AccountRepository
import fr.rob.login.test.unit.sandbox.game.character.stand.CharacterCreateProcess_CharacterRepository
import fr.rob.login.test.unit.sandbox.game.character.stand.CharacterStandProcess_CharacterStandRepository
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
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

        processManager.registerProcess(AccountProcess::class) {
            AccountProcess(AccountProcess_AccountRepository())
        }

        processManager.registerProcess(SessionInitializerProcess::class) {
            SessionInitializerProcess(
                mock(),
                processManager.getOrMakeProcess(AccountProcess::class)
            )
        }

        processManager.registerProcess(StrategyProcess::class) {
            StrategyProcess(server)
        }
    }

    protected fun getSessionInitializerProcessMock(): SessionInitializerProcess {
        val sessionInitializerProcess = mock<SessionInitializerProcess>()
        `when`(sessionInitializerProcess.execute(any(), anyString())).then { }

        return sessionInitializerProcess
    }

    companion object {
        const val ACCOUNT_NAME_1 = "Evywell#1234"
    }
}
