package fr.umlp.m1info.projectvms.group2.cli;

import java.util.function.Consumer;

import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.memory.Memory;
import fr.umlp.m1info.projectvms.group2.memory.heap.Heap;
import fr.umlp.m1info.projectvms.group2.memory.stack.Stack;
import fr.umlp.m1info.projectvms.group2.memory.symtable.SymTable;

/**
 * Hello world!
 *
 */
public class CLI {

    private static void execute(CLIOptions opts) {
        SymTable symTable = new SymTable(opts.getSymTableLength());
        Stack stack = new Stack(opts.getStackDepth());
        Heap heap = new Heap(opts.getHeapSize());
        Memory memory = new Memory(symTable, stack, heap);
        ASTNode root;
        try {
            root = ParserUtils.parseFile(opts.getFilePath());
        } catch (Exception e) {
            System.err.println("Error while interpreting file " + e.getMessage());
        }
    }

    private static void compile(CLIOptions opts) {
    }

    private static boolean handleIntOption(String[] args, int i, Consumer<Integer> setter, String optionName) {
        if (i + 1 >= args.length) {
            System.err.println("Error: Missing value for " + optionName);
            return false;
        }

        try {
            setter.accept(Integer.parseInt(args[++i]));
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid value for " + optionName);
            return false;
        }

        return true;
    }

    private static CLIOptions parseOptions(String[] args) {
        CLIOptions options = new CLIOptions();
        if (args.length == 0) {
            return null;
        }

        options.setCommand(args[0]);

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case CLIOptions.SET_HEAP_SIZE:
                    if (!handleIntOption(args, i, options::setHeapSize, CLIOptions.SET_HEAP_SIZE)) {
                        return null;
                    }
                    i++;
                    break;
                case CLIOptions.SET_STACK_DEPTH:
                    if (!handleIntOption(args, i, options::setStackDepth, CLIOptions.SET_STACK_DEPTH)) {
                        return null;
                    }
                    i++;
                    break;
                case CLIOptions.SET_SYMTABLE_SIZE:
                    if (!handleIntOption(args, i, options::setSymTableLength, CLIOptions.SET_SYMTABLE_SIZE)) {
                        return null;
                    }
                    i++;
                    break;
                default:
                    if (options.getFilePath() == null) {
                        options.setFilePath(args[i]);
                    } else {
                        System.err.println("Error: Unexpected argument: " + args[i]);
                        return null;
                    }
                    break;
            }
        }
        return options;
    }

    private static void showHelp() {
        System.out.println("Usage: compile|execute [options] file");
        System.out.println("Commands:");
        System.out.println("  compile\tCompile the given file");
        System.out.println("  execute\tExecute the given file");
        System.out.println("\nOptions:");
        System.out.println("  " + CLIOptions.SET_HEAP_SIZE + " N\tSet the heap size");
        System.out.println("  " + CLIOptions.SET_STACK_DEPTH + " N\tSet the stack depth");
        System.out.println("  " + CLIOptions.SET_SYMTABLE_SIZE + " N\tSet the Symbol table size");
        System.out.println("  --help\t\tShow this help message");
    }

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
            case "execute":
                execute(options);
                break;
            default:
                System.out.println("Unrecognized CLI command. type --help for help.");
                break;
        }
    }
}
