using UnityEngine;

namespace App.Managers {
    public class ActorsManager : MonoBehaviour {
        public GameObject GetEntityByGuid(ulong guid) {
            return GameObject.Find($"entity-{guid}");
        }
    }
}