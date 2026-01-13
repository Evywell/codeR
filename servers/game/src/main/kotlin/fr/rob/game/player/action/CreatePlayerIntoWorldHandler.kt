package fr.rob.game.player.action

import fr.rob.game.behavior.MovableBehavior
import fr.rob.game.behavior.ObjectSheetBehavior
import fr.rob.game.component.MovementComponent
import fr.rob.game.component.resource.HealthComponent
import fr.rob.game.player.message.PlayerDescriptionMessage
import fr.rob.game.entity.ObjectManager
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.controller.SplineMovementController
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.movement.spline.SplineMovementBrainInterface
import fr.rob.game.instance.MapInstance
import fr.rob.game.player.PlayerFactory
import fr.rob.game.world.RandomRollEngine

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

        worldObject?.let {
            // send info to unity + ask to move
            val controller = SplineMovementController(it, splineMovementBrain)
            controller.initiateMovementToPosition(Position(10f, 10f, 1f, 0f))
        }
        worldObject2?.let {
            // send info to unity + ask to move
            val controller = SplineMovementController(it, splineMovementBrain)
            controller.initiateMovementToPosition(Position(10f, 10f, 1f, 0f))
        }
    }

    private fun createMobAroundPosition(lowGuid: ObjectGuid.LowGuid, mobPosition: Position, mapInstance: MapInstance): WorldObject? {
        val worldObject = objectManager.spawnObject(
            lowGuid,
            mobPosition,
            mapInstance,
        )

        worldObject?.let {
            it.addComponent(HealthComponent(100))
            it.addBehavior(ObjectSheetBehavior(RandomRollEngine()))

            it.addComponent(MovementComponent())
            it.addBehavior(MovableBehavior)
        }

        return worldObject
    }
}
