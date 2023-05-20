using RobClient.Game.Entity;

namespace RobClient.Game.View {
    public interface IObjectViewFactory {
        void Create(WorldObject worldObject);
    }
}