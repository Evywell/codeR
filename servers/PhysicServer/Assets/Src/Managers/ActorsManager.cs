using System.Collections.Generic;
using UnityEngine;

namespace App.Managers {
    public class ActorsManager : MonoBehaviour {
        private readonly Dictionary<ulong, GameObject> _actors = new Dictionary<ulong, GameObject>();

        public void RegisterActor(ulong guid, GameObject actor) {
            _actors[guid] = actor;
        }

        public void UnregisterActor(ulong guid) {
            _actors.Remove(guid);
        }

        public GameObject GetEntityByGuid(ulong guid) {
            return _actors.TryGetValue(guid, out var actor) ? actor : null;
        }
    }
}
