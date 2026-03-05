using Game.Camera;
using Game.Entity;
using Game.Input;
using Game.State;
using UnityEngine;
using VContainer;
using VContainer.Unity;

namespace DI
{
    /// <summary>
    /// VContainer entry point that manages the visual/interactive layer of the game world.
    /// Ticks the EntitySpawner (to upgrade the player entity when ready) and the
    /// PlayerInputController (WASD movement + keybinds). Initializes the CameraController
    /// on the main camera once the container is ready.
    /// </summary>
    public class GameWorldManager : ITickable
    {
        private readonly EntitySpawner _entitySpawner;
        private readonly PlayerInputController _playerInputController;
        private readonly WorldState _worldState;
        private readonly GameInput _input;

        private CameraController _cameraController;
        private bool _initialized;

        [Inject]
        public GameWorldManager(
            EntitySpawner entitySpawner,
            PlayerInputController playerInputController,
            WorldState worldState,
            GameInput input)
        {
            _entitySpawner = entitySpawner;
            _playerInputController = playerInputController;
            _worldState = worldState;
            _input = input;
        }

        public void Tick()
        {
            if (!_initialized)
            {
                Initialize();
                _initialized = true;
            }

            _entitySpawner.Tick();
            _playerInputController.Tick();
        }

        private void Initialize()
        {
            // Enable input — must happen in the player loop, not during Awake/Build,
            // because Input System devices aren't ready until after the first frame.
            _input.Gameplay.Enable();

            // Attach CameraController to the Main Camera
            UnityEngine.Camera mainCam = UnityEngine.Camera.main;

            if (mainCam != null)
            {
                _cameraController = mainCam.gameObject.AddComponent<CameraController>();
                _cameraController.Initialize(_entitySpawner, _worldState, _input);
                Debug.Log("[GameWorldManager] CameraController attached to Main Camera.");
            }
            else
            {
                Debug.LogWarning("[GameWorldManager] No Main Camera found. CameraController not attached.");
            }
        }
    }
}
