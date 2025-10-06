package fr.umlp.m1info.projectvms.group2.miniJaja.visitor;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Utility class for working with the MiniJaja parse tree when building the AST
 *
 * These utilities are mainly used when creating AST nodes so that each node can
 * carry precise source location metadata for error reporting, debugging, or
 * semantic analysis
 */
public class VisitorUtils {

	/**
	 * Returns the line number of the first token in the given parser context
	 *
	 * @param ctx the parser context
	 * @return the line number (starting from 1)
	 */
	public static int line(ParserRuleContext ctx) {
		return ctx.start.getLine();
	}

	/**
	 * Retuns the column number of the first token in the given parser context
	 *
	 * @param ctx the parser context
	 * @return the column number (starting from 0)
	 */
	public static int column(ParserRuleContext ctx) {
		return ctx.start.getCharPositionInLine();
	}
}
