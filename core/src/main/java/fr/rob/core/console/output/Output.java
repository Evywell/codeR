package fr.rob.core.console.output;

import java.io.PrintStream;

public class Output implements OutputInterface {

    private PrintStream ps;

    public Output() {
        ps = System.out;
    }

    @Override
    public void write(String message, boolean newline) {
        if (newline) {
            ps.println(message);
        } else {
            ps.print(message);
        }
    }

    @Override
    public void write(String message) {
        write(message, false);
    }

    @Override
    public void writeln(String message) {
        write(message, true);
    }
}
