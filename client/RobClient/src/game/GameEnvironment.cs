using System.Collections.Concurrent;
using RobClient.Game.Entity.Guid;
using RobClient.Game.Entity;
using System.Collections.Generic;
using System.Reactive.Subjects;
using RobClient.Framework;
using Fr.Raven.Proto.Message.Game;

namespace RobClient.Game {
    public class GameEnvironment {
        public ObjectGuid ControlledObjectId
        { get; set; }

        public Subject<WorldObjectUpdate> WorldObjectUpdatedSub
        { get; private set; }

        public Subject<DebugSignal> DebugSignalSub
        { get; private set; }

        private ConcurrentDictionary<ulong, WorldObject> _objects = new ConcurrentDictionary<ulong, WorldObject>();
        private ConcurrentQueue<IAction> actions = new ConcurrentQueue<IAction>();

        public GameEnvironment()
        {
            WorldObjectUpdatedSub = new Subject<WorldObjectUpdate>();
            DebugSignalSub = new Subject<DebugSignal>();
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

        public void DispatchSignal(DebugSignal debugSignal)
        {
            DebugSignalSub.OnNext(debugSignal);
        }

        public StateProxy<WorldObject> UseStateProxyFor(WorldObject worldObject)
        {
            return new StateProxyChangeNotify<WorldObject>(
                worldObject,
                () => WorldObjectUpdatedSub.OnNext(new WorldObjectUpdate(UpdateType.POSITION, worldObject))
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
            WorldObjectUpdatedSub.OnNext(new WorldObjectUpdate(UpdateType.SPAWN, worldObject));
        }

        public void UpdateObjectPosition(ObjectGuid guid, Vector4f position, Vector3f direction, bool isMovementOngoing = false)
        {
            WorldObject worldObject = GetObjectById(guid);

            if (worldObject != null) {
                worldObject.Position = position;
                worldObject.Direction = direction;
                worldObject.IsMoving = isMovementOngoing;
                WorldObjectUpdatedSub.OnNext(new WorldObjectUpdate(UpdateType.POSITION, worldObject));
            }
        }

        public void UpdateObjectHealth(ObjectGuid guid, uint newHealth)
        {
            WorldObject worldObject = GetObjectById(guid);

            if (worldObject != null) {
                worldObject.Health = newHealth;
                WorldObjectUpdatedSub.OnNext(new WorldObjectUpdate(UpdateType.RESOURCE, worldObject));
            }
        }

        public WorldObject GetObjectById(ObjectGuid guid)
        {
            _objects.TryGetValue(guid.GetRawValue(), out WorldObject worldObject);

            return worldObject;
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
