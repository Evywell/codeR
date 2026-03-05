using Core.Networking.Gateway;
using Core.Networking.Routing;
using Core.Networking.Services;
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
    ///
    /// Packet handlers are auto-wired into the PacketRouter via IReadOnlyList injection —
    /// NetworkManager does not need to know about individual handlers.
    /// </summary>
    public class NetworkManager : IInitializable, ITickable
    {
        private readonly GatewayConnection _gatewayConnection;
        private readonly PacketRouter _router;
        private readonly AuthService _authService;
        private readonly RealmService _realmService;
        private readonly LoginConfig _loginConfig;

        [Inject]
        public NetworkManager(
            GatewayConnection gatewayConnection,
            PacketRouter router,
            AuthService authService,
            RealmService realmService,
            LoginConfig loginConfig)
        {
            _gatewayConnection = gatewayConnection;
            _router = router;
            _authService = authService;
            _realmService = realmService;
            _loginConfig = loginConfig;
        }

        /// <summary>
        /// Called once after all dependencies are injected.
        /// Wires the gateway to the router and kicks off the connect + auth + login sequence.
        /// </summary>
        public async void Initialize()
        {
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
