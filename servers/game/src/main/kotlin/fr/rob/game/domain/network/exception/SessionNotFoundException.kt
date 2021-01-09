package fr.rob.game.domain.network.exception

class SessionNotFoundException(sessionIdentifier: Int) :
    Exception("Cannot retrieve session $sessionIdentifier")