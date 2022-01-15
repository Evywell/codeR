package fr.rob.game.game.world.map.loader.creature

import fr.rob.game.game.world.entity.template.Creature
import fr.rob.game.game.world.map.loader.WorldObjectsLoaderInterface

class CreatureLoader(private val creatureRepository: CreatureRepositoryInterface) :
    WorldObjectsLoaderInterface<Creature> {

    override fun loadObjects(mapId: Int): List<Creature> = creatureRepository.findByMapId(mapId)
}
