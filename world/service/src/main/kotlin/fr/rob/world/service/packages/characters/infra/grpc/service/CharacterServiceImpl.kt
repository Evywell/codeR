package fr.rob.world.service.packages.characters.infra.grpc.service

import fr.rob.world.api.grpc.character.CharacterGrpc.CharacterImplBase
import fr.rob.world.api.grpc.character.CharacterInfo
import fr.rob.world.api.grpc.character.DescribeRequest
import fr.rob.world.api.grpc.character.InstanceInfo
import fr.rob.world.service.packages.characters.app.api.DescribeCharacter
import fr.rob.world.service.packages.characters.domain.character.CharacterId
import io.grpc.Status
import io.grpc.stub.StreamObserver

class CharacterServiceImpl(private val describeCharacterApi: DescribeCharacter) : CharacterImplBase() {
    override fun describe(request: DescribeRequest, responseObserver: StreamObserver<CharacterInfo>) {
        val characterDescriptionResponse = describeCharacterApi.invoke(CharacterId(request.characterId))

        if (characterDescriptionResponse.error != null) {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException())

            return
        }

        val description = characterDescriptionResponse.description!!
        val response = CharacterInfo.newBuilder()
            .setCharacterId(description.id.value)
            .setName(description.name)
            .setInstance(
                InstanceInfo.newBuilder()
                    .setInstanceId(description.instanceId)
                    .setMapId(description.mapId)
                    .setZoneId(description.zoneId)
            )
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
