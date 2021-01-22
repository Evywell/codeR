package fr.evywell.web.network

import io.netty.handler.codec.http.FullHttpRequest

abstract class RouteHandler {

    abstract fun handle(request: FullHttpRequest): String?
}
