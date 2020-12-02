package fr.rob.core.console.output;

import fr.rob.core.console.Console;
import fr.rob.core.console.event.ConsoleEvent;
import fr.rob.core.console.event.WriteConsoleEvent;

public class ConsoleOutput extends Output {

    private Console console;

    public ConsoleOutput(Console console) {
        this.console = console;
    }

    @Override
    public void write(String message, boolean newline) {
        super.write(message, newline);
        console.getDispatcher().dispatch(ConsoleEvent.AFTER_WRITE, new WriteConsoleEvent(message, console));
    }
}
