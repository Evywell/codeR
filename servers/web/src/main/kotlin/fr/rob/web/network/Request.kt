package fr.rob.web.network

import io.netty.handler.codec.http.FullHttpRequest
import io.netty.util.CharsetUtil

class Request(val request: FullHttpRequest) {

    var routeParameters: RouteParameters? = null
    var body = parseJsonRequest()

    private fun parseJsonRequest(): String? {
        return request.content().toString(CharsetUtil.UTF_8)
    }
}
