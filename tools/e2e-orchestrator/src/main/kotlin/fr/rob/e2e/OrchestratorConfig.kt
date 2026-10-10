package fr.rob.e2e

import java.io.File

data class OrchestratorConfig(
    val httpPort: Int,
    val dbHost: String,
    val dbPort: Int,
    val dbUser: String,
    val dbPassword: String,
    val dbName: String,
    val fixturesDir: File,
    val gameServerBinary: File,
    val gameServerWorkDir: File,
    val gameServerPorts: List<Int>,
    val logsDir: File,
    val startupTimeoutMs: Long,
    val shutdownTimeoutMs: Long,
) {
    val jdbcUrl: String
        get() = "jdbc:mysql://$dbHost:$dbPort/$dbName?allowMultiQueries=false&useSSL=false&allowPublicKeyRetrieval=true"

    companion object {
        /**
         * Reads configuration from system properties first, then environment variables (upper snake case).
         */
        fun load(): OrchestratorConfig = OrchestratorConfig(
            httpPort = read("e2e.http.port", "18080").toInt(),
            dbHost = read("e2e.db.host", "127.0.0.1"),
            dbPort = read("e2e.db.port", "33062").toInt(),
            dbUser = read("e2e.db.user", "dev"),
            dbPassword = read("e2e.db.password", "secret"),
            dbName = read("e2e.db.name", "coder"),
            fixturesDir = File(read("e2e.fixtures.dir", "fixtures/e2e")),
            gameServerBinary = File(read("e2e.game.binary", "servers/game/build/install/game/bin/game")),
            gameServerWorkDir = File(read("e2e.game.workdir", "servers/game/src/main/resources")),
            gameServerPorts = read("e2e.game.ports", "22222,12347").split(",").map { it.trim().toInt() },
            logsDir = File(read("e2e.logs.dir", "build/e2e-logs")),
            startupTimeoutMs = read("e2e.game.startup.timeout.ms", "60000").toLong(),
            shutdownTimeoutMs = read("e2e.game.shutdown.timeout.ms", "10000").toLong(),
        )

        private fun read(key: String, default: String): String =
            System.getProperty(key)
                ?: System.getenv(key.uppercase().replace('.', '_'))
                ?: default
    }
}
