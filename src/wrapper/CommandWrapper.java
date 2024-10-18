package wrapper;

import enums.STATEMENT;

/**
 * A wrapper class for the command which can be manipulated
 */
public class CommandWrapper {
    public STATEMENT command;

    public CommandWrapper(STATEMENT command) {
        this.command = command;
    }
}