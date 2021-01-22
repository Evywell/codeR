package fr.evywell.web.network

import fr.evywell.web.handler.HomePageHandler
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.HttpResponseStatus

import io.netty.handler.codec.http.HttpVersion

import io.netty.handler.codec.http.DefaultFullHttpResponse
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class HttpServerHandler(private val router: Router) : SimpleChannelInboundHandler<Any>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is FullHttpRequest) {
            return;
        }

        val request = msg

        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx)
        }

        val method = request.method()
        val uri = request.uri()

        val route = router.match(method, uri)
        if (route === null) {
            // Not Found
            writeNotFound(ctx, request)
            return
        }

        var content = route.handler.handle(request)

        if (content === null) {
            content = ""
        }

        writeResponse(ctx, request, HttpResponseStatus.OK, TYPE_PLAIN, content)
    }

    companion object {
        const val TYPE_PLAIN = "text/plain; charset=UTF-8"
    }

    private fun writeNotFound(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        writeErrorResponse(ctx, request, HttpResponseStatus.NOT_FOUND)
    }

    private fun writeErrorResponse(ctx: ChannelHandlerContext, request: FullHttpRequest, status: HttpResponseStatus) {
        writeResponse(ctx, request, status, TYPE_PLAIN, status.reasonPhrase().toString())
    }

    private fun writeResponse(
        ctx: ChannelHandlerContext,
        request: FullHttpRequest,
        status: HttpResponseStatus,
        contentType: CharSequence,
        content: String
    ) {

        val bytes: ByteArray = content.toByteArray(StandardCharsets.UTF_8)
        val entity = Unpooled.wrappedBuffer(bytes)
        writeHttpResponse(ctx, request, status, entity, contentType, bytes.size)
    }

    private fun writeHttpResponse(
        ctx: ChannelHandlerContext,
        request: FullHttpRequest,
        status: HttpResponseStatus,
        buffer: ByteBuf,
        contentType: CharSequence,
        contentLength: Int
    ) {
        val keepAlive = HttpUtil.isKeepAlive(request)
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buffer, false)

        val dateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.RFC_1123_DATE_TIME

        val headers = response.headers()
        headers.set(HttpHeaderNames.SERVER, "aze")
        headers.set(HttpHeaderNames.DATE, dateTime.format(formatter))
        headers.set(HttpHeaderNames.CONTENT_TYPE, contentType)
        headers.set(HttpHeaderNames.CONTENT_LENGTH, contentLength.toString())

        if (!keepAlive) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        } else {
            ctx.writeAndFlush(response, ctx.voidPromise())
        }
    }

    private fun send100Continue(ctx: ChannelHandlerContext) {
        ctx.write(
            DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.CONTINUE
            )
        )
    }
}
