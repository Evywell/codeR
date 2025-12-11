package fr.rob.game.persistence.map

import fr.rob.game.entity.template.Creature

interface CreatureRepositoryInterface {

    fun findByMapId(mapId: Int): List<Creature>
}
