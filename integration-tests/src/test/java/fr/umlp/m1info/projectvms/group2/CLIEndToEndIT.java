package fr.umlp.m1info.projectvms.group2;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * End-to-end tests for the CLI program.
 */
public class CLIEndToEndIT {

    private final String codeSamplesPath = "src/test/resources/samples/";

    record ProgramOutput(int exitCode, String output) {
    }

    /**
     * Executes the CLI program and returns the output as a string (stderr + stdout)
     * with the exitCode.
     * 
     * @param args the CLI arguments
     * @return the program output and exit code
     * @throws IOException          if an I/O error occurs when creating the
     *                              subprocess or reading its output.
     * @throws InterruptedException if the program is killed while being executed
     */
    private ProgramOutput executeProgram(String[] args) throws IOException, InterruptedException {
        List<String> builderArgs = new ArrayList<>();
        builderArgs.add("java");
        builderArgs.add("-cp");
        builderArgs.add(System.getProperty("java.class.path"));
        builderArgs.add("fr.umlp.m1info.projectvms.group2.cli.CLI");
        builderArgs.addAll(Arrays.asList(args));
        ProcessBuilder pb = new ProcessBuilder(builderArgs);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return new ProgramOutput(process.waitFor(), output.toString());

    }

    @Ignore
    @Test
    public void testCli0() throws IOException, InterruptedException {
        ProgramOutput out = executeProgram(new String[] { "execute:mjj", codeSamplesPath + "hello_world.mjj" });
        assertEquals(0, out.exitCode());
        assertEquals("Hello, World!\n", out.output());
    }

    @Test
    public void testWhile() throws IOException, InterruptedException {
        ProgramOutput out = executeProgram(new String[] { "execute:mjj", codeSamplesPath + "while_test.mjj" });
        assertEquals(0, out.exitCode());
        assertEquals("5\n", out.output());
    }
    @Test
    public void testVariableCreation() throws IOException, InterruptedException {
        ProgramOutput out = executeProgram(new String[] { "execute:mjj", codeSamplesPath + "variables_test.mjj" });
        assertEquals(0, out.exitCode());
        assertEquals("10\n20\n", out.output());
    }

 @Test
 public void testEndToEndConditions() throws IOException, InterruptedException {
     ProgramOutput out = executeProgram(new String[] {
             "execute:mjj",
             codeSamplesPath + "conditions_test.mjj"
     });
     assertEquals(0, out.exitCode());
     assertEquals("10\n", out.output());
 }

    @Ignore
    @Test
    public void testMethodCall() throws IOException, InterruptedException {
        ProgramOutput out = executeProgram(new String[] { "execute:mjj", codeSamplesPath + "methods_test.mjj" });
        System.out.println("DEBUG OUTPUT >>>" + out.output() + "<<<");
        assertEquals(0, out.exitCode());
        assertEquals("12\n", out.output());
    }

    @Ignore
    @Test
    public void testEndToEndIO() throws IOException, InterruptedException {
        ProgramOutput out = executeProgram(new String[] {
                "execute:mjj",
                codeSamplesPath + "io_test.mjj"
        });
        System.out.println("DEBUG OUTPUT >>>" + out.output() + "<<<");

        assertEquals(0, out.exitCode());
        assertEquals("Result:\n 5\n + \n10 \n= \n15\n", out.output());
    }





}
