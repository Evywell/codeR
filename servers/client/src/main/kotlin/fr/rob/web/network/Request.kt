package fr.rob.web.network

import io.netty.handler.codec.http.FullHttpRequest
import io.netty.util.CharsetUtil

import io.netty.buffer.ByteBuf




class Request(val request: FullHttpRequest) {

    var routeParameters: RouteParameters? = null
    var body = parseJsonRequest()

    private fun parseJsonRequest(): String? {
        val jsonBuf = request.content()
        return jsonBuf.toString(CharsetUtil.UTF_8)
    }
}