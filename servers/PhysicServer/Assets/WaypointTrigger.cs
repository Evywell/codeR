using App;
using UnityEngine;

public class WaypointTrigger : MonoBehaviour
{
    private ObjectController _objectController;
    private int _index;

    public void RegisterWaypoint(ObjectController objectController, int index)
    {
        _objectController = objectController;
        _index = index;
    }

    private void OnTriggerEnter(Collider other)
    {
        if (
            !other.CompareTag("Actor") || 
            other.gameObject.GetComponent<ObjectController>().entityGuid != _objectController.entityGuid
        ) {
            return;
        }

        _objectController.OnWaypointEntered(_index);
        Debug.Log($"Triggered {_index} by {_objectController.entityGuid}");
    }
}
