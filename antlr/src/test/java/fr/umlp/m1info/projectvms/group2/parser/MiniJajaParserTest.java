package fr.umlp.m1info.projectvms.group2.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import fr.umlp.m1info.projectvms.group2.utils.ParserUtils;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

public class MiniJajaParserTest {

	@Test
	public void testFile0() {
		assertThrows(Exception.class, () -> ParserUtils.parseFile("./src/test/resources/samples/0.mjj"));
	}

	@Test
	public void testFile1() {
		assertThrows(Exception.class, () -> ParserUtils.parseFile("./src/test/resources/samples/1.mjj"));
	}

	@Test
	public void testFileFact() {
		assertThrows(Exception.class, () -> ParserUtils.parseFile("./src/test/resources/samples/fact.mjj"));
	}

	@Test
	public void testFileQuickSort() {
		assertThrows(Exception.class, () -> ParserUtils.parseFile("./src/test/resources/samples/quick_sort.mjj"));
	}

	@Test
	public void testFileSynonymie() {
		assertThrows(Exception.class, () -> ParserUtils.parseFile("./src/test/resources/samples/synonymie.mjj"));
	}

	@Test
	public void testFileTas() {
		assertThrows(Exception.class, () -> ParserUtils.parseFile("./src/test/resources/samples/tas.mjj"));
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
	 * expectFailure("{ main { } }");
	 * }
	 * 
	 * @Test
	 * public void testMissingSemicolon() {
	 * expectFailure("class Test { int x main { } }");
	 * }
	 * 
	 * @Test
	 * public void testInvalidType() {
	 * expectFailure("class Test { float x; main { } }");
	 * }
	 * 
	 * @Test
	 * public void testMissingMain() {
	 * expectFailure("class Test { int x; }");
	 * }
	 * 
	 * @Test
	 * public void testMethodMissingParens() {
	 * expectFailure("class Test { void f { } main { } }");
	 * }
	 * 
	 * @Test
	 * public void testInvalidOperator() {
	 * expectFailure("class Test { int x; main { x ** 2; } }");
	 * }
	 * 
	 * @Test
	 * public void testInvalidIncrement() {
	 * expectFailure("class Test { main { 5++; } }");
	 * }
	 * 
	 * @Test
	 * public void testMissingBrace() {
	 * expectFailure("class Test { int x; main { x = 0; ");
	 * }
	 * 
	 * @Test
	 * public void testArrayWithoutSize() {
	 * expectFailure("class Test { int arr[]; main { } }");
	 * }
	 */

	public void testInvalidInput() {
		String input = "class { var ; }";
		assertThrows(Exception.class, () -> ParserUtils.parseString(input));
	}
}
