package fr.rob.core.console;

import fr.rob.core.console.event.ConsoleEvent;
import fr.rob.core.console.event.Dispatcher;
import fr.rob.core.console.event.NewOutputListener;
import fr.rob.core.console.input.ConsoleInput;
import fr.rob.core.console.input.InputInterface;
import fr.rob.core.console.output.ConsoleOutput;
import fr.rob.core.console.output.OutputInterface;

public class Console {

    protected InputInterface input;
    protected OutputInterface output;
    private Dispatcher dispatcher;

    public Console() {
        input = new ConsoleInput();
        output = new ConsoleOutput(this);
        dispatcher = new Dispatcher();
        dispatcher.listen(ConsoleEvent.AFTER_WRITE, new NewOutputListener());
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
