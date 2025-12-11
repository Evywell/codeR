package fr.rob.game.network.grpc

import fr.raven.proto.message.game.grpc.character.CharacterGrpc
import fr.raven.proto.message.game.grpc.character.ReservationRequest
import fr.raven.proto.message.game.grpc.character.ReservationStatus
import fr.rob.game.character.waitingroom.CharacterWaitingRoom
import fr.rob.game.instance.InstanceManager
import io.grpc.stub.StreamObserver

class CharacterServiceImpl(
    private val instanceManager: InstanceManager,
    private val characterWaitingRoom: CharacterWaitingRoom
) : CharacterGrpc.CharacterImplBase() {
    override fun reserve(request: ReservationRequest, responseObserver: StreamObserver<ReservationStatus>) {
        val mapInstance = instanceManager.retrieveByMap(request.mapId, request.zoneId)
        characterWaitingRoom.enter(request.characterId, mapInstance, request.accountId)

        responseObserver.onNext(ReservationStatus.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
