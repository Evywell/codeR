package fr.rob.game.network.event

import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventListenerInterface
import fr.rob.game.GameServerSupervisorApplication
import fr.rob.game.network.netty.NettyGameServer

class NewGameNodeForOrchestratorListener(private val gameServerSupervisorApplication: GameServerSupervisorApplication) :
    EventListenerInterface {

    override fun process(event: EventInterface) {
        event as GameNodeStarted
        event.server as NettyGameServer

        gameServerSupervisorApplication.notifyOrchestratorForNewGameNode(event.server.name, event.server.getPort())
    }
}
