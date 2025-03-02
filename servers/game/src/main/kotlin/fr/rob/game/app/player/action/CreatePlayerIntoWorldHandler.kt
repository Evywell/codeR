package fr.rob.game.app.player.action

import fr.rob.game.app.player.message.PlayerDescriptionMessage
import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait
import fr.rob.game.domain.entity.controller.SplineMovementController
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.entity.movement.spline.SplineMovementBrainInterface
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.player.PlayerFactory
import fr.rob.game.domain.world.RandomRollEngine
import java.util.Optional

class CreatePlayerIntoWorldHandler(
    private val playerFactory: PlayerFactory,
    private val objectManager: ObjectManager,
    private val splineMovementBrain: SplineMovementBrainInterface
) {
    fun execute(command: CreatePlayerIntoWorldCommand) {
        val playerGameSession = command.gameSession
        val createPlayerResult = playerFactory.createFromGameSession(
            playerGameSession,
            command.characterId,
        )

        if (!createPlayerResult.isSuccess) {
            return
        }

        val player = createPlayerResult.player!!
        playerGameSession.assignToPlayer(player)

        // @todo remove this
        val worldObject = createMobAroundPosition(ObjectGuid.LowGuid(1u, 1u), Position(10f, 0f, 1f, 0f), command.mapInstance)
        val worldObject2 = createMobAroundPosition(ObjectGuid.LowGuid(1u, 2u), Position(10f, 10f, 1f, 0f), command.mapInstance)

        player.addIntoInstance(command.mapInstance, createPlayerResult.position!!)

        // @todo Send player info
        player.ownerGameSession.send(PlayerDescriptionMessage(player.guid, player.name))

        worldObject.ifPresent {
            // send info to unity + ask to move
            val controller = SplineMovementController(it, splineMovementBrain)
            controller.initiateMovementToPosition(Position(15f, 50f, 1f, 0f))
        }
        worldObject2.ifPresent {
            // send info to unity + ask to move
            val controller = SplineMovementController(it, splineMovementBrain)
            controller.initiateMovementToPosition(Position(15f, 50f, 1f, 0f))
        }
    }

    private fun createMobAroundPosition(lowGuid: ObjectGuid.LowGuid, mobPosition: Position, mapInstance: MapInstance): Optional<WorldObject> {
        val worldObject = objectManager.spawnObject(
            lowGuid,
            mobPosition,
            mapInstance,
        )

        worldObject.ifPresent {
            it.addTrait(ObjectSheetTrait(it, 100, RandomRollEngine()))
            it.addTrait(Movable(it))
        }

        return worldObject
    }
}
