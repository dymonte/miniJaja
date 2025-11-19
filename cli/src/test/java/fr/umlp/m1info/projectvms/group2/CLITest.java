package fr.umlp.m1info.projectvms.group2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.umlp.m1info.projectvms.group2.cli.CLI;
import fr.umlp.m1info.projectvms.group2.logger.InterpreterLogger;
import fr.umlp.m1info.projectvms.group2.logger.ParserLogger;

public class CLITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @After
    public void cleanUpLoggers() {
        ParserLogger.getInstance().clearListeners();
        InterpreterLogger.getInstance().clearListeners();
    }

    @Test
    public void testNoArgs() {
        CLI.main(new String[] {});
        Assert.assertTrue(outContent.toString().contains("Usage: compile|execute:mjj|execute:jjc [options] file"));
    }

    @Test
    public void testHelp() {
        CLI.main(new String[] { "--help" });
        Assert.assertTrue(outContent.toString().contains("Usage: compile|execute:mjj|execute:jjc [options] file"));
    }

    @Test
    public void testMissingFile() {
        CLI.main(new String[] { "execute:mjj" });
        Assert.assertTrue(errContent.toString().contains("Error: File path is missing."));
        Assert.assertTrue(outContent.toString().contains("Usage: compile|execute:mjj|execute:jjc [options] file"));
    }

    @Test
    public void testInvalidOption() {
        CLI.main(new String[] { "execute:mjj", "--invalid-option", "file.mjj" });
        Assert.assertTrue(errContent.toString().contains("Unexpected CLI Argument or File not found"));
    }

    @Test
    public void testInvalidOptionValue() {
        CLI.main(new String[] { "execute:mjj", "--set-heap-size", "52.2345", "file.mjj" });
        Assert.assertTrue(errContent.toString().contains("Error Invalid value for: --set-heap-size"));
        CLI.main(new String[] { "execute:mjj", "--set-stack-depth", "52.2345", "file.mjj" });
        Assert.assertTrue(errContent.toString().contains("Error Invalid value for: --set-stack-depth"));
        CLI.main(new String[] { "execute:mjj", "--set-symtable-size", "52.2345", "file.mjj" });
        Assert.assertTrue(errContent.toString().contains("Error Invalid value for: --set-symtable-size"));
    }

    @Test
    public void testMissingValue() {
        CLI.main(new String[] { "execute:mjj", "--set-heap-size" });
        Assert.assertTrue(errContent.toString().contains("Error Missing value for: --set-heap-size"));
        CLI.main(new String[] { "execute:mjj", "--set-stack-depth" });
        Assert.assertTrue(errContent.toString().contains("Error Missing value for: --set-stack-depth"));
        CLI.main(new String[] { "execute:mjj", "--set-symtable-size" });
        Assert.assertTrue(errContent.toString().contains("Error Missing value for: --set-symtable-size"));
    }

    @Test
    public void testEmptyClass() {
        CLI.main(new String[] { "execute:mjj", "--set-heap-size", "512", "--set-stack-depth", "1024",
                "--set-symtable-size", "256",
                "./src/test/resources/empty.mjj" });
        Assert.assertEquals("", outContent.toString());
        Assert.assertEquals("", errContent.toString());
    }

    @Test
    public void testEmptyFile() {
        CLI.main(new String[] { "execute:mjj", "--set-heap-size", "512", "--debug",
                "./src/test/resources/empty_file.mjj" });
        Assert.assertTrue(errContent.toString()
                .contains("Error"));
    }

    @Test
    public void testCompilation() {
        CLI.main(new String[] { "compile", "./src/test/resources/empty.mjj" });
        Assert.assertTrue(outContent.toString().contains("init"));
    }

    @Test
    public void testCompilationOutput() throws IOException {
        Path outputPath = Files.createTempFile("output", ".jjc");
        try {
            CLI.main(new String[] { "compile", "-o", outputPath.toString(), "./src/test/resources/empty.mjj" });
            
            String fileContent = Files.readString(outputPath);
            Assert.assertTrue(fileContent.toString().contains("init"));

            CLI.main(new String[] { "compile", "--compiler-output", outputPath.toString(), "./src/test/resources/empty.mjj" });

            fileContent = Files.readString(outputPath);
            Assert.assertTrue(fileContent.toString().contains("init"));
        } finally {
            Files.deleteIfExists(outputPath);
        }
    }

    @Test
    public void testCompilationEmptyOutputOption() {
        CLI.main(new String[] { "compile", "./src/test/resources/empty.mjj" ,"-o" });
        Assert.assertTrue(errContent.toString().contains("Error Missing value for"));
        CLI.main(new String[] { "compile", "./src/test/resources/empty.mjj", "--compiler-output" });
        Assert.assertTrue(errContent.toString().contains("Error Missing value for"));
    }

    @Test
    public void testJaJaCodeInterpretation() throws IOException {
        CLI.main(new String[] { "execute:jjc", "./src/test/resources/empty.jjc" });
        Assert.assertEquals("", outContent.toString());
        Assert.assertEquals("", errContent.toString());
        CLI.main(new String[] { "execute:jjc", "./src/test/resources/empty.jjc", "--debug" });
        Assert.assertTrue(outContent.toString().contains("Debug"));
        Assert.assertEquals("", errContent.toString());
    }

    @Test
    public void testUnknownCommand() {
        CLI.main(new String[] { "exec:mjj", "./src/test/resources/empty.mjj" });
        Assert.assertTrue(errContent.toString()
                .contains("Unrecognized CLI command"));
    }

    @Test
    public void testFileNotFound() {
        CLI.main(new String[] { "execute:mjj", "--set-heap-size", "512",
                "./src/test/resources/emptyyyyy.mjj" });
        Assert.assertTrue(errContent.toString()
                .contains("Unexpected CLI Argument or File not found: ./src/test/resources/emptyyyyy.mjj"));
    }
}
