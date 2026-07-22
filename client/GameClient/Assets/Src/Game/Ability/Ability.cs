namespace Game.Ability
{
    public class Ability
    {
        public uint AbilityId { get; }
        public uint AbilityInfoId { get; }
        public ulong SourceGuid { get; }
        public AbilityState State { get; private set; }
        public uint CastingTimeMs { get; }
        public uint ElapsedCastingTimeMs { get; }

        public Ability(
            uint abilityId, 
            uint abilityInfoId, 
            ulong sourceGuid, 
            AbilityState state,
            uint castingTimeMs,
            uint elapsedCastingTimeMs
        )
        {
            AbilityId = abilityId;
            AbilityInfoId = abilityInfoId;
            SourceGuid = sourceGuid;
            State = state;
            CastingTimeMs = castingTimeMs;
            ElapsedCastingTimeMs = elapsedCastingTimeMs;
        }

        public void SetState(AbilityState state)
        {
            State = state;
        }

        public bool IsEnded()
        {
            return State == AbilityState.Done || State == AbilityState.Failed;
        }
    }
}
