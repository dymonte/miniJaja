package fr.umlp.m1info.projectvms.group2.visitor;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Utility class for working with the MiniJaja parse tree when building the AST
 *
 * These utilities are mainly used when creating AST nodes so that each node can
 * carry precise source location metadata for error reporting, debugging, or
 * semantic analysis
 */
public class VisitorUtils {
	private VisitorUtils() {
	}

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

	/**
	 * Generic method to build a left-recursive AST list chain (Cons/Nil pattern)
	 *
	 * @param <C>            The ANTLR context type
	 * @param <E>            The AST node type
	 * @param <L>            The List node type
	 * @param contexts       The list of parse rule contexts
	 * @param elementBuilder A function that converts a context into an ASTNode
	 * @param chainFactory   A function that wraps (head, tail) into a chain node
	 * @param nilSupplier    A supplier that returns the Nil node
	 * @return The root AST list node
	 */
	public static <C, E, L> L build(List<C> contexts, Function<C, E> elementBuilder,
			BiFunction<E, L, L> chainFactory, Supplier<L> nilSupplier) {
		L result = nilSupplier.get();
		for (int i = contexts.size() - 1; i >= 0; i--) {
			E element = elementBuilder.apply(contexts.get(i));
			result = chainFactory.apply(element, result);
		}
		return result;
	}
}
