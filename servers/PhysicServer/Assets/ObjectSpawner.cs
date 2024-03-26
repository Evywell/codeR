using Fr.Raven.Proto.Message.Game;
using UnityEngine;

public class ObjectSpawner : MonoBehaviour
{
    public GameObject Original;

    public void Spawn(Position position) {
        var vec3Position = new Vector3(position.PosX, position.PosY, position.PosZ);

        Instantiate(Original, vec3Position, Quaternion.identity);
    }
}
