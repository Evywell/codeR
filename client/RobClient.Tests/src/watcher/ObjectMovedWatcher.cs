using System.Collections.Concurrent;
using RobClient.Event;
using RobClient.Game;
using RobClient.Game.Event;

namespace RobClient.Tests.Watcher;

public class ObjectMovedWatcher {

    private Dictionary<String, Func<ObjectMovedEvent, Boolean>> _witnesses = new Dictionary<string, Func<ObjectMovedEvent, bool>>();
    private ConcurrentDictionary<String, ObjectMovedEvent> _lastEvents = new ConcurrentDictionary<String, ObjectMovedEvent>();
    private EventDispatcher _eventDispatcher;

    public ObjectMovedWatcher(GameContext gameContext) {
        _eventDispatcher = gameContext.EventDispatcher;
        _eventDispatcher.AddEventHandler(new WatchObjectMovingHandler(this));
    }

    public void DefineWitness(String identifier, Func<ObjectMovedEvent, Boolean> callback) {
        _witnesses.Add(identifier, callback);
    }

    public async Task<ObjectMovedEvent> GetLastEvent(String witnessIdentifier, int timeout = 3000) {
        var retrieveLastEvent = Task.Run(() => {
            waitForResult:
            while (!_lastEvents.ContainsKey(witnessIdentifier)) {
                Task.Delay(50);
            }

            _lastEvents.TryGetValue(witnessIdentifier, out ObjectMovedEvent? sourceEvent);

            if (sourceEvent == null) {
                goto waitForResult;
            }

            return sourceEvent;
        });

        await Task.WhenAny(retrieveLastEvent, Task.Delay(timeout));

        return retrieveLastEvent.Result;
    }

    public void NotifyObjectMoved(ObjectMovedEvent sourceEvent) {
        foreach (var witness in _witnesses)
        {
            if (witness.Value.Invoke(sourceEvent)) {
                _lastEvents.AddOrUpdate(witness.Key, sourceEvent, (_, _) => sourceEvent);
            }
        }
    }
}