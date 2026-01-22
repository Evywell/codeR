package fr.rob.game.test.unit.domain.ability

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.ability.AbilityRequirements
import fr.rob.game.ability.AbilityTargetParameter
import fr.rob.game.ability.AbilityType
import fr.rob.game.ability.launch.GhostProjectileLaunchInfo
import fr.rob.game.ability.launch.InstantLaunchInfo
import fr.rob.game.ability.resource.ManaResourceType
import fr.rob.game.ability.service.AbilityExecutor
import fr.rob.game.ability.service.AbilityRequirementChecker
import fr.rob.game.ability.service.phase.CastingPhaseHandler
import fr.rob.game.ability.service.phase.LaunchingPhaseHandler
import fr.rob.game.ability.service.phase.ResolvingPhaseHandler
import fr.rob.game.component.resource.ManaComponent
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.TestCaseWithContainer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.koin.test.inject

class AbilityTest : TestCaseWithContainer() {
    private val worldObjectBuilder: DummyWorldObjectBuilder by inject()
    private val requirementChecker = AbilityRequirementChecker()
    private val abilityExecutor = AbilityExecutor(
        requirementChecker,
        listOf(
            CastingPhaseHandler(requirementChecker),
            LaunchingPhaseHandler(listOf(InstantLaunchInfo())),
            ResolvingPhaseHandler()
        ),
    )

    @Test
    fun `Try using ability without enough mana should fail`() {
        // Arrange
        val source = worldObjectBuilder.createPlayer()
        source.addComponent(ManaComponent(10))

        val ability =
            Ability(
                info =
                    AbilityInfo(
                        identifier = 1,
                        type = AbilityType.MAGICAL,
                        abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(100))),
                        castingTimeMs = AbilityInfo.INSTANT_CASTING_TIME,
                        launchType = AbilityInfo.LaunchType.INSTANT,
                    ),
                source = source,
                target = AbilityTargetParameter(null, source),
            )

        // Act
        abilityExecutor.startAbility(ability)

        // Assert
        assertTrue(ability.failed())
        assertTrue(ability.isDone())
        assertFalse(ability.isInProgress())
    }

    @Test
    fun `Use ability with enough mana should leads to ability done without failure`() {
        // Arrange
        val source = worldObjectBuilder.createPlayer()
        source.addComponent(ManaComponent(100))

        val ability =
            Ability(
                info =
                    AbilityInfo(
                        identifier = 1,
                        type = AbilityType.MAGICAL,
                        abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(100))),
                        castingTimeMs = AbilityInfo.INSTANT_CASTING_TIME,
                        launchType = AbilityInfo.LaunchType.INSTANT,
                    ),
                source = source,
                target = AbilityTargetParameter(null, source),
            )

        // Act
        abilityExecutor.startAbility(ability)

        // Assert
        assertTrue(ability.isDone())
        assertFalse(ability.failed())
        assertFalse(ability.isInProgress())
    }

    @Test
    fun `Use ability with non instant casting time`() {
        // Arrange
        val source = worldObjectBuilder.createPlayer()
        source.addComponent(ManaComponent(100))

        val ability =
            Ability(
                info =
                    AbilityInfo(
                        identifier = 1,
                        type = AbilityType.MAGICAL,
                        abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(100))),
                        castingTimeMs = 800,
                        launchType = AbilityInfo.LaunchType.INSTANT,
                    ),
                source = source,
                target = AbilityTargetParameter(null, source),
            )

        // Act & Assert
        abilityExecutor.startAbility(ability)

        assertTrue(ability.isInProgress())
        assertFalse(ability.isDone())
        assertFalse(ability.failed())

        abilityExecutor.updateAbility(ability, 500)

        assertTrue(ability.isInProgress())
        assertFalse(ability.isDone())
        assertFalse(ability.failed())

        abilityExecutor.updateAbility(ability, 500)

        assertFalse(ability.isInProgress())
        assertTrue(ability.isDone())
        assertFalse(ability.failed())
    }

    @Test
    fun `Mana should be consumed after casting time completes`() {
        // Arrange
        val source = worldObjectBuilder.createPlayer()
        source.addComponent(ManaComponent(100))

        val ability =
            Ability(
                info =
                    AbilityInfo(
                        identifier = 1,
                        type = AbilityType.MAGICAL,
                        abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(50))),
                        castingTimeMs = 500,
                        launchType = AbilityInfo.LaunchType.INSTANT,
                    ),
                source = source,
                target = AbilityTargetParameter(null, source),
            )

        // Act
        abilityExecutor.startAbility(ability)

        // Mana should not be consumed during casting
        assertEquals(100, source.getComponent<ManaComponent>()!!.currentMana)

        abilityExecutor.updateAbility(ability, 1000)

        // Assert - Mana consumed after casting completes
        assertEquals(50, source.getComponent<ManaComponent>()!!.currentMana)
    }
}
