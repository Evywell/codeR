package fr.rob.game.test.unit.domain.game.state

import fr.rob.game.app.state.ActionHandlerInterface
import fr.rob.game.app.state.Store
import org.junit.jupiter.api.Test
import org.mockito.Mockito.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class StoreTest {

    @Test
    fun `When dispatching action, the correct handler should be invoked`() {
        // Arrange
        val actionHandlerSpy = spy(ActionHandler())

        val store = Store()
        store.registerHandler(actionHandlerSpy)

        val action = Action(12)

        // Act
        store.dispatch(action)

        // Assert
        verify(actionHandlerSpy, times(1)).invoke(action)
    }

    data class Action(val age: Int)

    open class ActionHandler : ActionHandlerInterface<Action> {
        override fun invoke(action: Action) { }

        override fun getType(): String = Action::class.qualifiedName!!
    }
}
