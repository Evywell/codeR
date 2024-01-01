package fr.rob.game.test.feature.specs

import fr.raven.proto.message.game.MovementProto
import fr.raven.proto.message.game.PositionProto.Position
import fr.raven.proto.message.game.setup.LogIntoWorldProto
import fr.rob.game.app.player.message.MovementHeartbeatMessage
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.terrain.map.Map
import fr.rob.game.domain.terrain.map.MapInfo
import fr.rob.game.domain.terrain.map.ZoneInfo
import fr.rob.game.domain.world.World
import fr.rob.game.domain.world.packet.WorldPacket
import fr.rob.game.domain.world.packet.WorldPacketQueue
import fr.rob.game.infra.opcode.CMSG_LOG_INTO_WORLD
import fr.rob.game.infra.opcode.CMSG_PLAYER_MOVEMENT
import fr.rob.game.infra.opcode.SMSG_MOVEMENT_HEARTBEAT
import fr.rob.game.test.feature.DatabaseTestApplication
import fr.rob.game.test.unit.sandbox.network.session.StoreMessageSender
import org.junit.jupiter.api.Test
import org.koin.test.get

class PlayerMoving : DatabaseTestApplication() {
    @Test
    fun `As a player, I should be able to move`() {
        loadFixtures()

        val instanceManager = get<InstanceManager>()
        val worldPacketQueue = get<WorldPacketQueue>()
        val world = World(instanceManager, worldPacketQueue)

        instanceManager.create(
            PlayerJoiningWorld.DEFAULT_TEST_INSTANCE_ID,
            Map(
                PlayerJoiningWorld.DEFAULT_TEST_MAP_ID,
                PlayerJoiningWorld.DEFAULT_TEST_ZONE_ID,
                MapInfo(PlayerJoiningWorld.DEFAULT_TEST_MAP_NAME, PlayerJoiningWorld.DEFAULT_TEST_MAP_WIDTH, PlayerJoiningWorld.DEFAULT_TEST_MAP_HEIGHT),
                ZoneInfo(PlayerJoiningWorld.DEFAULT_TEST_ZONE_NAME, PlayerJoiningWorld.DEFAULT_TEST_ZONE_WIDTH, PlayerJoiningWorld.DEFAULT_TEST_ZONE_HEIGHT, 0f, 0f),
            ),
        )

        val messageSender = StoreMessageSender()
        val gameSession = GameSession(PlayerJoiningWorld.DEFAULT_TEST_ACCOUNT_ID, messageSender)

        worldPacketQueue.enqueue(
            WorldPacket(
                gameSession,
                CMSG_LOG_INTO_WORLD,
                LogIntoWorldProto.LogIntoWorld.newBuilder()
                    .setCharacterId(1)
                    .build(),
            ),
        )

        worldPacketQueue.enqueue(
            WorldPacket(
                gameSession,
                CMSG_PLAYER_MOVEMENT,
                MovementProto.SMovementInfo.newBuilder()
                    .setPosition(
                        Position.newBuilder()
                            .setPosX(8f)
                            .setPosY(-3f)
                            .setPosZ(10f)
                            .setOrientation(0.8f),
                    )
                    .setPhase(MovementProto.MovementPhase.PHASE_BEGIN)
                    .setDirection(MovementProto.MovementDirectionType.TYPE_FORWARD)
                    .build(),
            ),
        )

        world.update(1)

        val messages = messageSender.getMessages()

        assertContainsMessage(messages) { container ->
            container.session.accountId == PlayerJoiningWorld.DEFAULT_TEST_ACCOUNT_ID &&
                container.message.opcode == SMSG_MOVEMENT_HEARTBEAT &&
                container.message.body is MovementHeartbeatMessage &&
                (container.message.body as MovementHeartbeatMessage).objectId == ObjectGuid(1u, 1) &&
                (container.message.body as MovementHeartbeatMessage).position == fr.rob.game.domain.entity.Position(8f, -3f, 10f, 0.8f)
        }
    }

    private fun loadFixtures() {
        // Loading fixtures
        databaseConnection.executeStatement("INSERT INTO accounts (id, user_id, is_administrator, name) VALUES (1, 1, 1, 'Evywell#1234')")
        databaseConnection.executeStatement("INSERT INTO characters (id, account_id, name, level, position_x, position_y, position_z, orientation, last_selected_at) VALUES (1, 1, 'Evy', 1, 0, 0, 0, 0, '2022-09-14 00:00:00')")
    }
}
