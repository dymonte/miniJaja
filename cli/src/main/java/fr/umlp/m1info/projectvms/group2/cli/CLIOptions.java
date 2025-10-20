package fr.umlp.m1info.projectvms.group2.cli;

public class CLIOptions {

    public static final String SET_HEAP_SIZE = "--set-heap-size";
    public static final String SET_STACK_DEPTH = "--set-stack-depth";
    public static final String SET_SYMTABLE_SIZE = "--set-symtable-size";

    private int symTableLength = 256;
    private int heapSize = 1024;
    private int stackDepth = 1024;
    private String filePath;
    private String command;

    public int getSymTableLength() {
        return symTableLength;
    }

    public void setSymTableLength(int symTableLength) {
        this.symTableLength = symTableLength;
    }

    public int getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(int heapSize) {
        this.heapSize = heapSize;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public void setStackDepth(int stackDepth) {
        this.stackDepth = stackDepth;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
