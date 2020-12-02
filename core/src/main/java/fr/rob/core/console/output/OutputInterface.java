package fr.rob.core.console.output;

public interface OutputInterface {

    void write(String message, boolean newline);
    void write(String message);
    void writeln(String message);

}
