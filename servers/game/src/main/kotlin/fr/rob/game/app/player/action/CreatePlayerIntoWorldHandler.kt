package fr.rob.game.app.player.action

import fr.rob.game.app.player.message.NearbyObjectMessage
import fr.rob.game.app.player.message.PlayerDescriptionMessage
import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.player.PlayerFactory
import fr.rob.game.domain.player.session.GameSession

class CreatePlayerIntoWorldHandler(
    private val playerFactory: PlayerFactory,
    private val objectManager: ObjectManager,
) {
    fun execute(command: CreatePlayerIntoWorldCommand) {
        val playerGameSession = command.gameSession
        val createPlayerResult = playerFactory.createFromGameSession(
            playerGameSession,
            command.characterId,
            command.mapInstance,
        )

        if (!createPlayerResult.isSuccess) {
            return
        }

        val player = createPlayerResult.player!!
        playerGameSession.assignToPlayer(player)

        // @todo remove this
        createMobForPlayer(player)

        // @todo Send player info
        player.ownerGameSession.send(PlayerDescriptionMessage(player.guid, player.name))

        notifyPlayerFromNearbyGameObjects(player)
    }

    private fun notifyPlayerFromNearbyGameObjects(player: Player) {
        assert(player.cell != null)

        val grid = player.mapInstance.grid
        val cells = grid.retrieveNeighborCells(player.cell!!)
        val worldObjectToBeNotifiedOf = ArrayList<WorldObject>()

        for (cell in cells) {
            worldObjectToBeNotifiedOf.addAll(grid.getObjectsOfCell(cell))
        }

        // Notify worldObject.ownerGameSession
        if (player.controlledByGameSession != null) {
            // Notify worldObject.controlledByGameSession
            worldObjectToBeNotifiedOf.forEach { obj -> notifyForObject(player.controlledByGameSession!!, obj) }
        }
    }

    private fun notifyForObject(session: GameSession, obj: WorldObject) {
        if (obj.controlledByGameSession != null && obj.controlledByGameSession == session) {
            return
        }

        session.send(NearbyObjectMessage(obj.guid, obj.position))
    }

    private fun createMobForPlayer(player: Player) {
        val position = Position(player.position.x + 10, player.position.y, player.position.z, 0f)

        objectManager.spawnObject(
            ObjectGuid.LowGuid(1u, 1u),
            position,
            player.mapInstance,
        )
    }
}
