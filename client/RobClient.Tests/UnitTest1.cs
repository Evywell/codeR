using RobClient.Game.Entity.Guid;
using RobClient.Game.Event;
using RobClient.Tests.Watcher;

namespace RobClient.Tests;

public class MovementTest
{
    [Test]
    public async Task PlayerPositionChangeWhenMoving()
    {
        // Arrange
        Entrypoint client = new Entrypoint(new NullObjectViewFactory());

        var playerInteraction = client.Game.PlayerInteraction;
        var gameEnvironment = client.Game.GameEnvironment;

        await client.ConnectToGateway();
        await client.Eas.Authenticate();

        await client.Realm.ReserveCharacterWithId(1);
        await client.Game.JoinWorld();

        var watcher = new EventWatcher<ObjectMovedEvent>(client.Game);
        watcher.DefineWitness("PLAYER_MOVED", (sourceEvent) => sourceEvent.Guid == ObjectGuid.From(68719476737).GetRawValue());

        // Act
        playerInteraction.Move(0);

        await watcher.GetLastEvent("PLAYER_MOVED");

        // Assert
        var player = gameEnvironment.GetControlledObject();

        Assert.Greater(player.Position.X, 0);
        Assert.That(0, Is.EqualTo(player.Position.Y));
        Assert.That(0, Is.EqualTo(player.Position.Z));
        Assert.That(0, Is.EqualTo(player.Position.O));
    }
}