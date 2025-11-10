package fr.umlp.m1info.projectvms.group2.utils;

import fr.umlp.m1info.projectvms.group2.logger.ParserLogger;

import fr.umlp.m1info.projectvms.group2.visitor.MiniJajaBuildVisitor;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

import fr.umlp.m1info.projectvms.group2.parser.MiniJajaLexer;
import fr.umlp.m1info.projectvms.group2.parser.MiniJajaParser;

import fr.umlp.m1info.projectvms.group2.visitor.JajaCodeBuildVisitor;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.Instr;

import fr.umlp.m1info.projectvms.group2.parser.JajaCodeLexer;
import fr.umlp.m1info.projectvms.group2.parser.JajaCodeParser;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Utility class for parsing Minijaja source code from strings or files
 */
public class ParserUtils {
	private static ParserLogger logger = ParserLogger.getInstance();

	private ParserUtils() {
	}

	/**
	 * Parse a MiniJaja source code from a string input
	 *
	 * @param inputStr the Minijaja source code as a string
	 * @return the root ASTNode of the parsed AST
	 */
	public static ASTNode parseString(String inputStr) {
		logger.debug("Starting miniJaja parsing from string");

		CharStream input = CharStreams.fromString(inputStr);
		return parseMiniJaja(input);
	}

	/**
	 * Parser Minijaja source code from a file
	 *
	 * @param fileName the path to the Minijaja source file
	 * @return the root ASTNode of the parsed AST
	 * @throws IOException if reading the file fails
	 */

	public static ASTNode parseFile(String fileName) throws IOException {
		logger.debug("Starting miniJaja parsing for file: " + fileName);

		CharStream input = CharStreams.fromFileName(fileName);
		return parseMiniJaja(input);
	}

	/**
	 * Parser JajaCode source code from a file
	 *
	 * @param inputStr the JajaCode source code as a string
	 * @return the list of all instructions of the parsed instrs
	 */
	public static List<Instr> jajaCodeParseString(String inputStr) {
		logger.debug("Starting jajaCode parsing from string");

		CharStream input = CharStreams.fromString(inputStr);
		return parseJajaCode(input);
	}

	/**
	 * Parser JajaCode source code from a file
	 *
	 * @param fileName the path to the JajaCode source file
	 * @return the list of all instructions of the parsed instrs
	 * @throws IOException if reading the file fails
	 */
	public static List<Instr> jajaCodeParseFile(String fileName) throws IOException {
		logger.debug("Starting jajaCode parsing for file: " + fileName);

		CharStream input = CharStreams.fromFileName(fileName);
		return parseJajaCode(input);
	}

	/**
	 * Parse MiniJaja source code from a CharStream
	 *
	 * @param input the CharStream containing Minijaja source
	 * @return the AST root node
	 */
	private static ASTNode parseMiniJaja(CharStream input) {
		logger.debug("Creating miniJaja lexer and parser");

		MiniJajaLexer lexer = new MiniJajaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniJajaParser parser = new MiniJajaParser(tokens);

		lexer.removeErrorListeners();
		parser.removeErrorListeners();

		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
		parser.addErrorListener(ThrowingErrorListener.INSTANCE);

		logger.debug("Parsing input stream");
		ParseTree tree = parser.prog();

		ThrowingErrorListener.INSTANCE.throwIfErrors();
		MiniJajaBuildVisitor builder = new MiniJajaBuildVisitor();
		ASTNode node = builder.visit(tree);

		logger.info("MiniJaja parsing completed successfully");
		return node;
	}

	/**
	 * Parse JajaCode source code from a CharStream
	 *
	 * @param input the CharStream containing JajaCode source
	 * @return the list of instructions
	 */
	private static List<Instr> parseJajaCode(CharStream input) {
		logger.debug("Creating jajaCode lexer and parser");

		JajaCodeLexer lexer = new JajaCodeLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JajaCodeParser parser = new JajaCodeParser(tokens);

		lexer.removeErrorListeners();
		parser.removeErrorListeners();

		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
		parser.addErrorListener(ThrowingErrorListener.INSTANCE);

		logger.debug("Parsing input stream");
		ParseTree tree = parser.prog();

		ThrowingErrorListener.INSTANCE.throwIfErrors();
		JajaCodeBuildVisitor builder = new JajaCodeBuildVisitor();

		logger.info("JajaCode parsing completed successfully");
		builder.visit(tree);
		return builder.getInstructions();
	}
}
