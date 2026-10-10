using System;

namespace Tests.E2E
{
    /// <summary>
    /// Name of the SQL fixture (fixtures/e2e/<name>.sql) loaded by the E2E orchestrator before the test.
    /// A method-level attribute overrides the class-level one.
    /// </summary>
    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Method, Inherited = true)]
    public sealed class E2EFixtureAttribute : Attribute
    {
        public string Name { get; }

        public E2EFixtureAttribute(string name)
        {
            Name = name;
        }
    }
}
