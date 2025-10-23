package fr.umlp.m1info.projectvms.group2.cli;

import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger.InterpreterListener;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger.MessageLevel;

/**
 * CLIOutput
 *
 * Simple wrapper around standard output and error streams.
 */
public class CLIOutput implements InterpreterListener {

    /**
     * Singleton Holder pattern
     */
    private static class Holder {
        private static final CLIOutput INSTANCE = new CLIOutput();
    }

    /**
     * Gets the singleton instance of the interpreter logger
     *
     * @return the shared InterpreterLogger instance
     */
    public static CLIOutput getInstance() {
        return Holder.INSTANCE;
    }

    private CLIOutput() {
    }

    @SuppressWarnings("java:S106")
    public static void write(String message) {
        System.out.println(message);
    }

    @SuppressWarnings("java:S106")
    public static void info(String message) {
        System.out.println("Info : " + message);
    }

    @SuppressWarnings("java:S106")
    public static void debug(String message) {
        System.out.println("Debug : " + message);
    }

    @SuppressWarnings("java:S106")
    public static void warning(String message) {
        System.out.println("Warning : " + message);
    }

    @SuppressWarnings("java:S106")
    public static void error(String context, String message) {
        System.err.println("Error " + context + ": " + message);
    }

    @SuppressWarnings("java:S106")
    public static void error(String message) {
        System.err.println("Error: " + message);
    }

    @Override
    public void receiveMessage(String message, MessageLevel level) {
        switch (level) {
            case WRITE -> write(message);
            case INFO -> info(message);
            case DEBUG -> debug(message);
            case WARNING -> warning(message);
            case ERROR -> error(message);
            default -> info(message);
        }
    }

}
