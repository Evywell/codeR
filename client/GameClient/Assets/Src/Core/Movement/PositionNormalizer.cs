using UnityEngine;

namespace Core.Movement
{
    /// <summary>
    /// Converts orientation angles between server and Unity coordinate spaces.
    /// The server and Unity use different orientation conventions — this utility
    /// handles the bidirectional conversion.
    /// </summary>
    public static class PositionNormalizer
    {
        private const float PI_2 = Mathf.PI / 2;
        private const float A2_PI = 2 * Mathf.PI;

        /// <summary>
        /// Converts a Unity euler Y rotation (in radians) to server orientation.
        /// Unity uses Y-up with clockwise rotation from +Z; the server uses Z-up with
        /// counter-clockwise rotation from +X, with Unity's +Z mapped to the server's +Y.
        /// The conversion is therefore a single mirror: (π/2 − x) mod 2π.
        /// </summary>
        public static float TransformUnityOrientationToServerOrientation(float orientation)
        {
            float result = (PI_2 - orientation) % A2_PI;
            return result < 0f ? result + A2_PI : result;
        }

        /// <summary>
        /// Converts a server orientation to Unity euler Y rotation (in radians).
        /// The mapping is an involution, so the same formula is used in both directions.
        /// </summary>
        public static float TransformServerOrientationToUnityOrientation(float orientation)
        {
            float result = (PI_2 - orientation) % A2_PI;
            return result < 0f ? result + A2_PI : result;
        }
    }
}
