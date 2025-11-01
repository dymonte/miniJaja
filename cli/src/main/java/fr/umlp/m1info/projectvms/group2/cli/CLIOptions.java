package fr.umlp.m1info.projectvms.group2.cli;

/**
 * Represents the command-line options for the CLI.
 */
public class CLIOptions {

    /**
     * Command-line option to set the heap size.
     */
    public static final String SET_HEAP_SIZE = "--set-heap-size";

    /**
     * Command-line option to set the stack depth.
     */
    public static final String SET_STACK_DEPTH = "--set-stack-depth";

    /**
     * Command-line option to set the symbol table size.
     */
    public static final String SET_SYMTABLE_SIZE = "--set-symtable-size";

    /**
     * Command-line option to set the debug output.
     */
    public static final String DEBUG = "--debug";

    private int symTableSize = 256;
    private int heapSize = 1024;
    private int stackDepth = 1024;
    private boolean isDebugging = false;
    private String filePath;
    private String command;

    /**
     * Gets the symbol table length.
     *
     * @return the symbol table length
     */
    public int getSymTableSize() {
        return symTableSize;
    }

    /**
     * Sets the symbol table size.
     *
     * @param symTableSize the new symbol table size
     */
    public void setSymTableSize(int symTableSize) {
        this.symTableSize = symTableSize;
    }

    /**
     * Gets the heap size.
     *
     * @return the heap size
     */
    public int getHeapSize() {
        return heapSize;
    }

    /**
     * Sets the heap size.
     *
     * @param heapSize the new heap size
     */
    public void setHeapSize(int heapSize) {
        this.heapSize = heapSize;
    }

    /**
     * Gets the stack depth.
     *
     * @return the stack depth
     */
    public int getStackDepth() {
        return stackDepth;
    }

    /**
     * Sets the stack depth.
     *
     * @param stackDepth the new stack depth
     */
    public void setStackDepth(int stackDepth) {
        this.stackDepth = stackDepth;
    }

    /**
     * Gets the file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath the new file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the command.
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command.
     *
     * @param command the new command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Gets wether the output shows every debug messages or not
     *
     * @return true is the output shows every debug messages, false otherwise.
     */
    public boolean getDebug() {
        return isDebugging;
    }

    /**
     * Sets the output mode to debug (show every logger messages)
     *
     * @param isDebugging a boolean (true for activating debug mode, false to deactivate)
     */
    public void setDebug(boolean isDebugging) {
        this.isDebugging = isDebugging;
    }
}
