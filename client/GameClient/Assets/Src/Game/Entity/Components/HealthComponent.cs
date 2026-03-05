namespace Game.Entity.Components
{
    /// <summary>
    /// Health data for an entity. Only attached to entities that receive health updates
    /// from the server.
    /// </summary>
    public class HealthComponent
    {
        public uint Health { get; set; }

        public HealthComponent(uint health)
        {
            Health = health;
        }
    }
}
