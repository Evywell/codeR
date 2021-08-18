package fr.rob.core.network.session.exception

class SessionNotFoundException(sessionIdentifier: Int) :
    Exception("Cannot retrieve session $sessionIdentifier")
