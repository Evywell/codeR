package fr.rob.game.test.unit.domain.ability

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.ability.AbilityRequirements
import fr.rob.game.ability.AbilityTargetParameter
import fr.rob.game.ability.AbilityType
import fr.rob.game.ability.effect.InstantDamageEffect
import fr.rob.game.ability.launch.GhostProjectileLaunchInfo
import fr.rob.game.ability.launch.InstantLaunchInfo
import fr.rob.game.ability.resource.ManaResourceType
import fr.rob.game.ability.service.AbilityExecutor
import fr.rob.game.ability.service.AbilityRequirementChecker
import fr.rob.game.ability.service.phase.CastingPhaseHandler
import fr.rob.game.ability.service.phase.ResolvingPhaseHandler
import fr.rob.game.ability.trigger.ApplyAbilityEffectsTrigger
import fr.rob.game.behavior.ObjectSheetBehavior
import fr.rob.game.component.resource.HealthComponent
import fr.rob.game.component.resource.ManaComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.test.unit.tools.RiggedDiceEngine
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import fr.rob.game.entity.Unit as WorldUnit

class AbilityEffectsTest {
    private lateinit var caster: WorldUnit
    private lateinit var target: WorldUnit
    private val riggedDiceEngine = RiggedDiceEngine()
    private val instance = WorldBuilder.buildBasicWorld()
    private val guidGenerator = ObjectGuidGenerator()
    private var entryGuid: UInt = 1u

    private val requirementChecker = AbilityRequirementChecker()
    private val abilityExecutor = AbilityExecutor(
        requirementChecker,
        listOf(CastingPhaseHandler(requirementChecker), ResolvingPhaseHandler()),
    )

    @BeforeEach
    fun setUp() {
        caster = createUnit("Caster", 5, Position(0f, 0f, 0f, 0f))
        caster.addComponent(ManaComponent(100))
        caster.registerAbilities(listOf(1, 2, 3, 4))

        target = createUnit("Target", 3, Position(5f, 0f, 0f, 0f))
    }

    @Test
    fun `Instant damage ability should reduce target health`() {
        // Arrange
        val initialHealth = target.getComponent<HealthComponent>()!!.value
        val damageValue = 25

        // Rig dice to avoid critical hits
        riggedDiceEngine.nextRollResult = ObjectSheetBehavior.MAX_ROLL_FOR_PERCENTAGE

        val ability = Ability(
            info = AbilityInfo(
                identifier = 1,
                type = AbilityType.MAGICAL,
                abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(10))),
                launchInfo = InstantLaunchInfo(
                    ApplyAbilityEffectsTrigger(
                        arrayOf(InstantDamageEffect.InstantDamageEffectInfo(damageValue))
                    )
                ),
                castingTimeMs = AbilityInfo.INSTANT_CASTING_TIME,
            ),
            source = caster,
            target = AbilityTargetParameter(target.guid, caster),
        )

        // Act
        abilityExecutor.startAbility(ability)

        // Assert
        assertTrue(ability.isDone())
        assertFalse(ability.failed())
        assertEquals(initialHealth - damageValue, target.getComponent<HealthComponent>()!!.value)
    }

    @Test
    fun `Projectile ability should complete when projectile reaches target`() {
        // Arrange
        val damageValue = 40
        val projectileSpeed = 10f // 10 meters per second

        // Rig dice to avoid critical hits
        riggedDiceEngine.nextRollResult = ObjectSheetBehavior.MAX_ROLL_FOR_PERCENTAGE

        val initialHealth = target.getComponent<HealthComponent>()!!.value

        val ability = Ability(
            info = AbilityInfo(
                identifier = 4,
                type = AbilityType.MAGICAL,
                abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(15))),
                launchInfo = GhostProjectileLaunchInfo(
                    projectileSpeed = projectileSpeed,
                    onHitTargetTrigger = ApplyAbilityEffectsTrigger(
                        arrayOf(InstantDamageEffect.InstantDamageEffectInfo(damageValue))
                    ),
                ),
                castingTimeMs = AbilityInfo.INSTANT_CASTING_TIME,
            ),
            source = caster,
            target = AbilityTargetParameter(target.guid, caster),
        )

        // Act
        abilityExecutor.startAbility(ability)

        // Ability should be in resolving state (projectile flying)
        assertTrue(ability.isInProgress())
        assertEquals(Ability.AbilityState.RESOLVING, ability.state)

        // Target at 5m, speed 10m/s, so after 300ms (3m traveled), still in progress
        abilityExecutor.updateAbility(ability, 300)
        assertTrue(ability.isInProgress())

        // After another 300ms (6m total traveled), should be done (target at 5m)
        abilityExecutor.updateAbility(ability, 300)
        assertTrue(ability.isDone())
        assertFalse(ability.failed())

        // Assert damage was applied
        assertEquals(initialHealth - damageValue, target.getComponent<HealthComponent>()!!.value)
    }

    @Test
    fun `Mana should be consumed when ability is cast successfully`() {
        // Arrange
        val initialMana = caster.getComponent<ManaComponent>()!!.currentMana
        val manaCost = 25

        val ability = Ability(
            info = AbilityInfo(
                identifier = 1,
                type = AbilityType.MAGICAL,
                abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(manaCost))),
                launchInfo = InstantLaunchInfo(),
                castingTimeMs = AbilityInfo.INSTANT_CASTING_TIME,
            ),
            source = caster,
            target = AbilityTargetParameter(null, caster),
        )

        // Act
        abilityExecutor.startAbility(ability)

        // Assert
        assertTrue(ability.isDone())
        assertFalse(ability.failed())
        assertEquals(initialMana - manaCost, caster.getComponent<ManaComponent>()!!.currentMana)
    }

    private fun createUnit(name: String, level: Int, position: Position): WorldUnit {
        val unitToCreate = WorldUnit(
            guidGenerator.fromGuidInfo(
                ObjectGuidGenerator.GuidInfo(
                    ObjectGuid.LowGuid(entryGuid++, 0u),
                    ObjectGuid.GUID_TYPE.GAME_OBJECT,
                )
            ),
            name,
            level,
        )
        unitToCreate.addComponent(HealthComponent(100))
        unitToCreate.addBehavior(ObjectSheetBehavior(riggedDiceEngine))
        unitToCreate.addIntoInstance(instance, position)

        return unitToCreate
    }
}
