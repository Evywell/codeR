namespace RobClient.Game.Entity.Guid {
    public class ObjectGuid {
        public int HighGuid
        { get; private set; }

        private LowGuid _lowGuid;

        public ObjectGuid(LowGuid lowGuid, int highGuid) {
            _lowGuid = lowGuid;
            HighGuid = highGuid;
        }

        public bool IsPlayer() {
            return HighGuid == (int)GuidType.Player;
        }

        public bool IsGameObject() {
            return HighGuid == (int)GuidType.GameObject;
        }

        public ulong GetRawValue() {
            return ((ulong)HighGuid << 36) | (ulong)_lowGuid.GetRawValue();
        }

        public uint GetEntry() {
            return _lowGuid.GetRawValue() >> 16;
        }

        public uint GetCounter() {
            // Remove all the bits after the 16th using AND operator
            // e.g. 10010000011000111101 0001001100011111
            // AND  00000000000000000000 1111111111111111 (0xFFFF)
            //  =   00000000000000000000 0001001100011111 (only the 16th first bits)
            return _lowGuid.GetRawValue() & 0xFFFF;
        }

        public override bool Equals(object obj)
        {
            //
            // See the full list of guidelines at
            //   http://go.microsoft.com/fwlink/?LinkID=85237
            // and also the guidance for operator== at
            //   http://go.microsoft.com/fwlink/?LinkId=85238
            //
            
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }
            
            return GetRawValue() == ((ObjectGuid)obj).GetRawValue();
        }

        public override int GetHashCode()
        {
            return GetRawValue().GetHashCode();
        }

        public static ObjectGuid From(ulong value) {
            return new ObjectGuid(
                new LowGuid((uint) ((value >> 16) & 0xFFFFF), (uint) (value & 0xFFFF)),
                (int)(value >> 36)
            );
        }
    }
}
