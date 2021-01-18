package fr.rob.game.infrastructure.config.database

data class DatabaseConfig(
    val host: String,
    val port: Long,
    val user: String,
    val password: String,
    val dbname: String
)
