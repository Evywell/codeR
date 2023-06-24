namespace RobClient.Game.Entity {
    public class Vector4f {
        public float X
        { get; set; }

        public float Y
        { get; set; }

        public float Z
        { get; set; }

        public float O
        { get; set; }

        public Vector4f(float x, float y, float z, float o) {
            X = x;
            Y = y;
            Z = z;
            O = o;
        }

        public static Vector4f Zero() {
            return new Vector4f(0, 0, 0, 0);
        }
    }
}
