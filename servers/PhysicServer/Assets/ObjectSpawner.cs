using App.Network;
using Fr.Raven.Proto.Message.Physicbridge;
using UnityEngine;

public class ObjectSpawner : MonoBehaviour
{
    public GameObject Original;

    public void Spawn(ulong entityGuid, Position position) {
        var vec3Position = new Vector3(position.PosX, position.PosY, position.PosZ);

        var gameObject = Instantiate(Original, vec3Position, Quaternion.identity);

        gameObject.name = $"entity-{entityGuid}";
        gameObject.GetComponent<TransformSyncOverNetwork>().entityGuid = entityGuid;
    }
}
