package fr.rob.web.handler

import fr.rob.web.network.Request
import fr.rob.web.network.RouteHandler

class HomePageHandler : RouteHandler() {

    override fun handle(request: Request): String = "Bien le bonjour"
}
