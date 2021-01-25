package fr.rob.client.web.handler

import fr.rob.client.web.network.Request
import fr.rob.client.web.network.RouteHandler

class HomePageHandler : RouteHandler() {

    override fun handle(request: Request): String = "Bien le bonjour"
}
