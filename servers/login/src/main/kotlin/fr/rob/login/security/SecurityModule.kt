package fr.rob.login.security

import fr.rob.core.AbstractModule
import fr.rob.core.ENV_DEV
import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.core.network.Server
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.network.netty.plugin.security.RequestLimiter
import fr.rob.core.network.netty.plugin.security.rule.KickLimitRule
import fr.rob.core.process.ProcessManager
import fr.rob.core.security.PublicKeyReader
import fr.rob.core.security.SecurityBanProcess
import fr.rob.login.ROB_CERTS_API_URL
import fr.rob.login.opcode.ClientOpcodeLogin.Companion.OPERATOR_CHANGE_STRATEGY
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.shared.network.netty.security.rule.CheckOperatorRule
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import org.codehaus.jackson.map.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.PublicKey
import java.util.Base64

class SecurityModule(
    private val env: String,
    private val processManager: ProcessManager,
    private val server: Server
) : AbstractModule() {

    override fun boot() {
        // Registering the authentication process
        registerAuthenticationProcess()

        if (server is NettyServer) {
            server.registerPlugin(getRequestLimiterPlugin())
        }
    }

    private fun getRequestLimiterPlugin(): RequestLimiter = RequestLimiter()
        .rule(
            CheckOperatorRule(
                arrayOf(
                    OPERATOR_CHANGE_STRATEGY
                )
            )
        )
        .rule(KickLimitRule(3, processManager.getOrMakeProcess(SecurityBanProcess::class))) as RequestLimiter

    private fun registerAuthenticationProcess() {
        val accountProcess = processManager.getOrMakeProcess(AccountProcess::class)

        if (env == ENV_DEV) {
            processManager.registerProcess(AuthenticationProcess::class) { DevAuthenticationProcess(accountProcess) }
        } else {
            processManager.registerProcess(AuthenticationProcess::class) {
                val jwtService = JWTDecoderService(getJWTPublicKey(), JacksonDeserializer())

                JWTAuthenticationProcess(jwtService, accountProcess)
            }
        }
    }

    private fun getJWTPublicKey(): PublicKey {
        val client: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build()

        val certsRequest: HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(ROB_CERTS_API_URL))
            .GET()
            .build()

        val certsResponse: HttpResponse<String> = client.send(certsRequest, HttpResponse.BodyHandlers.ofString())

        val jsonResponse: String = certsResponse.body()
        val mapper = ObjectMapper()

        val certs = mapper.readValue(jsonResponse, CertsResponse::class.java)

        return PublicKeyReader.fromString(String(Base64.getDecoder().decode(certs.publicKey)))
    }
}

data class CertsResponse(var publicKey: String = "", var signer: String = "")
