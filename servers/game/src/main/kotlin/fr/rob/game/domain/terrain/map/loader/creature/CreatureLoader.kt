package fr.rob.game.domain.terrain.map.loader.creature

import fr.rob.game.domain.entity.template.Creature
import fr.rob.game.domain.terrain.map.loader.WorldObjectsLoaderInterface

class CreatureLoader(private val creatureRepository: CreatureRepositoryInterface) :
    WorldObjectsLoaderInterface<Creature> {

    override fun loadObjects(mapId: Int): List<Creature> = creatureRepository.findByMapId(mapId)
}
