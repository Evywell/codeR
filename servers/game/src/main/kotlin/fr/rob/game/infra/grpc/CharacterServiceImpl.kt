package fr.rob.game.infra.grpc

import fr.rob.game.domain.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.grpc.character.CharacterGrpc.CharacterImplBase
import fr.rob.game.grpc.character.ReservationRequest
import fr.rob.game.grpc.character.ReservationStatus
import io.grpc.stub.StreamObserver

class CharacterServiceImpl(
    private val instanceManager: InstanceManager,
    private val characterWaitingRoom: CharacterWaitingRoom
) : CharacterImplBase() {
    override fun reserve(request: ReservationRequest, responseObserver: StreamObserver<ReservationStatus>) {
        val mapInstance = instanceManager.retrieveByMap(request.mapId, request.zoneId)
        characterWaitingRoom.enter(request.characterId, mapInstance, request.accountId)
    }
}
