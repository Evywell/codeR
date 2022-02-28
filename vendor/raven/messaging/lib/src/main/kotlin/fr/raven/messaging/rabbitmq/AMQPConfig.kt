package fr.raven.messaging.rabbitmq

data class AMQPConfig(
    val hostname: String,
    val port: Int,
    val username: String,
    val password: String,
    val vhost: String
)
