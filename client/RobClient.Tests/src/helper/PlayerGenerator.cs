using RobClient.Game.Entity;
using RobClient.Game.Entity.Guid;

namespace RobClient.Tests.Helper;

public class PlayerGenerator {
    private int currentEntry = 1;

    public Player GenerateWith(string name, Vector4f position)
    {
        var guid = new ObjectGuid(new LowGuid((uint)++this.currentEntry, 0), (int)GuidType.Player);

        return new ObjectFactory().CreatePlayer(guid, name, position);
    }

    public Player GenerateWith(string name)
    {
        return GenerateWith(name, Vector4f.Zero());
    }
}