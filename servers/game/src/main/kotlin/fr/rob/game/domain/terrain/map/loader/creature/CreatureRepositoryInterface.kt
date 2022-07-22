package fr.rob.game.domain.terrain.map.loader.creature

import fr.rob.game.domain.entity.template.Creature

interface CreatureRepositoryInterface {

    fun findByMapId(mapId: Int): List<Creature>
}
