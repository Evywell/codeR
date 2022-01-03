## Introduction
In the game project, client packets are handled in two places:
1. When the server asks for an update (50 times / second)
2. When an instance asks for an update

In the first place, the game sessions run every "expensive" packets (limited to 100 per loop) inside their queue. 
"Expensive" packets are the ones received when there is no player in world linked to the session OR the CPU/RAM consuming ones.

All the remaining packets are handled by the instance update loop

In order to know if a packet must be treated in the first or the second place, we use filters.

### What is a filter ?
A filter is a class extending `fr.rob.core.network.Filter` base class. It implements the `process` method that returns true if the subject (here a packet) can be processed, false otherwise.

### GamePacketFilter
For the first case, the used filter is `fr.rob.game.network.GamePacketFilter`. The `process` method returns true if the current session does not have a player or the player is not in the world.
This filter is called inside the update sessions method loop here `fr.rob.game.network.netty.NettyGameServer@updateSessions`

### InstancePacketFilter
TODO 