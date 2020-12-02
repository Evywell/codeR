package fr.rob.game.application.setup.tasks

import fr.rob.core.initiator.TaskInterface
import fr.rob.core.security.PublicKeyReader
import fr.rob.game.ROB_CERTS_API_URL
import fr.rob.game.domain.setup.Setup
import org.codehaus.jackson.map.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

class TaskAuthCollectJWTPublicKey(private var setup: Setup) : TaskInterface {

    override fun run() {
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

        this.setup.setJWTPublicKey(PublicKeyReader.fromString(String(Base64.getDecoder().decode(certs.publicKey))))
    }
}

data class CertsResponse(var publicKey: String = "", var signer: String = "")