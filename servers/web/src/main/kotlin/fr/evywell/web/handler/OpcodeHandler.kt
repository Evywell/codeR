package fr.evywell.web.handler

import fr.evywell.web.network.RouteHandler
import io.netty.handler.codec.http.FullHttpRequest
import java.nio.charset.StandardCharsets

class OpcodeHandler : RouteHandler() {

    override fun handle(request: FullHttpRequest): String {
        return request.content().toString(StandardCharsets.UTF_8)
    }
}
