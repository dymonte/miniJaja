package fr.umlp.m1info.projectvms.group2.cli;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.compilation.MiniJajaCompiler;
import fr.umlp.m1info.projectvms.group2.interpreter.jajacode.JajaCodeInterpreter;
import fr.umlp.m1info.projectvms.group2.interpreter.minijaja.MiniJajaInterpreter;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.logger.ParserLogger;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

/**
 * Command-line interface for the MiniJaJa/JajaCode interpreters and MiniJaJa
 * compiler.
 */
public class CLI {

    private CLI() {
    }

    /**
     * Executes the MiniJaJa code from the file specified in the options.
     *
     * @param opts the command-line options
     */
    private static void executeMiniJaJa(CLIOptions opts) {
        SymTable symTable = new SymTable(opts.getSymTableSize());
        Stack stack = new Stack(opts.getStackDepth());
        Heap heap = new Heap(opts.getHeapSize());
        Memory memory = new Memory(symTable, stack, heap);
        MiniJajaInterpreter interpreter = new MiniJajaInterpreter(memory);
        InterpreterLogger.getInstance().addListener(CLIOutput.getInstance());
        if (opts.getDebug()) {
            ParserLogger.getInstance().addListener(CLIOutput.getInstance());
        }
        try {
            ASTNode root = ParserUtils.parseFile(opts.getFilePath());
            interpreter.interpret(root);
        } catch (Exception e) {
            CLIOutput.error("while interpreting file", e.getMessage());
        }
    }

    /**
     * Executes the JaJaCode from the file specified in the options.
     *
     * @param opts the command-line options
     */
    private static void executeJaJaCode(CLIOptions opts) {
        SymTable symTable = new SymTable(opts.getSymTableSize());
        Stack stack = new Stack(opts.getStackDepth());
        Heap heap = new Heap(opts.getHeapSize());
        Memory memory = new Memory(symTable, stack, heap);
        InterpreterLogger.getInstance().addListener(CLIOutput.getInstance());
        if (opts.getDebug()) {
            ParserLogger.getInstance().addListener(CLIOutput.getInstance());
        }
        try {
            List<Instr> instrs = ParserUtils.jajaCodeParseFile(opts.getFilePath());
            JajaCodeInterpreter interpreter = new JajaCodeInterpreter(memory, instrs);
            for (Instr instr : instrs) {
                interpreter.interpret(instr);
            }
        } catch (Exception e) {
            CLIOutput.error("while interpreting file", e.getMessage());
        }
    }

    /**
     * Compiles the MiniJaJa code into JaJaCode from the file specified in the
     * options.
     *
     * @param opts the command-line options
     */
    private static void compile(CLIOptions opts) {
        StringBuilder output = new StringBuilder();
        MiniJajaCompiler compiler = new MiniJajaCompiler(output);

        try {
            ASTNode root = ParserUtils.parseFile(opts.getFilePath());
            compiler.compile(root);

        } catch (Exception e) {
            CLIOutput.error("while compiling file", e.getMessage());
        }
        if (!opts.isCompilationOutputPath()) {
            CLIOutput.write(output.toString());
            return;
        }
        try (FileWriter writer = new FileWriter(opts.getCompilationOutput())) {
            writer.write(output.toString());
        } catch (IOException e) {
            CLIOutput.error("while writting compiled file", e.getMessage());
        }
    }

    /**
     * Handles an option that has a value from the command line.
     *
     * @param args       the command-line arguments
     * @param i          the index of the option
     * @param setter     the setter for the option
     * @param optionName the name of the option
     * @param parser     the value parser
     * @return true if the option was handled successfully, false otherwise
     */
    private static <T> boolean handleValueOption(String[] args, int i, Consumer<T> setter, String optionName,
            Function<String, T> parser) {
        if (i + 1 >= args.length) {
            CLIOutput.error("Missing value for", optionName);
            return false;
        }

        try {
            T value = parser.apply(args[i + 1]);
            setter.accept(value);
        } catch (NumberFormatException e) {
            CLIOutput.error("Invalid value for", optionName);
            return false;
        }

        return true;
    }

    /**
     * Handles a boolean option from the command line.
     *
     * @param setter the setter for the option
     * @param value  the boolean value to set, i.e. true or false
     * @param i      the index of the option
     */
    private static int handleBooleanOption(Consumer<Boolean> setter, boolean value, int i) {
        setter.accept(value);
        return i + 1;
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
        CLIOutput.error("Unexpected CLI Argument or File not found", arg);
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
                handleValueOption(args, i, options::setHeapSize, CLIOptions.SET_HEAP_SIZE, Integer::parseInt) ? i + 2
                        : -1;
            case CLIOptions.SET_STACK_DEPTH ->
                handleValueOption(args, i, options::setStackDepth, CLIOptions.SET_STACK_DEPTH, Integer::parseInt)
                        ? i + 2
                        : -1;
            case CLIOptions.SET_SYMTABLE_SIZE ->
                handleValueOption(args, i, options::setSymTableSize, CLIOptions.SET_SYMTABLE_SIZE, Integer::parseInt)
                        ? i + 2
                        : -1;
            case CLIOptions.COMPILATION_OUTPUT ->
                handleValueOption(args, i, options::setCompilationOutput, CLIOptions.COMPILATION_OUTPUT, s -> s) ? i + 2
                        : -1;
            case CLIOptions.COMPILATION_OUTPUT_SHORT ->
                handleValueOption(args, i, options::setCompilationOutput, CLIOptions.COMPILATION_OUTPUT_SHORT, s -> s)
                        ? i + 2
                        : -1;
            case CLIOptions.DEBUG -> handleBooleanOption(options::setDebug, true, i);
            default -> parseFileOption(args[i], options) ? i + 1 : -1;
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

        int i = 1;
        while (i < args.length) {
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
        CLIOutput.write("Usage: compile|execute:mjj|execute:jjc [options] file\n");
        CLIOutput.write("Commands:\n");
        CLIOutput.write("  compile\tCompile the given miniJaJa file into JaJaCode.\n");
        CLIOutput.write("  execute:mjj\tExecute the given miniJaJa file.\n");
        CLIOutput.write("  execute:jjc\tExecute the given JaJaCode file.\n");
        CLIOutput.write("\nOptions:\n");
        CLIOutput.write("  " + CLIOptions.COMPILATION_OUTPUT + " | " + CLIOptions.COMPILATION_OUTPUT_SHORT
                + " \n\tSets the compiled file path\n");
        CLIOutput.write("  " + CLIOptions.SET_HEAP_SIZE + " \n\tSet the heap size\n");
        CLIOutput.write("  " + CLIOptions.SET_STACK_DEPTH + " \n\tSet the stack depth\n");
        CLIOutput.write("  " + CLIOptions.SET_SYMTABLE_SIZE + " \n\tSet the Symbol table size\n");
        CLIOutput.write("  " + CLIOptions.DEBUG + " \n\tShow every debug logger messages\n");
        CLIOutput.write("  --help\n\tShow this help message\n");
        CLIOutput.write(
                "\nExamples:\n\texecute:mjj minijajafile.mjj\n\tcompile -o jajacodefile.jjc minijajafile.mjj\n\texecute:jjc jajacodefile.jjc\n");
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
                CLIOutput.error("File path is missing.");
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
                CLIOutput.error("Unrecognized CLI command : " + options.getCommand() + "; type --help for help.");
                break;
        }
    }
}
