package fr.rob.login.test.feature

abstract class AuthenticatedScenario: Scenario() {

    fun authAs(userId: Int) {
        client.authenticateToServerAs(userId)
    }
}
