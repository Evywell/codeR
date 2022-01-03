package fr.rob.orchestrator.api.composer

import fr.rob.orchestrator.api.instance.Instance
import fr.rob.orchestrator.api.node.Node
import java.util.UUID
import kotlin.collections.HashMap

class RequestComposer {

    private val tracks = HashMap<String, TrackRequest>()

    fun create(instance: Instance, node: Node): CreateInstanceRequest {
        val track = generateTrack(instance)
        tracks[track.uuid] = track

        return CreateInstanceRequest(track.uuid, instance, node)
    }

    private fun generateTrack(instance: Instance): TrackRequest =
        TrackRequest(UUID.randomUUID().toString(), instance)

    data class TrackRequest(val uuid: String, val instance: Instance)
}
