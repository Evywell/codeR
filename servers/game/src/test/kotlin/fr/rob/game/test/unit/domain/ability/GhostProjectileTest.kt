package fr.rob.game.test.unit.domain.ability

import fr.rob.game.ability.projectile.GhostProjectile
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GhostProjectileTest {
    private val worldBuilder = WorldBuilder()
    private val instance = worldBuilder.buildBasicWorld()
    private val target = DummyWorldObjectBuilder(ObjectGuidGenerator()).createUnit()

    init {
        worldBuilder.addIntoInstance(target, instance, Position(10f, 0f, 0f, 0f))
    }

    @Test
    fun `the projectile follows its target`() {
        val position = Position(0f, 0f, 0f, 0f)
        val projectile = GhostProjectile(position, target.guid, instance, 10f)

        projectile.update(500)
        target.setPosition(10f, 20f, 0f, 0f)
        projectile.update(10_000)

        assertTrue(projectile.hasHitTarget())
        assertEquals(20f, position.y)
    }

    @Test
    fun `a projectile whose target left keeps flying to the last known position`() {
        val position = Position(0f, 0f, 0f, 0f)
        val projectile = GhostProjectile(position, target.guid, instance, 10f)

        projectile.update(500)
        instance.grid.removeWorldObject(target)
        target.position.x = 40f
        projectile.update(400)
        assertFalse(projectile.hasHitTarget())

        projectile.update(10_000)

        assertTrue(projectile.hasHitTarget())
        assertEquals(10f, position.x)
    }
}
