using System;
using System.Collections;
using System.Reflection;
using NUnit.Framework;
using UnityEngine.Networking;
using UnityEngine.TestTools;

namespace Tests.E2E
{
    /// <summary>
    /// Base class for E2E tests: before each test, asks the E2E orchestrator to reset the database
    /// with the fixture declared by <see cref="E2EFixtureAttribute"/> and to restart the game server.
    /// Override the URL with the E2E_ORCHESTRATOR_URL environment variable.
    /// </summary>
    [Category("E2E")]
    public abstract class E2ETestBase
    {
        private const string DefaultOrchestratorUrl = "http://127.0.0.1:18080";
        private const int ResetTimeoutSeconds = 120;

        [UnitySetUp]
        public IEnumerator LoadFixture()
        {
            string fixture = ResolveFixtureName();
            string baseUrl = Environment.GetEnvironmentVariable("E2E_ORCHESTRATOR_URL") ?? DefaultOrchestratorUrl;
            string url = $"{baseUrl.TrimEnd('/')}/fixture?name={UnityWebRequest.EscapeURL(fixture)}";

            using (UnityWebRequest request = UnityWebRequest.PostWwwForm(url, string.Empty))
            {
                request.timeout = ResetTimeoutSeconds;
                yield return request.SendWebRequest();

                if (request.result != UnityWebRequest.Result.Success)
                {
                    Assert.Fail(
                        $"Loading fixture '{fixture}' failed ({request.responseCode} {request.error}): " +
                        $"{request.downloadHandler?.text}. Is the E2E orchestrator running on {baseUrl}?"
                    );
                }
            }
        }

        private string ResolveFixtureName()
        {
            string methodName = TestContext.CurrentContext.Test.MethodName;
            MethodInfo method = methodName == null ? null : GetType().GetMethod(methodName);

            E2EFixtureAttribute attribute =
                method?.GetCustomAttribute<E2EFixtureAttribute>()
                ?? GetType().GetCustomAttribute<E2EFixtureAttribute>(true);

            if (attribute == null)
            {
                Assert.Fail($"{GetType().Name}.{methodName} has no [E2EFixture(\"...\")] attribute");
            }

            return attribute.Name;
        }
    }
}
