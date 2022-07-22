package fr.rob.game.infra.mysql.player

import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.player.InstanceFinderInterface

class MysqlInstanceFinder(private val instanceManager: InstanceManager) : InstanceFinderInterface {
    override fun fromCharacterId(characterId: Int): MapInstance = instanceManager.retrieve(1) // @todo replace this
}
