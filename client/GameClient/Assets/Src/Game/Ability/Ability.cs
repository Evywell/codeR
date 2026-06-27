namespace Game.Ability
{
    public class Ability
    {
        public uint AbilityId { get; }
        public uint AbilityInfoId { get; }
        public ulong SourceGuid { get; }
        public AbilityState State { get; private set; }

        public Ability(uint abilityId, uint abilityInfoId, ulong sourceGuid, AbilityState state)
        {
            AbilityId = abilityId;
            AbilityInfoId = abilityInfoId;
            SourceGuid = sourceGuid;
            State = state;
        }

        public void SetState(AbilityState state)
        {
            State = state;
        }
    }
}
