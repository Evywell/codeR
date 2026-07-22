using App;
using App.Managers;
using App.Network;
using App.Normalizers;
using Fr.Raven.Proto.Message.Physicbridge;
using UnityEngine;

public class ObjectSpawner : MonoBehaviour
{
    public GameObject Original;
    public ActorsManager actorsManager;

    public void Spawn(ulong entityGuid, Position position) {
        var vec3Position = PositionNormalizer.TransformServerPositionToUnityPosition(position);

        var gameObject = Instantiate(Original, vec3Position, Quaternion.identity);

        gameObject.name = $"entity-{entityGuid}";
        gameObject.GetComponent<TransformSyncOverNetwork>().entityGuid = entityGuid;
        gameObject.GetComponent<ObjectController>().entityGuid = entityGuid;

        actorsManager.RegisterActor(entityGuid, gameObject);
    }
}
