package fr.rob.core.network.v2.netty.protobuf

import com.google.protobuf.MessageLite
import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.netty.NettyChannelInitializer
import fr.rob.core.network.v2.netty.NettyServer

class ProtobufNettyServer<T>(private val prototype: MessageLite, port: Int, private val server: ServerInterface<T>, ssl: Boolean) :
    NettyServer<T>(port, ssl) {
    override fun channelInitializer(): NettyChannelInitializer<T> = ProtobufNettyChannelInitializer(prototype, server, ssl)
}
