package fr.umlp.m1info.projectvms.group2.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

public class MiniJajaParserTest {

	@Test
	public void testFile0() {
		try {
			ParserUtils.parseFile("./src/test/resources/samples/0.mjj");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFile1() {
		try {
			ParserUtils.parseFile("./src/test/resources/samples/1.mjj");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFileFact() {
		try {
			ParserUtils.parseFile("./src/test/resources/samples/fact.mjj");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFileQuickSort() {
		try {
			ParserUtils.parseFile("./src/test/resources/samples/quick_sort.mjj");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFileSynonymie() {
		try {
			ParserUtils.parseFile("./src/test/resources/samples/synonymie.mjj");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFileTas() {
		try {
			ParserUtils.parseFile("./src/test/resources/samples/tas.mjj");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEmptyMain() throws Exception {
		ParserUtils.parseString("class Test { main {} }");
	}

	@Test
	public void testVarDecl() throws Exception {
		ParserUtils.parseString("class Test { int x = 5; main {} }");
	}

	@Test
	public void testFinalVar() throws Exception {
		ParserUtils.parseString("class Test { final int y = 10; main {} }");
	}

	@Test
	public void testArrayVar() throws Exception {
		ParserUtils.parseString("class Test { int arr[10]; main {} }");
	}

	@Test
	public void testVoidMethod() throws Exception {
		ParserUtils.parseString("class Test { void f() {}; main {} }");
	}

	@Test
	public void testIntMethod() throws Exception {
		ParserUtils.parseString("class Test { int f() {}; main {} }");
	}

	@Test
	public void testMethodParams() throws Exception {
		ParserUtils.parseString("class Test { int sum(int a, int b) { return a+b; }; main {} }");
	}

	@Test
	public void testAssignment() throws Exception {
		ParserUtils.parseString("class Test { int x; main { x = 3; } }");
	}

	@Test
	public void testIncDec() throws Exception {
		ParserUtils.parseString("class Test { int x; main { x++; x++; } }");
	}

	@Test
	public void testMethodCall() throws Exception {
		ParserUtils.parseString("class Test { void f() { }; main { f(); } }");
	}

	@Test
	public void testWriteWriteln() throws Exception {
		ParserUtils.parseString("class Test { int x; main { write(x); writeln(\"Hello World\"); } }");
	}

	@Test
	public void testIf() throws Exception {
		ParserUtils.parseString("class Test { int x; main { if (x > 0) { x = 1; }; } }");
	}

	@Test
	public void testIfElse() throws Exception {
		ParserUtils.parseString("class Test { int x; main { if (x > 0) { x = 1; } else { x = 0; }; } }");
	}

	@Test
	public void testWhile() throws Exception {
		ParserUtils.parseString("class Test { int x; main { while (x < 10) { x = x + 1; }; } }");
	}

	@Test
	public void testBooleanExpr() throws Exception {
		ParserUtils.parseString("class Test { boolean b; main { b = true && false || (!true); } }");
	}

	@Test
	public void testArrayAccess() throws Exception {
		ParserUtils.parseString("class Test { int arr[5]; main { arr[2] = 10; } }");
	}

	@Test
	public void testLength() throws Exception {
		ParserUtils.parseString("class Test { int arr[5]; main { int n; n = length(arr); } }");
	}

	@Test
	public void testUnaryMinus() throws Exception {
		ParserUtils.parseString("class Test { int x; main { x = -5; } }");
	}

	@Test
	public void testComments() throws Exception {
		ParserUtils
				.parseString("class Test { //single line\n int x; /* multi-line comments \n \n */ main { x = 0; } }");
	}

	@Test
	public void testComplex() throws Exception {
		ParserUtils.parseString(
				"class Test { int arr[3]; void f(int a) { arr[0] = a; }; main { int x = 5; f(x); if (x>0) { writeln(\"ok\"); }; while(x>0) { x++; }; } }");
	}

	/*
	 * @Test
	 * public void testMissingClass() {
	 * assertThrows(Exception.class, () -> ParserUtils.parseFile("{ main { } }"));
	 * }
	 * 
	 * @Test
	 * public void testMissingSemicolon() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { int x main { } }"));
	 * }
	 * 
	 * @Test
	 * public void testInvalidType() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { float x; main { } }"));
	 * }
	 * 
	 * @Test
	 * public void testMissingMain() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { int x; }"));
	 * }
	 * 
	 * @Test
	 * public void testMethodMissingParens() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { void f { } main { } }"));
	 * }
	 * 
	 * @Test
	 * public void testInvalidOperator() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { int x; main { x ** 2; } }"));
	 * }
	 * 
	 * @Test
	 * public void testInvalidIncrement() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { main { 5++; } }"));
	 * }
	 * 
	 * @Test
	 * public void testMissingBrace() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { int x; main { x = 0; "));
	 * }
	 * 
	 * @Test
	 * public void testArrayWithoutSize() {
	 * assertThrows(Exception.class, () ->
	 * ParserUtils.parseFile("class Test { int arr[]; main { } }"));
	 * }
	 */

	public void testInvalidInput() {
		String input = "class { var ; }";
		try {
			ParserUtils.parseString(input);
			fail("Should have thrown an exception for invalid input");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("mismatched"));
		}
	}
}
