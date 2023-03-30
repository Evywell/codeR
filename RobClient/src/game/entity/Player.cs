using RobClient.Game.Entity.Guid;

namespace RobClient.Game.Entity;

public class Player : WorldObject
{
    public string Name
    { get; private set; }

    public Player(string name, ObjectGuid guid, Vector4f position) : base(guid, position)
    {
        Name = name;
    }
}