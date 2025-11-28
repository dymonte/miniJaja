package fr.umlp.m1info.projectvms.group2;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
		assertEquals("1020\n", out.output());
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

	@Test
	public void testMethodCall() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] { "execute:mjj", codeSamplesPath + "methods_test.mjj" });
		assertEquals(0, out.exitCode());
		assertEquals("12\n", out.output());
	}

	@Test
	public void testEndToEndIO() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"execute:mjj",
				codeSamplesPath + "io_test.mjj"
		});
		assertEquals(0, out.exitCode());
		assertEquals("Result:5+10=15\n", out.output());
	}

	@Test
	public void testPushAndAdd() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "testAddPush.jjc"
		});
		assertEquals(0, out.exitCode());
		assertEquals("11\n", out.output());
	}

	@Test
	public void testNew() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "testSwap.jjc"
		});
		assertEquals(0, out.exitCode());
		assertEquals("1020\n", out.output());
	}

	@Test
	public void testNewVar() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "newvar_test.jjc"
		});
		assertEquals("7\n", out.output());
	}

	@Test
	public void testNewConst() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "newconst_test.jjc"
		});
		assertEquals("99\n", out.output());
	}

	@Test
	public void testNewMethod() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "newmeth_test.jjc"
		});
		assertEquals("3\n", out.output());
	}

	@Test
	public void testNewArray() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "newarray_test.jjc"
		});
		assertEquals("42\n", out.output());
	}

	@Test
	public void testReturn() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "test_return_goto.jjc"
		});
		assertEquals("3\n", out.output());
	}

	@Test
	public void testInvoke() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "testinvoke.jjc"
		});
		assertEquals("2\n", out.output());
	}

	@Test
	public void testload() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "loadTest.jjc"
		});
		assertEquals("10\n", out.output());
	}

	@Test
	public void teststore() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "storeTest.jjc"
		});
		assertEquals("15\n", out.output());
	}

	@Test
	public void testiftrue() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "testiftrue.jjc"
		});
		assertEquals("1\n", out.output());
	}

	@Test
	public void testiffalse() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "testiffalse.jjc"
		});
		assertEquals("0\n", out.output());
	}

	@Test
	public void testinc() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "incTest.jjc"
		});
		assertEquals("10\n", out.output());
	}

	private void testFile(String file, String result) throws IOException, InterruptedException {
		ProgramOutput compileOut = executeProgram(new String[] {
				"compile",
				codeSamplesPath + file + ".mjj"
		});
		assertEquals(0, compileOut.exitCode());

		String jajacode = compileOut.output();
		assertNotNull(jajacode);
		assertFalse(jajacode.isEmpty());

		Path tmpDir = Files.createTempDirectory("jaja-test-");
		Path jjcFile = tmpDir.resolve("out.jjc");
		try {
			Files.writeString(jjcFile, jajacode, StandardCharsets.UTF_8);

			ProgramOutput execOut = executeProgram(new String[] {
					"execute:jjc",
					jjcFile.toString()
			});

			assertEquals(0, execOut.exitCode());
			assertEquals(result, execOut.output());

		} finally {
			try {
				Files.deleteIfExists(jjcFile);
			} catch (IOException ignored) {
			}
			try {
				Files.deleteIfExists(tmpDir);
			} catch (IOException ignored) {
			}
		}
	}

	@Ignore
	@Test
	public void testCompileInterpreter() throws IOException, InterruptedException {
		testFile("0", "x (15) = 15\nx (11) = 11\n");
		testFile("1", "x (8) = 8\nx (11) = 11\n");
		testFile("synonymie", "3 3 3 4 3 3 3 3 3 3 \n7 7 7 7 7 7 7 4 7 7 \n");
	}

	@Test
	public void testainc() throws Exception {
		ProgramOutput out = executeProgram(new String[] {
				"execute:jjc",
				codeSamplesPath + "Testainc.jjc"
		});
		assertEquals("15\n", out.output());
	}

}
