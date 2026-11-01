package fr.rob.game.test.unit.domain.game.world.grid

import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class GridGuidIndexTest {
    private val worldBuilder = WorldBuilder()
    private val instance = worldBuilder.buildBasicWorld()
    private val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

    @Test
    fun `an added object is found by guid, also after moving`() {
        val unit = objectBuilder.createUnit()
        worldBuilder.addIntoInstance(unit, instance, Position(1f, 1f, 0f, 0f))

        assertSame(unit, instance.findObjectByGuid(unit.guid))

        unit.setPosition(35f, 35f, 0f, 0f)
        assertSame(unit, instance.findObjectByGuid(unit.guid))
    }

    @Test
    fun `a removed object is no longer found`() {
        val unit = objectBuilder.createUnit()
        worldBuilder.addIntoInstance(unit, instance, Position(1f, 1f, 0f, 0f))

        instance.grid.removeWorldObject(unit)

        assertNull(instance.findObjectByGuid(unit.guid))
    }
}
