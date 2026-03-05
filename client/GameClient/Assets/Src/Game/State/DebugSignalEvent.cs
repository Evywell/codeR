namespace Game.State
{
    /// <summary>
    /// A debug signal sent by the server, forwarded to interested Unity components.
    /// </summary>
    public readonly struct DebugSignalEvent
    {
        public string Name { get; }
        public int Value { get; }

        public DebugSignalEvent(string name, int value)
        {
            Name = name;
            Value = value;
        }
    }
}
