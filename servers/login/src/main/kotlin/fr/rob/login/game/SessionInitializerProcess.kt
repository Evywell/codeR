package fr.rob.login.game

import fr.rob.core.network.session.Session
import fr.rob.core.network.session.exception.UnauthenticatedSessionException
import fr.rob.login.game.character.CharacterRepositoryInterface
import fr.rob.login.network.LoginSessionData

class SessionInitializerProcess(private val characterRepository: CharacterRepositoryInterface) {

    fun execute(session: Session) {
        if (!session.isAuthenticated) {
            throw UnauthenticatedSessionException()
        }

        session.data = LoginSessionData()

        initSessionCharacters(session.userId!!, session.data as LoginSessionData)
    }

    private fun initSessionCharacters(userId: Int, sessionData: LoginSessionData) {
        val characters = characterRepository.allByUserId(userId)

        sessionData.characters = characters
    }
}
