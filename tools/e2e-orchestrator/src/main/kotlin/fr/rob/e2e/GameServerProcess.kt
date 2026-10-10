package fr.rob.e2e

import java.io.File
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

class GameServerProcess(private val config: OrchestratorConfig) {
    private var process: Process? = null
    private var currentLog: File? = null

    /**
     * @throws GameServerStartException if the server does not open its ports in time
     */
    fun start(logName: String) {
        check(process?.isAlive != true) { "Game server already running" }
        require(config.gameServerBinary.canExecute()) {
            "Game server binary not found: ${config.gameServerBinary.absolutePath} (run :servers:game:installDist)"
        }

        config.logsDir.mkdirs()
        val log = File(config.logsDir, "$logName.log")
        currentLog = log

        val builder = ProcessBuilder(config.gameServerBinary.absolutePath)
            .directory(config.gameServerWorkDir)
            .redirectErrorStream(true)
            .redirectOutput(log)
        builder.environment()["GAME_OPTS"] =
            "-Dmysql_game.host=${config.dbHost} -Dmysql_game.tcp.3306=${config.dbPort}"

        val started = builder.start()
        process = started

        val deadline = System.currentTimeMillis() + config.startupTimeoutMs
        while (!config.gameServerPorts.all(::isPortOpen)) {
            if (!started.isAlive) {
                throw GameServerStartException("Game server exited (code ${started.exitValue()})\n${tailLog()}")
            }
            if (System.currentTimeMillis() > deadline) {
                stop()
                throw GameServerStartException("Game server did not start in time\n${tailLog()}")
            }
            Thread.sleep(POLL_INTERVAL_MS)
        }
    }

    fun stop() {
        val running = process ?: return
        process = null

        val handles = running.descendants().toList() + running.toHandle()
        handles.forEach { it.destroy() }
        if (!running.waitFor(config.shutdownTimeoutMs, TimeUnit.MILLISECONDS)) {
            handles.forEach { it.destroyForcibly() }
            running.waitFor()
        }

        val deadline = System.currentTimeMillis() + config.shutdownTimeoutMs
        while (config.gameServerPorts.any(::isPortOpen) && System.currentTimeMillis() < deadline) {
            Thread.sleep(POLL_INTERVAL_MS)
        }
    }

    fun tailLog(lines: Int = 50): String =
        currentLog?.takeIf { it.exists() }?.readLines()?.takeLast(lines)?.joinToString("\n") ?: ""

    private fun isPortOpen(port: Int): Boolean = try {
        Socket().use { it.connect(InetSocketAddress("127.0.0.1", port), 200) }
        true
    } catch (_: Exception) {
        false
    }

    companion object {
        private const val POLL_INTERVAL_MS = 200L
    }
}

class GameServerStartException(message: String) : RuntimeException(message)
