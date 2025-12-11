package fr.rob.game.map.loader.creature

import fr.rob.game.entity.template.Creature
import fr.rob.game.map.loader.WorldObjectsLoaderInterface
import fr.rob.game.persistence.map.CreatureRepositoryInterface

class CreatureLoader(private val creatureRepository: CreatureRepositoryInterface) :
    WorldObjectsLoaderInterface<Creature> {

    override fun loadObjects(mapId: Int): List<Creature> = creatureRepository.findByMapId(mapId)
}
