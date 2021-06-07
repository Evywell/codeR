package fr.rob.login.game

import fr.rob.core.network.session.Session
import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.login.game.character.CharacterRepositoryInterface
import fr.rob.login.network.LoginSessionData
import fr.rob.login.security.account.AccountProcess

class SessionInitializerProcess(
    private val characterRepository: CharacterRepositoryInterface,
    private val accountProcess: AccountProcess
) {

    fun execute(session: Session) {
        if (!session.isAuthenticated) {
            throw UnauthenticatedSessionException()
        }

        val loginSessionData = LoginSessionData()
        val account = accountProcess.retrieveOrCreate(session.userId!!)

        loginSessionData.account = account

        session.data = loginSessionData

        initSessionCharacters(account.id, session.data as LoginSessionData)
    }

    private fun initSessionCharacters(accountId: Int, sessionData: LoginSessionData) {
        val characters = characterRepository.allByAccountId(accountId)

        sessionData.characters = characters
    }
}
