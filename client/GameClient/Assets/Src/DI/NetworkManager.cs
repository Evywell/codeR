using Core.Networking.Gateway;
using Core.Networking.Protocol;
using Core.Networking.Routing;
using Core.Networking.Services;
using Game.Networking.Handlers;
using UnityEngine;
using VContainer;
using VContainer.Unity;

namespace DI
{
    /// <summary>
    /// VContainer entry point that wires the packet router to the gateway connection,
    /// runs the authentication and login flow, and drains incoming packets every frame.
    ///
    /// Registered via builder.RegisterEntryPoint&lt;NetworkManager&gt;() in GameLifetimeScope.
    /// VContainer will call Initialize once after injection and Tick every frame.
    /// </summary>
    public class NetworkManager : IInitializable, ITickable
    {
        private readonly GatewayConnection _gatewayConnection;
        private readonly PacketRouter _router;
        private readonly AuthService _authService;
        private readonly RealmService _realmService;
        private readonly LoginConfig _loginConfig;

        // Handlers injected to wire into the router
        private readonly PlayerDescriptionHandler _playerDescriptionHandler;
        private readonly NearbyObjectHandler _nearbyObjectHandler;
        private readonly MovementHeartbeatHandler _movementHeartbeatHandler;
        private readonly ObjectHealthHandler _objectHealthHandler;
        private readonly ObjectDestinationHandler _objectDestinationHandler;
        private readonly DebugSignalHandler _debugSignalHandler;

        [Inject]
        public NetworkManager(
            GatewayConnection gatewayConnection,
            PacketRouter router,
            AuthService authService,
            RealmService realmService,
            LoginConfig loginConfig,
            PlayerDescriptionHandler playerDescriptionHandler,
            NearbyObjectHandler nearbyObjectHandler,
            MovementHeartbeatHandler movementHeartbeatHandler,
            ObjectHealthHandler objectHealthHandler,
            ObjectDestinationHandler objectDestinationHandler,
            DebugSignalHandler debugSignalHandler)
        {
            _gatewayConnection = gatewayConnection;
            _router = router;
            _authService = authService;
            _realmService = realmService;
            _loginConfig = loginConfig;
            _playerDescriptionHandler = playerDescriptionHandler;
            _nearbyObjectHandler = nearbyObjectHandler;
            _movementHeartbeatHandler = movementHeartbeatHandler;
            _objectHealthHandler = objectHealthHandler;
            _objectDestinationHandler = objectDestinationHandler;
            _debugSignalHandler = debugSignalHandler;
        }

        /// <summary>
        /// Called once after all dependencies are injected.
        /// Registers all packet handlers, wires the gateway to the router,
        /// and kicks off the connect + auth + login sequence.
        /// </summary>
        public async void Initialize()
        {
            _router.RegisterHandler(Opcodes.SMSG_PLAYER_DESCRIPTION, _playerDescriptionHandler);
            _router.RegisterHandler(Opcodes.SMSG_NEARBY_OBJECT_UPDATE, _nearbyObjectHandler);
            _router.RegisterHandler(Opcodes.SMSG_MOVEMENT_HEARTBEAT, _movementHeartbeatHandler);
            _router.RegisterHandler(Opcodes.SMSG_OBJECT_HEALTH_UPDATED, _objectHealthHandler);
            _router.RegisterHandler(Opcodes.SMSG_OBJECT_MOVING_TO_DESTINATION, _objectDestinationHandler);
            _router.RegisterHandler(Opcodes.SMSG_DEBUG_SIGNAL, _debugSignalHandler);

            _gatewayConnection.PacketReceived += _router.Route;

            // Connect and authenticate
            try
            {
                Debug.Log("[NetworkManager] Connecting to gateway...");
                await _gatewayConnection.Connect();
                Debug.Log("[NetworkManager] Connected. Authenticating...");

                await _authService.AuthenticateWithUserId(_loginConfig.UserId);
                Debug.Log("[NetworkManager] Auth sent. Joining world...");

                await _realmService.JoinWorldWithCharacter(_loginConfig.CharacterId);
                Debug.Log("[NetworkManager] Login sequence complete. Waiting for server response...");
            }
            catch (System.Exception ex)
            {
                Debug.LogError($"[NetworkManager] Login failed: {ex}");
            }
        }

        /// <summary>
        /// Called every frame by VContainer's PlayerLoopSystem.
        /// Drains the incoming packet queue on the main thread.
        /// </summary>
        public void Tick()
        {
            _gatewayConnection.ProcessIncomingPackets();
        }
    }
}
