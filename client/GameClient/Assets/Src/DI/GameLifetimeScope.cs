using Core.Networking.Gateway;
using Core.Networking.Protocol;
using Core.Networking.Routing;
using Core.Networking.Services;
using Game.Entity;
using Game.Input;
using Game.Interaction;
using Game.Networking.Handlers;
using Game.State;
using UnityEngine;
using VContainer;
using VContainer.Unity;

namespace DI
{
    /// <summary>
    /// Root VContainer LifetimeScope for the game client.
    /// Wires all networking, state, handlers, services, interaction senders,
    /// entity spawning, input, and camera systems.
    /// Attach this MonoBehaviour to a GameObject in the boot scene.
    /// </summary>
    public class GameLifetimeScope : LifetimeScope
    {
        [Header("Gateway")]
        [SerializeField] private string _gatewayHost = "127.0.0.1";
        [SerializeField] private int _gatewayPort = 11111;

        [Header("Login")]
        [SerializeField] private int _userId = 1;
        [SerializeField] private int _characterId = 1;

        protected override void Configure(IContainerBuilder builder)
        {
            // --- Networking ---
            var gatewayConnection = new GatewayConnection(_gatewayHost, _gatewayPort);

            builder.RegisterInstance(gatewayConnection);
            builder.RegisterInstance<IPacketSender>(gatewayConnection);
            builder.RegisterInstance<IPacketReceiver>(gatewayConnection);

            // --- Login config ---
            builder.RegisterInstance(new LoginConfig(_userId, _characterId));

            // --- Game state ---
            builder.Register<WorldState>(Lifetime.Singleton);

            // --- Packet handlers ---
            builder.Register<PlayerDescriptionHandler>(Lifetime.Singleton);
            builder.Register<NearbyObjectHandler>(Lifetime.Singleton);
            builder.Register<MovementHeartbeatHandler>(Lifetime.Singleton);
            builder.Register<ObjectHealthHandler>(Lifetime.Singleton);
            builder.Register<ObjectDestinationHandler>(Lifetime.Singleton);
            builder.Register<DebugSignalHandler>(Lifetime.Singleton);

            // --- Packet router (wired after container build via entry point) ---
            builder.Register<PacketRouter>(Lifetime.Singleton);

            // --- Auth services ---
            builder.Register<AuthService>(Lifetime.Singleton);
            builder.Register<RealmService>(Lifetime.Singleton);

            // --- Interaction senders ---
            builder.Register<MovementSender>(Lifetime.Singleton);
            builder.Register<SpellCaster>(Lifetime.Singleton);
            builder.Register<CombatEngager>(Lifetime.Singleton);

            // --- Input (new Input System, hand-coded InputAction bindings) ---
            // NOTE: Do not call Enable() here — deferred to GameWorldManager first Tick().
            builder.RegisterInstance(new GameInput());

            // --- Entity spawning ---
            builder.Register<EntitySpawner>(Lifetime.Singleton);

            // --- Player input ---
            builder.Register<PlayerInputController>(Lifetime.Singleton);

            // --- Entry points ---
            builder.RegisterEntryPoint<NetworkManager>();
            builder.RegisterEntryPoint<GameWorldManager>();
        }
    }
}
