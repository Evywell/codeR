package fr.rob.game.test.feature.specs

import fr.raven.proto.message.game.MovementProto.CheatTeleport
import fr.raven.proto.message.game.setup.LogIntoWorldProto
import fr.rob.game.app.player.message.MovementHeartbeatMessage
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.instance.InstanceManager
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.terrain.map.Map
import fr.rob.game.domain.terrain.map.MapInfo
import fr.rob.game.domain.terrain.map.ZoneInfo
import fr.rob.game.domain.world.DelayedUpdateQueue
import fr.rob.game.domain.world.World
import fr.rob.game.domain.world.packet.WorldPacket
import fr.rob.game.domain.world.packet.WorldPacketQueue
import fr.rob.game.infra.opcode.CMSG_CHEAT_TELEPORT
import fr.rob.game.infra.opcode.CMSG_LOG_INTO_WORLD
import fr.rob.game.infra.opcode.SMSG_MOVEMENT_HEARTBEAT
import fr.rob.game.test.feature.DatabaseTestApplication
import fr.rob.game.test.unit.sandbox.network.session.StoreMessageSender
import org.junit.jupiter.api.Test
import org.koin.test.get
import fr.raven.proto.message.game.PositionProto.Position as PositionProto

class PlayerCheatTeleport : DatabaseTestApplication() {
    @Test
    fun `As a player, I should be able to teleport for testing purpose`() {
        // Loading fixtures
        databaseConnection.executeStatement("INSERT INTO accounts (id, user_id, is_administrator, name) VALUES (1, 1, 1, 'Evywell#1234')")
        databaseConnection.executeStatement("INSERT INTO characters (id, account_id, name, level, position_x, position_y, position_z, orientation, last_selected_at) VALUES (1, 1, 'Evy', 1, 0, 0, 0, 0, '2022-09-14 00:00:00')")

        val instanceManager = get<InstanceManager>()
        val worldPacketQueue = get<WorldPacketQueue>()
        val world = World(instanceManager, worldPacketQueue, get<DelayedUpdateQueue>())

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
                CMSG_CHEAT_TELEPORT,
                CheatTeleport.newBuilder()
                    .setPosition(
                        PositionProto.newBuilder()
                            .setPosX(8f)
                            .setPosY(-6f)
                            .setPosZ(3.3f)
                            .setOrientation(0.6f),
                    )
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
                (container.message.body as MovementHeartbeatMessage).position == Position(8f, -6f, 3.3f, 0.6f)
        }
    }
}
