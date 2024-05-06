package fr.rob.game.domain.entity.movement.spline

import fr.raven.proto.message.physicbridge.PhysicProto
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.infra.network.physic.PhysicObjectInteraction
import fr.rob.game.infra.network.physic.unity.UnityIntegration

class UnitySplineMovementGenerator(
    private val physicClientIntegration: UnityIntegration,
    private val physicObjectInteraction: PhysicObjectInteraction,
) : SplineMovementGeneratorInterface {
    override fun generateForFinalPosition(source: WorldObject, position: Position): SplineMovement {
        val sourceGuidRawValue = source.guid.getRawValue()

        physicClientIntegration.send(
            PhysicProto.Packet.newBuilder()
                .setOpcode(1)
                .setBody(
                    PhysicProto.SpawnRequest.newBuilder()
                        .setGuid(sourceGuidRawValue)
                        .setPosition(
                            PhysicProto.Position.newBuilder()
                                .setPosX(source.position.x)
                                .setPosY(source.position.y)
                                .setPosZ(source.position.z)
                                .setOrientation(source.position.orientation),
                        )
                        .build()
                        .toByteString(),
                )
                .build(),
        )

        physicClientIntegration.send(
            PhysicProto.Packet.newBuilder()
                .setOpcode(2)
                .setBody(
                    PhysicProto.MoveToRequest.newBuilder()
                        .setGuid(sourceGuidRawValue)
                        .setPosition(
                            PhysicProto.Position.newBuilder()
                                .setPosX(position.x)
                                .setPosY(position.y)
                                .setPosZ(position.z)
                                .setOrientation(position.orientation),
                        )
                        .build()
                        .toByteString(),
                )
                .build(),
        )

        val splineMovement = SplineMovement()
        physicObjectInteraction.movingObjects[sourceGuidRawValue] = splineMovement

        return splineMovement
    }
}
