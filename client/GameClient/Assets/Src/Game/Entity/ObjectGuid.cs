namespace Game.Entity
{
    /// <summary>
    /// Represents a unique object identifier matching the server's GUID system.
    /// 64-bit: high 28 bits = type, next 20 bits = entry, lowest 16 bits = counter.
    /// </summary>
    public class ObjectGuid
    {
        public int HighGuid { get; private set; }

        private readonly LowGuid _lowGuid;

        public ObjectGuid(LowGuid lowGuid, int highGuid)
        {
            _lowGuid = lowGuid;
            HighGuid = highGuid;
        }

        public bool IsPlayer() => HighGuid == (int)GuidType.Player;

        public bool IsGameObject() => HighGuid == (int)GuidType.GameObject;

        public bool IsScriptableObject() => HighGuid == (int)GuidType.ScriptableObject;

        public GuidType GetGuidType()
        {
            if (IsPlayer()) return GuidType.Player;
            if (IsGameObject()) return GuidType.GameObject;
            if (IsScriptableObject()) return GuidType.ScriptableObject;
            return GuidType.GameObject;
        }

        public ulong GetRawValue() => ((ulong)HighGuid << 36) | (ulong)_lowGuid.GetRawValue();

        public uint GetEntry() => _lowGuid.GetRawValue() >> 16;

        public uint GetCounter() => _lowGuid.GetRawValue() & 0xFFFF;

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
                return false;

            return GetRawValue() == ((ObjectGuid)obj).GetRawValue();
        }

        public override int GetHashCode() => GetRawValue().GetHashCode();

        public override string ToString() => $"ObjectGuid(type={GetGuidType()}, entry={GetEntry()}, counter={GetCounter()})";

        /// <summary>
        /// Reconstructs an ObjectGuid from a raw 64-bit value (as received from the server).
        /// </summary>
        public static ObjectGuid From(ulong value)
        {
            return new ObjectGuid(
                new LowGuid((uint)((value >> 16) & 0xFFFFF), (uint)(value & 0xFFFF)),
                (int)(value >> 36)
            );
        }
    }

    public class LowGuid
    {
        private readonly uint _entry;
        private readonly uint _counter;

        public LowGuid(uint entry, uint counter)
        {
            _entry = entry;
            _counter = counter;
        }

        public uint GetRawValue() => (_entry << 16) | _counter;
    }

    public enum GuidType : int
    {
        GameObject = 0x00,
        Player = 0x01,
        ScriptableObject = 0x02,
    }
}
