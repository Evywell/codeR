package sandbox.scenario.benchmark

import fr.rob.core.network.v2.session.Session

interface OnNewConnectionInterface {
    fun call(sessionId: String, session: Session)
}
