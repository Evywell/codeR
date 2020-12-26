package fr.rob.game.domain.server.exception

class SessionNotFoundException(sessionIdentifier: Int) :
    Exception("Cannot retrieve session $sessionIdentifier")