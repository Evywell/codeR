package fr.rob.e2e

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.net.URLDecoder
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

fun main() {
    val config = OrchestratorConfig.load()
    val resetter = DatabaseResetter(config.jdbcUrl, config.dbUser, config.dbPassword, config.fixturesDir)
    val gameServer = GameServerProcess(config)
    val lock = ReentrantLock()

    Runtime.getRuntime().addShutdownHook(Thread { gameServer.stop() })

    log("Starting game server")
    gameServer.start("boot")

    val server = HttpServer.create(InetSocketAddress("127.0.0.1", config.httpPort), 0)

    server.createContext("/health") { exchange -> exchange.respond(200, "ok") }

    server.createContext("/fixture") { exchange ->
        if (exchange.requestMethod != "POST") {
            exchange.respond(405, "Method not allowed")
            return@createContext
        }

        val fixture = exchange.queryParam("name")
        if (fixture == null) {
            exchange.respond(400, "Missing 'name' query parameter")
            return@createContext
        }

        lock.withLock {
            try {
                resetter.fixtureFile(fixture)
                val start = System.currentTimeMillis()
                log("Reset with fixture '$fixture'")
                gameServer.stop()
                resetter.reset(fixture)
                gameServer.start(fixture)
                log("Reset '$fixture' done in ${System.currentTimeMillis() - start}ms")
                exchange.respond(200, "ok")
            } catch (e: FixtureNotFoundException) {
                exchange.respond(400, e.message ?: "Fixture not found")
            } catch (e: Exception) {
                log("Reset '$fixture' failed: ${e.message}")
                exchange.respond(500, "${e::class.simpleName}: ${e.message}")
            }
        }
    }

    server.start()
    log("E2E orchestrator listening on http://127.0.0.1:${config.httpPort}")
}

private fun HttpExchange.queryParam(name: String): String? =
    requestURI.rawQuery
        ?.split("&")
        ?.map { it.split("=", limit = 2) }
        ?.firstOrNull { it[0] == name }
        ?.getOrNull(1)
        ?.let { URLDecoder.decode(it, Charsets.UTF_8) }

private fun HttpExchange.respond(status: Int, body: String) {
    val bytes = body.toByteArray()
    sendResponseHeaders(status, bytes.size.toLong())
    responseBody.use { it.write(bytes) }
}

private fun log(message: String) = println("[e2e-orchestrator] $message")
