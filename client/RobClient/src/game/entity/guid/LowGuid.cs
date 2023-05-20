namespace RobClient.Game.Entity.Guid {
    public class LowGuid {
        private uint _entry;
        private uint _counter;

        public LowGuid(uint entry, uint counter) {
            _entry = entry;
            _counter = counter;
        }

        public uint GetRawValue() {
            return (_entry << 16) | _counter;
        }
    }
}
