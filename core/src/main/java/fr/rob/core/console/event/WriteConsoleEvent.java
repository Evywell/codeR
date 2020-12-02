package fr.rob.core.console.event;

import fr.rob.core.console.Console;

public class WriteConsoleEvent extends ConsoleEvent {

    private String message;

    public WriteConsoleEvent(String message, Console console) {
        super(console);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
