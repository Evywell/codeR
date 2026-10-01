package fr.rob.game.world

import java.util.concurrent.ConcurrentLinkedQueue

class WorldTaskQueue {
    private val tasks = ConcurrentLinkedQueue<() -> Unit>()
    
    fun enqueue(task: () -> Unit){
        tasks.add(task)
    }
    
    fun dequeue() {
        while (true) {
            val task = tasks.poll() ?: return
            task()
        }
    }
}