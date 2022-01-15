package fr.rob.game.game.world.map.loader.creature

import fr.rob.game.game.world.entity.template.Creature

interface CreatureRepositoryInterface {

    fun findByMapId(mapId: Int): List<Creature>
}
