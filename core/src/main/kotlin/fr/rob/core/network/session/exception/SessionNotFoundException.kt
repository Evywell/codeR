package fr.rob.core.network.session.exception

class SessionNotFoundException(sessionIdentifier: String) :
    Exception("Cannot retrieve session $sessionIdentifier")
