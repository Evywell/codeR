package fr.rob.core.console.event;

import java.util.HashMap;
import java.util.Map;

public class Dispatcher {

    private Map<Integer, ConsoleListenerCollection> listeners;

    public Dispatcher() {
        listeners = new HashMap<>();
    }

    public void listen(int eventName, ConsoleListener listener) {
        ConsoleListenerCollection collection;

        collection = this.listeners.containsKey(eventName)
            ? this.listeners.get(eventName)
            : new ConsoleListenerCollection();

        collection.add(listener);

        if (!this.listeners.containsKey(eventName)) {
            this.listeners.put(eventName, collection);
        }
    }

    public void dispatch(int eventName, ConsoleEvent event) {
        if (
            !listeners.containsKey(eventName)
            || listeners.get(eventName).isEmpty()
        ) {
            return;
        }

        for (ConsoleListener listener : listeners.get(eventName)) {
            listener.invoke(event);
        }
    }

}
