using System.Collections;
using Core.Networking.Gateway;
using DI;
using Game.Entity;
using NUnit.Framework;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.TestTools;
using VContainer;

namespace Tests.E2E
{
    /// <summary>
    /// End-to-end test: requires MySQL (migrated + dev seed), the game server and the gateway
    /// (127.0.0.1:11111) to be running before execution.
    /// </summary>
    [TestFixture]
    [Category("E2E")]
    public class PlayerSpawnE2ETest
    {
        private const string BootSceneName = "Boot";
        private const float SpawnTimeoutSeconds = 30f;

        private GameLifetimeScope _scope;

        [UnitySetUp]
        public IEnumerator SetUp()
        {
            AsyncOperation load = SceneManager.LoadSceneAsync(BootSceneName, LoadSceneMode.Single);
            Assert.IsNotNull(load, $"Scene '{BootSceneName}' could not be loaded (missing from Build Settings?)");

            while (!load.isDone)
            {
                yield return null;
            }

            _scope = Object.FindAnyObjectByType<GameLifetimeScope>();
            Assert.IsNotNull(_scope, "GameLifetimeScope not found in Boot scene");
        }

        [UnityTest]
        public IEnumerator PlayerSpawnsInActiveScene()
        {
            EntitySpawner spawner = _scope.Container.Resolve<EntitySpawner>();

            float deadline = Time.realtimeSinceStartup + SpawnTimeoutSeconds;
            while (spawner.PlayerGameObject == null)
            {
                if (Time.realtimeSinceStartup > deadline)
                {
                    Assert.Fail(
                        $"Player did not spawn within {SpawnTimeoutSeconds}s. " +
                        "Are MySQL, the game server and the gateway (127.0.0.1:11111) running?");
                }

                yield return null;
            }

            GameObject player = spawner.PlayerGameObject;

            Assert.AreEqual(SceneManager.GetActiveScene(), player.scene, "Player is not in the active scene");
            Assert.IsTrue(player.activeInHierarchy, "Player is not active in hierarchy");
            Assert.IsNotNull(player.GetComponent<CharacterController>(), "Player has no CharacterController");
        }

        [UnityTearDown]
        public IEnumerator TearDown()
        {
            if (_scope != null)
            {
                // Instances registered via RegisterInstance are not disposed by VContainer:
                // close the gateway socket explicitly to avoid leaking connections between runs.
                GatewayConnection connection = _scope.Container.Resolve<GatewayConnection>();
                var disconnect = connection.Disconnect();
                while (!disconnect.IsCompleted)
                {
                    yield return null;
                }

                connection.Dispose();
                Object.Destroy(_scope.gameObject);
                _scope = null;
            }

            yield return null;
        }
    }
}
