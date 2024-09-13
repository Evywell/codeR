using System;
using System.Collections.Generic;
using Fr.Raven.Proto.Message.Game;
using RobClient.Game.Entity;
using RobClient.Game.Entity.Guid;

namespace RobClient.Game.World {
    public class WorldObserver : IDisposable {
        private ObjectFactory _objectFactory = new ObjectFactory();
        private GameEnvironment _environment;
        private List<IDisposable> sink = new List<IDisposable>();

        public WorldObserver(GameEnvironment environment, WorldApi worldApi)
        {
            _environment = environment;
            sink.Add(worldApi.GamePacketReceivedObs.Subscribe(packet => {
                switch (packet.Opcode) {
                    case 0x02: // SMSG_PLAYER_DESCRIPTION
                        OnPlayerInitialized(PlayerDescription.Parser.ParseFrom(packet.Body));
                        break;
                    case 0x03: // SMSG_NEARBY_OBJECT_UPDATE
                        OnObjectSpawn(NearbyObjectOpcode.Parser.ParseFrom(packet.Body));
                        break;
                    case 0x04: // SMSG_MOVEMENT_HEARTBEAT
                        OnObjectMove(MovementHeartbeat.Parser.ParseFrom(packet.Body));
                        break;
                    case 0x08: // SMSG_OBJECT_HEALTH_UPDATED
                        OnObjectHealthUpdated(ObjectSheetUpdate.Parser.ParseFrom(packet.Body));
                        break;
                    case 0x97:
                        OnDebugSignalReceived(DebugSignal.Parser.ParseFrom(packet.Body));
                        break;
                }
            }));
        }

        public void Dispose()
        {
            foreach (IDisposable obs in sink) {
                obs.Dispose();
            }
        }

        private void OnPlayerInitialized(PlayerDescription description)
        {
             var player = _objectFactory.CreatePlayer(
                ObjectGuid.From(description.Guid),
                description.Name,
                new Vector4f(12f, 0.7f, 0f, 2.61799f) // @todo to change with real data
            );

            _environment.AddPlayerToWorld(player);
        }

        private void OnObjectSpawn(NearbyObjectOpcode nearbyObject)
        {
            var gameObject = _objectFactory.Create(
                ObjectGuid.From(nearbyObject.Guid),
                new Vector4f(
                    nearbyObject.PosX,
                    nearbyObject.PosY,
                    nearbyObject.PosZ,
                    nearbyObject.Orientation
                )
            );

            _environment.AttachObject(gameObject);
        }

        private void OnObjectMove(MovementHeartbeat heartbeat)
        {
            _environment.UpdateObjectPosition(
                ObjectGuid.From(heartbeat.Guid), 
                new Vector4f(
                    heartbeat.Position.PosX,
                    heartbeat.Position.PosY,
                    heartbeat.Position.PosZ,
                    heartbeat.Position.Orientation
                ),
                new Vector3f(heartbeat.Direction.X, heartbeat.Direction.Y, heartbeat.Direction.Z),
                heartbeat.Phase == MovementPhase.PhaseBegin
            );
        }

        private void OnObjectHealthUpdated(ObjectSheetUpdate objectSheetUpdate)
        {
            _environment.UpdateObjectHealth(
                ObjectGuid.From(objectSheetUpdate.Guid),
                objectSheetUpdate.Health
            );
        }

        private void OnDebugSignalReceived(DebugSignal debugSignal)
        {
            _environment.DispatchSignal(debugSignal);
        }
    }
}