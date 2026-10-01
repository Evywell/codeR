package fr.rob.game.player.action

import fr.rob.game.behavior.MovableBehavior
import fr.rob.game.behavior.ObjectSheetBehavior
import fr.rob.game.character.Character
import fr.rob.game.character.CharacterService
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
import fr.rob.game.world.GameCoroutines
import fr.rob.game.world.RandomRollEngine

class CreatePlayerIntoWorldHandler(
    private val playerFactory: PlayerFactory,
    private val characterService: CharacterService,
    private val objectManager: ObjectManager,
    private val splineMovementBrain: SplineMovementBrainInterface,
    private val coroutines: GameCoroutines,
) {
    fun execute(command: CreatePlayerIntoWorldCommand) {
        coroutines.launch("load-character-${command.characterId}") {
            val character = coroutines.database {
                characterService.loadFromCharacterIdForAccountId(command.gameSession.accountId, command.characterId)
            } ?: return@launch

            createPlayerIntoWorld(command, character)
        }
    }

    private fun createPlayerIntoWorld(command: CreatePlayerIntoWorldCommand, character: Character) {
        val playerGameSession = command.gameSession
        val (player, position) = playerFactory.createFromCharacterForSession(
            playerGameSession,
            character,
        )

        playerGameSession.assignToPlayer(player)

        // @todo remove this
        val worldObject = createMobAroundPosition(ObjectGuid.LowGuid(1u, 1u), Position(10f, 0f, 1f, 0f), command.mapInstance)

        objectManager.addEntityIntoInstance(player, command.mapInstance, position)

        // @todo Send player info
        player.ownerGameSession.send(PlayerDescriptionMessage(player.guid, player.name))

        worldObject?.let {
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
