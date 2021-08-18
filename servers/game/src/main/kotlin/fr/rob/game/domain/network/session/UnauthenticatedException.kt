package fr.rob.game.domain.network.session

import java.lang.RuntimeException

class UnauthenticatedException(message: String = "The session MUST be authenticated") : RuntimeException(message)
