package fr.rob.login.game

import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.login.game.character.CharacterRepositoryInterface
import fr.rob.login.network.LoginSession
import fr.rob.login.security.account.AccountProcess

class SessionInitializerProcess(
    private val characterRepository: CharacterRepositoryInterface,
    private val accountProcess: AccountProcess
) {

    fun execute(session: LoginSession, accountName: String) {
        if (!session.isAuthenticated) {
            throw UnauthenticatedSessionException()
        }

        val account = accountProcess.retrieveOrCreate(session.userId!!, accountName)

        session.account = account

        initSessionCharacters(account.id, session)
    }

    private fun initSessionCharacters(accountId: Int, session: LoginSession) {
        val characters = characterRepository.allByAccountId(accountId)

        session.characters = characters
    }
}
