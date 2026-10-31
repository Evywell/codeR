package fr.rob.game.test.unit.domain.movement

import fr.raven.proto.message.game.MovementProto
import fr.raven.proto.message.game.MovementProto.CheatTeleport
import fr.raven.proto.message.game.MovementProto.SMovementInfo
import fr.raven.proto.message.game.PositionProto
import fr.rob.game.behavior.MovableBehavior
import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.entity.movement.MovementValidator
import fr.rob.game.network.opcode.CMSG_CHEAT_TELEPORT
import fr.rob.game.network.opcode.CMSG_PLAYER_MOVEMENT
import fr.rob.game.player.Player
import fr.rob.game.player.message.MovementHeartbeatMessage
import fr.rob.game.player.session.GameSession
import fr.rob.game.test.unit.sandbox.network.session.StoreMessageSender
import fr.rob.game.test.unit.tools.WorldBuilder
import fr.rob.game.world.function.CheatTeleportFunction
import fr.rob.game.world.function.MovePlayerFunction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class MovementValidationTest {
    private val messageSender = StoreMessageSender()
    private val session = GameSession(1, messageSender)
    private val player = Player(session, ObjectGuidGenerator().createForPlayer(1), "Test", 1)
    private val start = Position(1f, 2f, 3f, 0.5f)

    init {
        // Basic world: 100x100 zone centered on 0
        WorldBuilder().apply { addIntoInstance(player, buildBasicWorld(), Position(start.x, start.y, start.z, start.orientation)) }
        player.addComponent(MovementComponent())
        player.addBehavior(MovableBehavior)
        session.beginLoading()
        session.enterWorld(player)
    }

    private fun move(x: Float, y: Float, z: Float, orientation: Float = 0f) =
        MovePlayerFunction(MovementValidator()).invoke(
            session,
            CMSG_PLAYER_MOVEMENT,
            SMovementInfo.newBuilder()
                .setPosition(position(x, y, z, orientation))
                .setPhase(MovementProto.MovementPhase.PHASE_BEGIN)
                .setDirection(MovementProto.Direction.newBuilder())
                .build(),
        )

    private fun teleport(x: Float, y: Float, z: Float, orientation: Float) =
        CheatTeleportFunction(MovementValidator()).invoke(
            session,
            CMSG_CHEAT_TELEPORT,
            CheatTeleport.newBuilder().setPosition(position(x, y, z, orientation)).build(),
        )

    private fun position(x: Float, y: Float, z: Float, orientation: Float) =
        PositionProto.Position.newBuilder().setPosX(x).setPosY(y).setPosZ(z).setOrientation(orientation)

    private fun assertRejectedWithCorrection() {
        assertTrue(player.position.isSameHas(start), "position must not change, got ${player.position.x},${player.position.y},${player.position.z}")

        val correction = messageSender.getMessages().single().message.body as MovementHeartbeatMessage
        assertEquals(player.guid, correction.objectId)
        assertTrue(correction.position.isSameHas(start))
    }

    @ParameterizedTest
    @MethodSource("invalidDestinations")
    fun `invalid movement is rejected with a correction`(x: Float, y: Float, z: Float, orientation: Float) {
        move(x, y, z, orientation)

        assertRejectedWithCorrection()
    }

    @ParameterizedTest
    @MethodSource("invalidDestinations")
    fun `invalid teleport is rejected with a correction`(x: Float, y: Float, z: Float, orientation: Float) {
        teleport(x, y, z, orientation)

        assertRejectedWithCorrection()
    }

    @Test
    fun `valid movement is applied`() {
        move(10f, -5f, 1f)

        assertTrue(player.position.isSameHas(Position(10f, -5f, 1f, 0f)))
    }

    companion object {
        @JvmStatic
        fun invalidDestinations(): Stream<Arguments> = Stream.of(
            Arguments.of(Float.NaN, 0f, 0f, 0f),
            Arguments.of(0f, Float.POSITIVE_INFINITY, 0f, 0f),
            Arguments.of(0f, 0f, Float.NEGATIVE_INFINITY, 0f),
            Arguments.of(0f, 0f, 0f, Float.NaN),
            Arguments.of(51f, 0f, 0f, 0f),
            Arguments.of(0f, -1_000_000f, 0f, 0f),
        )
    }
}
