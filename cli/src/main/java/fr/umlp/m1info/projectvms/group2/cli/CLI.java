package fr.umlp.m1info.projectvms.group2.cli;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreter;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

/**
 * Command-line interface for the MiniJaJa/JajaCode interpreters and MiniJaJa
 * compiler.
 */
public class CLI {

    /**
     * Executes the MiniJaJa code from the file specified in the options.
     *
     * @param opts the command-line options
     */
    private static void executeMiniJaJa(CLIOptions opts) {
        SymTable symTable = new SymTable(opts.getSymTableLength());
        Stack stack = new Stack(opts.getStackDepth());
        Heap heap = new Heap(opts.getHeapSize());
        Memory memory = new Memory(symTable, stack, heap);
        MiniJajaInterpreter interpreter = new MiniJajaInterpreter(memory);
        try {
            ASTNode root = ParserUtils.parseFile(opts.getFilePath());
            // interpreter.interpret(root); TODO: remove this when interpreter implemented
        } catch (Exception e) {
            System.err.println("Error while interpreting file " + e.getMessage());
        }
    }

    /**
     * Executes the JaJaCode from the file specified in the options.
     *
     * @param opts the command-line options
     */
    private static void executeJaJaCode(CLIOptions opts) {

    }

    /**
     * Compiles the MiniJaJa code into JaJaCode from the file specified in the
     * options.
     *
     * @param opts the command-line options
     */
    private static void compile(CLIOptions opts) {
    }

    /**
     * Handles an integer option from the command line.
     *
     * @param args       the command-line arguments
     * @param i          the index of the option
     * @param setter     the setter for the option
     * @param optionName the name of the option
     * @return true if the option was handled successfully, false otherwise
     */
    private static boolean handleIntOption(String[] args, int i, Consumer<Integer> setter, String optionName) {
        if (i + 1 >= args.length) {
            System.err.println("Error: Missing value for " + optionName);
            return false;
        }

        try {
            setter.accept(Integer.parseInt(args[i + 1]));
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid value for " + optionName);
            return false;
        }

        return true;
    }

    /**
     * Checks if a file exists at the given path.
     *
     * @param filePath the path to the file
     * @return true if the file exists, false otherwise
     */
    private static boolean isFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.exists(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }

    /**
     * Parses the file option from the command line.
     *
     * @param arg     the command-line argument
     * @param options the command-line options
     * @return true if the file option was parsed successfully, false otherwise
     */
    private static boolean parseFileOption(String arg, CLIOptions options) {
        if (options.getFilePath() == null && isFile(arg)) {
            options.setFilePath(arg);
            return true;
        }
        System.err.println("Unexpected CLI Argument or File not found : " + arg);
        return false;
    }

    /**
     * Parses a command-line option.
     *
     * @param args    the command-line arguments
     * @param i       the index of the option
     * @param options the command-line options
     * @return the new index after parsing the option, or -1 if an error occurred
     */
    private static int parseOption(String[] args, int i, CLIOptions options) {
        if (i < 0 || i > args.length) {
            return -1;
        }

        return switch (args[i]) {
            case CLIOptions.SET_HEAP_SIZE ->
                handleIntOption(args, i, options::setHeapSize, CLIOptions.SET_HEAP_SIZE) ? i + 1 : -1;
            case CLIOptions.SET_STACK_DEPTH ->
                handleIntOption(args, i, options::setStackDepth, CLIOptions.SET_STACK_DEPTH) ? i + 1 : -1;
            case CLIOptions.SET_SYMTABLE_SIZE ->
                handleIntOption(args, i, options::setSymTableLength, CLIOptions.SET_SYMTABLE_SIZE) ? i + 1 : -1;
            default -> parseFileOption(args[i], options) ? i : -1;
        };
    }

    /**
     * Parses the command-line options.
     *
     * @param args the command-line arguments
     * @return the parsed command-line options, or null if an error occurred
     */
    private static CLIOptions parseOptions(String[] args) {
        CLIOptions options = new CLIOptions();
        if (args.length == 0) {
            return null;
        }

        options.setCommand(args[0]);

        for (int i = 1; i < args.length; i++) {
            i = parseOption(args, i, options);
            if (i == -1) {
                return null;
            }
        }
        return options;
    }

    /**
     * Shows the help message.
     */
    private static void showHelp() {
        System.out.println("Usage: compile|execute:mjj|execute:jjc [options] file");
        System.out.println("Commands:");
        System.out.println("  compile\tCompile the given miniJaJa file into JaJaCode.");
        System.out.println("  execute:mjj\tExecute the given miniJaJa file.");
        System.out.println("  execute:jjc\tExecute the given JaJaCode file.");
        System.out.println("\nOptions:");
        System.out.println("  " + CLIOptions.SET_HEAP_SIZE + " N\tSet the heap size");
        System.out.println("  " + CLIOptions.SET_STACK_DEPTH + " N\tSet the stack depth");
        System.out.println("  " + CLIOptions.SET_SYMTABLE_SIZE + " N\tSet the Symbol table size");
        System.out.println("  --help\t\tShow this help message");
    }

    /**
     * The main entry point for the CLI.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0 || (args.length > 0 && "--help".equals(args[0]))) {
            showHelp();
            return;
        }

        CLIOptions options = parseOptions(args);

        if (options == null || options.getFilePath() == null) {
            if (options != null) {
                System.err.println("Error: File path is missing.");
            }
            showHelp();
            return;
        }

        switch (options.getCommand()) {
            case "compile":
                compile(options);
                break;
            case "execute:mjj":
                executeMiniJaJa(options);
                break;
            case "execute:jjc":
                executeJaJaCode(options);
                break;
            default:
                System.out.println("Unrecognized CLI command. type --help for help.");
                break;
        }
    }
}
