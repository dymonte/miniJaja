package fr.umlp.m1info.projectvms.group2.utils;

import fr.umlp.m1info.projectvms.group2.visitor.MiniJajaBuildVisitor;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;

import fr.umlp.m1info.projectvms.group2.parser.MiniJajaLexer;
import fr.umlp.m1info.projectvms.group2.parser.MiniJajaParser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

/**
 * Utility class for parsing Minijaja source code from strings or files
 */
public class ParserUtils {
	/**
	 * Parse a MiniJaja source code from a string input
	 *
	 * @param inputStr the Minijaja source code as a string
	 * @return the root ASTNode of the parsed AST
	 * @throws Exception if a parsing of lexical error occurs
	 */
	public static ASTNode parseString(String inputStr) throws Exception {
		CharStream input = CharStreams.fromString(inputStr);
		MiniJajaLexer lexer = new MiniJajaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniJajaParser parser = new MiniJajaParser(tokens);
		ParseTree tree = parser.prog();
		MiniJajaBuildVisitor builder = new MiniJajaBuildVisitor();
		return builder.visit(tree);
	}

	/**
	 * Parser Minijaja source code from a file
	 *
	 * @param fileName the path to the Minijaja source file
	 * @return the root ASTNode of the parsed AST
	 * @throws Exception if the file cannot be read or parsing fails
	 */
	public static ASTNode parseFile(String fileName) throws Exception {
		CharStream input = CharStreams.fromFileName(fileName);
		MiniJajaLexer lexer = new MiniJajaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MiniJajaParser parser = new MiniJajaParser(tokens);
		ParseTree tree = parser.prog();
		MiniJajaBuildVisitor builder = new MiniJajaBuildVisitor();
		return builder.visit(tree);
	}
}
