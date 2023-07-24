using System.Collections.Concurrent;
using RobClient.Game.Entity.Guid;
using RobClient.Game.Entity;
using System.Collections.Generic;
using System.Reactive.Subjects;
using RobClient.Framework;

namespace RobClient.Game {
    public class GameEnvironment {
        public ObjectGuid ControlledObjectId
        { get; set; }

        public Subject<WorldObject> WorldObjectUpdatedSub
        { get; private set; }

        private ConcurrentDictionary<ulong, WorldObject> _objects = new ConcurrentDictionary<ulong, WorldObject>();
        private ConcurrentQueue<IAction> actions = new ConcurrentQueue<IAction>();

        public GameEnvironment()
        {
            WorldObjectUpdatedSub = new Subject<WorldObject>();
        }

        public void Update(int deltaTime)
        {
            int actionsToProceed = GetMaxActionsToProceed();
            
            for (int i = 0; i < actionsToProceed; i++) {
                if (!actions.TryDequeue(out IAction action)) {
                    break;
                }

                action.Invoke(deltaTime);

                if (action.ShouldBeRepeated()) {
                    AddAction(action);
                }
            }
        }

        public StateProxy<WorldObject> UseStateProxyFor(WorldObject worldObject)
        {
            return new StateProxyChangeNotify<WorldObject>(
                worldObject,
                () => WorldObjectUpdatedSub.OnNext(worldObject)
            );
        }

        public void AddAction(IAction action)
        {
            actions.Enqueue(action);
        }

        public void AddPlayerToWorld(Player player)
        {
            AttachObject(player);
            ControlledObjectId = player.Guid;
        }

        public void AttachObject(WorldObject worldObject)
        {
            _objects.TryAdd(worldObject.Guid.GetRawValue(), worldObject);
            WorldObjectUpdatedSub.OnNext(worldObject);
        }

        public void UpdateObjectPosition(ObjectGuid guid, Vector4f position)
        {
            _objects.TryGetValue(guid.GetRawValue(), out WorldObject worldObject);

            if (worldObject != null) {
                worldObject.Position = position;
                WorldObjectUpdatedSub.OnNext(worldObject);
            }
        }

        public WorldObject GetControlledObject()
        {
            if (ControlledObjectId == null) {
                return null;
            }

            if (_objects.TryGetValue(ControlledObjectId.GetRawValue(), out WorldObject worldObject)) {
                return worldObject;
            }

            return null;
        }

        public IReadOnlyDictionary<ulong, WorldObject> GetWorldObjects()
        {
            return _objects;
        }

        private int GetMaxActionsToProceed() {
            return System.Math.Min(actions.Count, 20);
        }
    }
}
