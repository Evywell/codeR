namespace DI
{
    /// <summary>
    /// Immutable configuration for the login sequence.
    /// Values are set from the inspector on GameLifetimeScope and injected where needed.
    /// </summary>
    public readonly struct LoginConfig
    {
        public readonly int UserId;
        public readonly int CharacterId;

        public LoginConfig(int userId, int characterId)
        {
            UserId = userId;
            CharacterId = characterId;
        }
    }
}
