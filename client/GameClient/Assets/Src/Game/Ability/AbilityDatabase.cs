using UnityEngine;

namespace Game.Ability
{
    [CreateAssetMenu(fileName = "AbilityDatabase", menuName = "Game/Ability/Ability Database")]
    public class AbilityDatabase : ScriptableObject
    {
        [SerializeField] private AbilityDefinition[] _definitions;

        public AbilityDefinition GetDefinition(uint abilityId)
        {
            foreach (var def in _definitions)
            {
                if (def.AbilityId == abilityId)
                {
                    return def;
                }
            }

            return null;
        }
    }
}