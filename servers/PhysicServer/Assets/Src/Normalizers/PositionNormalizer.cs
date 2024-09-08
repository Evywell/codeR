using UnityEngine;

namespace App.Normalizers {
    public class PositionNormalizer {
        private const float PI_2 = Mathf.PI / 2;
        private const float PI = Mathf.PI;
        private const float A3_PI_2 = 3 * PI / 2;
        private const float A2_PI = 2 * PI;

        public static float TransformUnityOrientationToServerOrientation(float orientation) {
            if (orientation <= PI_2) {
                return PI_2 - orientation;
            } else if (orientation > PI_2 && orientation <= PI) {
                return PI + orientation;
            } else if (orientation >= A3_PI_2 && orientation <= A2_PI) {
                return orientation - A3_PI_2 + (2 * (A2_PI - orientation));
            }

            return PI + (A3_PI_2 - orientation);
        }

        public static float TransformServerOrientationToUnityOrientation(float orientation) {
            if (orientation <= PI_2) {
                return PI_2 - orientation;
            } else if (orientation > PI_2 && orientation <= PI) {
                return PI + (A3_PI_2 - orientation);
            } else if (orientation >= A3_PI_2 && orientation <= A2_PI) {
                return PI - (orientation - (A3_PI_2));
            }

            return (orientation - PI) + A3_PI_2;
        }
    }
}