using System.Collections.Concurrent;
using RobClient.Game.Interaction.Action;

namespace RobClient.Game.World {
    public class LocalWorld {
        ConcurrentQueue<IAction> actions = new ConcurrentQueue<IAction>();

        public void Update(int deltaTime) {
            int actionsToProceed = GetMaxActionsToProceed();
            
            for (int i = 0; i < actionsToProceed; i++) {
                actions.TryDequeue(out IAction action);
                action.Invoke(deltaTime);

                if (action.ShouldBeRepeated()) {
                    AddAction(action);
                }
            }
        }

        public void AddAction(IAction action) {
            actions.Enqueue(action);
        }

        private int GetMaxActionsToProceed() {
            return System.Math.Min(actions.Count, 20);
        }
    }
}