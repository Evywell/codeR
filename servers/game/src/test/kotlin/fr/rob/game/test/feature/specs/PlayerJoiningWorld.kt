package fr.rob.game.test.feature.specs

import fr.raven.proto.message.game.setup.LogIntoWorldProto
import fr.rob.game.player.message.NearbyObjectMessage
import fr.rob.game.player.message.PlayerDescriptionMessage
import fr.rob.game.entity.ObjectManager
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.instance.InstanceManager
import fr.rob.game.player.session.GameSession
import fr.rob.game.map.Map
import fr.rob.game.map.MapInfo
import fr.rob.game.map.ZoneInfo
import fr.rob.game.world.DelayedUpdateQueue
import fr.rob.game.world.World
import fr.rob.game.world.packet.WorldPacket
import fr.rob.game.world.packet.WorldPacketQueue
import fr.rob.game.network.opcode.CMSG_LOG_INTO_WORLD
import fr.rob.game.network.opcode.SMSG_NEARBY_OBJECT_UPDATE
import fr.rob.game.network.opcode.SMSG_PLAYER_DESCRIPTION
import fr.rob.game.test.feature.DatabaseTestApplication
import fr.rob.game.test.unit.sandbox.network.session.StoreMessageSender
import org.junit.jupiter.api.Test
import org.koin.test.get

class PlayerJoiningWorld : DatabaseTestApplication() {
    @Test
    fun `When player joins the world, it should be notified with objects around him`() {
        // Loading fixtures
        databaseConnection.executeStatement("INSERT INTO accounts (id, user_id, is_administrator, name) VALUES (1, 1, 1, 'Evywell#1234')")
        databaseConnection.executeStatement("INSERT INTO characters (id, account_id, name, level, position_x, position_y, position_z, orientation, last_selected_at) VALUES (1, 1, 'Evy', 1, 0, 0, 0, 0, '2022-09-14 00:00:00')")

        // 1. Instantiate the world
        val instanceManager = get<InstanceManager>()
        val worldPacketQueue = get<WorldPacketQueue>()
        val objectManager = get<ObjectManager>()
        val world = World(instanceManager, worldPacketQueue, get<DelayedUpdateQueue>())

        val instance = instanceManager.create(
            DEFAULT_TEST_INSTANCE_ID,
            Map(
                DEFAULT_TEST_MAP_ID,
                DEFAULT_TEST_ZONE_ID,
                MapInfo(DEFAULT_TEST_MAP_NAME, DEFAULT_TEST_MAP_WIDTH, DEFAULT_TEST_MAP_HEIGHT),
                ZoneInfo(DEFAULT_TEST_ZONE_NAME, DEFAULT_TEST_ZONE_WIDTH, DEFAULT_TEST_ZONE_HEIGHT, 0f, 0f),
            ),
        )

        val messageSender = StoreMessageSender()
        val gameSession = GameSession(DEFAULT_TEST_ACCOUNT_ID, messageSender)

        // 2. Create objects around the future player
        objectManager.spawnObject(
            ObjectGuid.LowGuid(1u, 0u),
            Position(5f, 0f, 0f, 0f),
            instance,
        )

        objectManager.spawnObject(
            ObjectGuid.LowGuid(2u, 0u),
            Position(6f, 0f, 0f, 0f),
            instance,
        )

        // 3. Pop the player
        worldPacketQueue.enqueue(
            WorldPacket(
                gameSession,
                CMSG_LOG_INTO_WORLD,
                LogIntoWorldProto.LogIntoWorld.newBuilder()
                    .setCharacterId(1)
                    .build(),
            ),
        )

        world.update(1)
        // 4. Assert objects are received
        val messages = messageSender.getMessages()

        // I should receive player description message
        assertContainsMessage(messages) { container ->
            container.session.accountId == DEFAULT_TEST_ACCOUNT_ID &&
                container.message.opcode == SMSG_PLAYER_DESCRIPTION &&
                container.message.body is PlayerDescriptionMessage &&
                (container.message.body as PlayerDescriptionMessage).name == "Evy" &&
                (container.message.body as PlayerDescriptionMessage).guid == ObjectGuid(1u, 1)
        }

        assertContainsMessage(messages) { container ->
            container.session.accountId == DEFAULT_TEST_ACCOUNT_ID &&
                container.message.opcode == SMSG_NEARBY_OBJECT_UPDATE &&
                container.message.body is NearbyObjectMessage &&
                (container.message.body as NearbyObjectMessage).objectId == getGuidFromLow(ObjectGuid.LowGuid(1u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT) &&
                (container.message.body as NearbyObjectMessage).position == Position(5f, 0f, 0f, 0f)
        }

        assertContainsMessage(messages) { container ->
            container.session.accountId == DEFAULT_TEST_ACCOUNT_ID &&
                container.message.opcode == SMSG_NEARBY_OBJECT_UPDATE &&
                container.message.body is NearbyObjectMessage &&
                (container.message.body as NearbyObjectMessage).objectId == getGuidFromLow(ObjectGuid.LowGuid(2u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT) &&
                (container.message.body as NearbyObjectMessage).position == Position(6f, 0f, 0f, 0f)
        }

        assertContainsMessage(messages) { container ->
            container.session.accountId == DEFAULT_TEST_ACCOUNT_ID &&
                container.message.opcode == SMSG_NEARBY_OBJECT_UPDATE &&
                container.message.body is NearbyObjectMessage &&
                (container.message.body as NearbyObjectMessage).objectId == ObjectGuid(1u, 1) &&
                (container.message.body as NearbyObjectMessage).position == Position(0f, 0f, 0f, 0f)
        }

        // Temp assert the straight spline movement generator is working
        assertContainsMessage(messages) { container ->
            container.session.accountId == DEFAULT_TEST_ACCOUNT_ID &&
                container.message.opcode == SMSG_NEARBY_OBJECT_UPDATE &&
                container.message.body is NearbyObjectMessage &&
                (container.message.body as NearbyObjectMessage).objectId == getGuidFromLow(ObjectGuid.LowGuid(1u, 1u), ObjectGuid.GUID_TYPE.GAME_OBJECT) &&
                (container.message.body as NearbyObjectMessage).position == Position(10f, 10f, 1f, 0f)
        }
    }

    companion object {
        const val DEFAULT_TEST_INSTANCE_ID = 1
        const val DEFAULT_TEST_MAP_ID = 1
        const val DEFAULT_TEST_ZONE_ID = 1
        const val DEFAULT_TEST_MAP_NAME = "Unit Test"
        const val DEFAULT_TEST_MAP_WIDTH = 200
        const val DEFAULT_TEST_MAP_HEIGHT = 200
        const val DEFAULT_TEST_ZONE_NAME = DEFAULT_TEST_MAP_NAME
        const val DEFAULT_TEST_ZONE_WIDTH = DEFAULT_TEST_MAP_WIDTH
        const val DEFAULT_TEST_ZONE_HEIGHT = DEFAULT_TEST_MAP_HEIGHT

        const val DEFAULT_TEST_ACCOUNT_ID = 1
    }
}
