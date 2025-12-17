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
	public void testBonuses() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"execute:mjj",
				codeSamplesPath + "bonuses.mjj"
		});

		String expectedResult = "5\n25\n14\n";
		assertEquals(0, out.exitCode());
		assertEquals(expectedResult, out.output());
		testFile("bonuses", expectedResult);
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

	@Test
	public void testHelloWorldCompilation() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] { "compile", codeSamplesPath + "hello_world.mjj" });
		assertEquals(0, out.exitCode());
		assertEquals("init\npush(\"Hello, World!\")\nwrite\npush(0)\npop\njcstop\n", out.output());
	}

	@Test
	public void testConditionscompilation() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"compile",
				codeSamplesPath + "conditions_test.mjj"
		});
		assertEquals(0, out.exitCode());
		assertEquals(
				"init\npush(0)\nnew(x, int, var, 0)\npush(0)\nnew(y, int, var, 0)\npush(5)\nstore(x)\npush(0)\nstore(y)\nload(x)\npush(3)\nsup\nif(17)\npush(20)\nstore(y)\ngoto(19)\npush(10)\nstore(y)\nload(y)\nwrite\npush(0)\nswap\npop\nswap\npop\npop\njcstop\n",
				out.output());

	}

	@Test
	public void testFactCompilation() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"compile",
				codeSamplesPath + "fact.mjj"
		});
		assertEquals(0, out.exitCode());
		assertEquals(
				"init\npush(7)\nnew(x, int, var, 0)\npush(7)\nnew(pause, void, meth, 0)\ngoto(14)\npush(0)\nnew(i, int, var, 0)\nswap\npop\npush(0)\nswap\nreturn\npush(17)\nnew(fact, int, meth, 0)\ngoto(41)\nnew(x,int,var,1)\npush(0)\nnew(retour, int, var, 0)\nload(x)\npush(0)\ncmp\nif(34)\nload(x)\nload(x)\npush(1)\nsub\ninvoke(fact)\nswap\npop\nmul\nstore(retour)\ngoto(36)\npush(1)\nstore(retour)\nload(retour)\nswap\npop\nswap\nreturn\nload(x)\ninvoke(fact)\nswap\npop\nnew(res, int, var, 0)\npush(\"x(5040) = \")\nwrite\nload(res)\nwriteln\ninvoke(pause)\npop\npush(0)\nswap\npop\nswap\npop\nswap\npop\nswap\npop\npop\njcstop\n",
				out.output());
	}

	@Test
	public void testioCompilation() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"compile",
				codeSamplesPath + "io_test.mjj"
		});
		assertEquals(0, out.exitCode());
		assertEquals(
				"init\npush(0)\nnew(a, int, var, 0)\npush(0)\nnew(b, int, var, 0)\npush(0)\nnew(c, int, var, 0)\npush(5)\nstore(a)\npush(10)\nstore(b)\nload(a)\nload(b)\nadd\nstore(c)\npush(\"Result:\")\nwrite\nload(a)\nwrite\npush(\"+\")\nwrite\nload(b)\nwrite\npush(\"=\")\nwrite\nload(c)\nwrite\npush(0)\nswap\npop\nswap\npop\nswap\npop\npop\njcstop\n",
				out.output());
	}

	@Test
	public void testmethodsCompilation() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] {
				"compile",
				codeSamplesPath + "methods_test.mjj"
		});
		assertEquals(0, out.exitCode());
		assertEquals(
				"init\npush(5)\nnew(add, int, meth, 0)\ngoto(18)\nnew(x,int,var,2)\nnew(y,int,var,1)\npush(0)\nnew(z, int, var, 0)\nload(x)\nload(y)\nadd\nstore(z)\nload(z)\nswap\npop\nswap\nreturn\npush(0)\nnew(result, int, var, 0)\npush(5)\npush(7)\ninvoke(add)\nswap\npop\nswap\npop\nstore(result)\nload(result)\nwrite\npush(0)\nswap\npop\nswap\npop\npop\njcstop\n",
				out.output());
	}

	@Test
	public void testvarCompilation() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] { "compile", codeSamplesPath + "variables_test.mjj" });
		assertEquals(0, out.exitCode());
		assertEquals(
				"init\npush(0)\nnew(a, int, var, 0)\npush(0)\nnew(b, int, var, 0)\npush(10)\nstore(a)\npush(20)\nstore(b)\nload(a)\nwrite\nload(b)\nwrite\npush(0)\nswap\npop\nswap\npop\npop\njcstop\n",
				out.output());
	}

	@Test
	public void testwhileCompilation() throws IOException, InterruptedException {
		ProgramOutput out = executeProgram(new String[] { "compile", codeSamplesPath + "while_test.mjj" });
		assertEquals(0, out.exitCode());
		assertEquals(
				"init\npush(0)\nnew(i, int, var, 0)\npush(0)\nnew(max, int, var, 0)\npush(5)\nstore(max)\npush(1)\nstore(i)\nload(max)\nload(i)\nsup\nnot\nif(18)\npush(1)\ninc(i)\ngoto(10)\nload(i)\nwrite\npush(0)\nswap\npop\nswap\npop\npop\njcstop\n",
				out.output());
	}
}
