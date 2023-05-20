using RobClient.Game.Entity;

namespace RobClient.Game.View {
    public class ViewEnvironment {
        private IObjectViewFactory _objectViewFactory;

        public ViewEnvironment(IObjectViewFactory objectViewFactory) {
            _objectViewFactory = objectViewFactory;
        }

        public void AttachObject(WorldObject worldObject) {
            _objectViewFactory.Create(worldObject);
        }
    }
}