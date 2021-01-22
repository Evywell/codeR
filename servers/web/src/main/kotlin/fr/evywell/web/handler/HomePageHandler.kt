package fr.evywell.web.handler

import fr.evywell.web.network.RouteHandler
import io.netty.handler.codec.http.FullHttpRequest

class HomePageHandler : RouteHandler() {

    override fun handle(request: FullHttpRequest): String  = "Bien le bonjour"
}
