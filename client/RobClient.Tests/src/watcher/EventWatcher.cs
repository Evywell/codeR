using System.Collections.Concurrent;
using RobClient.Event;
using RobClient.Game;

namespace RobClient.Tests.Watcher;

public class EventWatcher<T> where T : IEvent {

    private Dictionary<String, Func<T, Boolean>> _witnesses = new Dictionary<string, Func<T, bool>>();
    private ConcurrentDictionary<String, T> _lastEvents = new ConcurrentDictionary<String, T>();
    private EventDispatcher _eventDispatcher;

    public EventWatcher(GameContext gameContext) {
        _eventDispatcher = gameContext.EventDispatcher;
        _eventDispatcher.AddEventHandler(new WatchEventHandler<T>(this));
    }

    public void DefineWitness(String identifier, Func<T, Boolean> callback) {
        _witnesses.Add(identifier, callback);
    }

    public async Task<T> GetLastEvent(String witnessIdentifier, int timeoutMs = 3000) {
        var retrieveLastEvent = Task.Run(() => {
            waitForResult:
            while (!_lastEvents.ContainsKey(witnessIdentifier)) {
                Task.Delay(50);
            }

            _lastEvents.TryGetValue(witnessIdentifier, out T? sourceEvent);

            if (sourceEvent == null) {
                goto waitForResult;
            }

            return sourceEvent;
        });

        if (await Task.WhenAny(retrieveLastEvent, Task.Delay(timeoutMs)) != retrieveLastEvent) {
            throw new Exception("Timeout");
        }

        return retrieveLastEvent.Result;
    }

    public void NotifyObjectMoved(T sourceEvent) {
        foreach (var witness in _witnesses)
        {
            if (witness.Value.Invoke(sourceEvent)) {
                _lastEvents.AddOrUpdate(witness.Key, sourceEvent, (_, _) => sourceEvent);
            }
        }
    }
}