package fr.rob.core.console.event;

import fr.rob.core.console.Console;

public class ConsoleEvent {

    public static final int
            AFTER_WRITE = 1;

    protected Console console;

    public ConsoleEvent(Console console) {
        this.console = console;
    }

    public Console getConsole() {
        return console;
    }
}
