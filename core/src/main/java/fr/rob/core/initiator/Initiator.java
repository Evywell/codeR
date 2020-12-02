package fr.rob.core.initiator;

import java.util.HashMap;
import java.util.Map;

public class Initiator {

    private Map<String, TaskInterface> tasks = new HashMap<>();

    /**
     * Adds a task to the initiator and returns it
     * @param taskName The task name
     * @param task The task object to add
     * @return The initiator with the task newly added
     */
    public Initiator addTask(String taskName, TaskInterface task) {
        tasks.put(taskName, task);

        return this;
    }

    public Initiator runTask(String taskName) {
        tasks.get(taskName).run();

        return this;
    }

}
