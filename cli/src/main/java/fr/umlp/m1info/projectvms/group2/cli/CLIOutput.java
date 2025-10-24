package fr.umlp.m1info.projectvms.group2.cli;

/**
 * CLIOutput
 *
 * Simple wrapper around standard output and error streams.
 */
public class CLIOutput {

    private CLIOutput() {
    }

    @SuppressWarnings("java:S106")
    public static void info(String message) {
        System.out.println(message);
    }

    @SuppressWarnings("java:S106")
    public static void error(String context, String message) {
        System.err.println("Error " + context + ": " + message);
    }

    @SuppressWarnings("java:S106")
    public static void error(String message) {
        System.err.println("Error: " + message);
    }

}
