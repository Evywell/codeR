using UnityEngine.InputSystem;

namespace Game.Input
{
    /// <summary>
    /// Hand-coded input wrapper that creates individual InputAction objects directly.
    /// Avoids the InputActionAsset binding resolution pipeline which can crash
    /// when Enable() is called before Input System devices are fully initialized.
    ///
    /// Exposes the same API surface as the previously auto-generated class:
    /// GameInput.Gameplay.Move, Look, Zoom, CastSpell, EngageCombat, ToggleCursor.
    /// </summary>
    public class GameInput
    {
        private readonly GameplayActions _gameplay;

        public GameInput()
        {
            _gameplay = new GameplayActions();
        }

        public GameplayActions Gameplay => _gameplay;

        public class GameplayActions
        {
            public InputAction Move { get; }
            public InputAction Look { get; }
            public InputAction Zoom { get; }
            public InputAction CastSpell { get; }
            public InputAction EngageCombat { get; }
            public InputAction ToggleCursor { get; }

            public GameplayActions()
            {
                // WASD / Left Stick → Vector2 (digital normalized for keyboard, passthrough for gamepad)
                Move = new InputAction("Move", InputActionType.Value);
                Move.AddCompositeBinding("2DVector")
                    .With("Up", "<Keyboard>/w")
                    .With("Down", "<Keyboard>/s")
                    .With("Left", "<Keyboard>/a")
                    .With("Right", "<Keyboard>/d");
                Move.AddBinding("<Gamepad>/leftStick");

                // Mouse Delta / Right Stick → Vector2
                Look = new InputAction("Look", InputActionType.Value);
                Look.AddBinding("<Mouse>/delta");
                Look.AddBinding("<Gamepad>/rightStick");

                // Scroll Wheel → Vector2 (y = scroll amount)
                Zoom = new InputAction("Zoom", InputActionType.Value);
                Zoom.AddBinding("<Mouse>/scroll");

                // Q key / Gamepad West → Button
                CastSpell = new InputAction("CastSpell", InputActionType.Button);
                CastSpell.AddBinding("<Keyboard>/q");
                CastSpell.AddBinding("<Gamepad>/buttonWest");

                // E key / Gamepad East → Button
                EngageCombat = new InputAction("EngageCombat", InputActionType.Button);
                EngageCombat.AddBinding("<Keyboard>/e");
                EngageCombat.AddBinding("<Gamepad>/buttonEast");

                // Escape / Gamepad Start → Button
                ToggleCursor = new InputAction("ToggleCursor", InputActionType.Button);
                ToggleCursor.AddBinding("<Keyboard>/escape");
                ToggleCursor.AddBinding("<Gamepad>/start");
            }

            /// <summary>
            /// Enables all gameplay input actions.
            /// </summary>
            public void Enable()
            {
                Move.Enable();
                Look.Enable();
                Zoom.Enable();
                CastSpell.Enable();
                EngageCombat.Enable();
                ToggleCursor.Enable();
            }

            /// <summary>
            /// Disables all gameplay input actions.
            /// </summary>
            public void Disable()
            {
                Move.Disable();
                Look.Disable();
                Zoom.Disable();
                CastSpell.Disable();
                EngageCombat.Disable();
                ToggleCursor.Disable();
            }
        }
    }
}
