using UnityEngine;

namespace Game.Ability
{
    [CreateAssetMenu(fileName = "AbilityDatabase", menuName = "Game/Ability/Ability Database")]
    public class AbilityDatabase : ScriptableObject
    {
        [SerializeField] private AbilityDefinition[] _definitions;

        public AbilityDefinition GetDefinition(uint abilityInfoId)
        {
            foreach (var def in _definitions)
            {
                if (def.AbilityInfoId == abilityInfoId)
                {
                    return def;
                }
            }

            return null;
        }
    }
}