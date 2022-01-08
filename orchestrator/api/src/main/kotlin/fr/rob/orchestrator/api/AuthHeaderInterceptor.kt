package fr.rob.orchestrator.api

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor

class AuthHeaderInterceptor : ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {

        val keys = headers.keys()

        for (keyString in keys) {
            val key = Metadata.Key.of(keyString, Metadata.ASCII_STRING_MARSHALLER)

            println("$keyString : ${headers.get(key)}")
        }

        return next.startCall(call, headers)
    }
}
