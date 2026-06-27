namespace Game.Ability
{
    public class Ability
    {
        public ulong SourceGuid { get; }
        public uint AbilityId { get; }
        public AbilityState State { get; private set; }

        public Ability(ulong sourceGuid, uint abilityId, AbilityState state)
        {
            SourceGuid = sourceGuid;
            AbilityId = abilityId;
            State = state;
        }

        public void SetState(AbilityState state)
        {
            State = state;
        }
    }
}
