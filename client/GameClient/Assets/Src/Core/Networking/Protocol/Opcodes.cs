namespace Core.Networking.Protocol
{
    /// <summary>
    /// All opcode constants for client-server communication.
    /// CMSG = Client Message (client -> server), SMSG = Server Message (server -> client).
    /// </summary>
    public static class Opcodes
    {
        // EAS (Authentication)
        public const int EAS_AUTHENTICATION_RESULT = 0x01;

        // Realm
        public const int CMSG_REALM_JOIN_WORLD = 0x03;
        public const int SMSG_REALM_BIND_CHARACTER_TO_NODE = 0x01;
        public const int SMSG_REALM_GAME_NODE_READY = 0x02;

        // Game - Client to Server
        public const int CMSG_LOG_INTO_WORLD = 0x01;
        public const int CMSG_REMOVE_FROM_WORLD = 0x05;
        public const int CMSG_PLAYER_MOVEMENT = 0x06;
        public const int CMSG_PLAYER_CAST_SPELL = 0x07;
        public const int CMSG_PLAYER_ENGAGE_COMBAT = 0x09;
        public const int CMSG_PLAYER_USE_ABILITY = 0x0B;
        public const int CMSG_CHEAT_TELEPORT = 0x98;

        // Game - Server to Client
        public const int SMSG_PLAYER_DESCRIPTION = 0x02;
        public const int SMSG_NEARBY_OBJECT_UPDATE = 0x03;
        public const int SMSG_MOVEMENT_HEARTBEAT = 0x04;
        public const int SMSG_OBJECT_HEALTH_UPDATED = 0x08;
        public const int SMSG_OBJECT_MOVING_TO_DESTINATION = 0x0A;
        public const int SMSG_ABILITY_FAILED = 0x0C;
        public const int SMSG_DEBUG_SIGNAL = 0x97;
        public const int SMSG_GAME_INITIALIZATION_SUCCEED = 0x99;
    }
}
