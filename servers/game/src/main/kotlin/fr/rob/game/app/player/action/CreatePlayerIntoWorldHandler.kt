package fr.rob.game.app.player.action

import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.player.PlayerFactory

class CreatePlayerIntoWorldHandler(
    private val playerFactory: PlayerFactory,
    private val objectManager: ObjectManager,
) {
    fun execute(command: CreatePlayerIntoWorldCommand) {
        val playerGameSession = command.gameSession
        val createPlayerResult = playerFactory.createFromGameSession(playerGameSession, command.characterId)

        if (!createPlayerResult.isSuccess) {
            return
        }

        val player = createPlayerResult.player!!
        playerGameSession.assignToPlayer(player)

        // @todo remove this
        createMobForPlayer(player)

        player.addToWorld(objectManager)
    }

    private fun createMobForPlayer(player: Player) {
        val position = Position(player.position.x + 10, player.position.y, player.position.z, 0f)

        objectManager.spawnObject(
            ObjectGuid.LowGuid(1u, 1u),
            position,
            player.mapInstance
        )
    }
}
