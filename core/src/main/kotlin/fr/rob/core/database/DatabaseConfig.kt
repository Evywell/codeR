package fr.rob.core.database

data class DatabaseConfig(
    var host: String,
    var port: Long,
    var user: String,
    var password: String,
    var dbname: String
)