using UnityEngine;

namespace Game.Ability
{
    [CreateAssetMenu(fileName = "AbilityDefinition", menuName = "Game/Ability/Ability Definition")]
    public class AbilityDefinition : ScriptableObject
    {
        public uint AbilityId;
        public int CastDurationMs;
    }
}