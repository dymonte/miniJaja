package fr.umlp.m1info.projectvms.group2.antlr.miniJaja.visitor;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;
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

	/**
	 * Generic method to build a left-recursive AST list chain (Cons/Nil pattern)
	 *
	 * @param <Ctx> The ANTLR context type
	 * @param <Elem> The AST node type
	 * @param contexts The list of parse rule contexts
	 * @param elementBuilder A function that converts a context into an ASTNode
	 * @param chainFactory A function that wraps (head, tail) into a chain node
	 * @param nilSupplier A supplier that returns the Nil node
	 * @return The root AST list node
	 */
	public static <Ctx, Elem, ListNode> ListNode build(List<Ctx> contexts, Function<Ctx, Elem> elementBuilder, BiFunction<Elem, ListNode, ListNode> chainFactory, Supplier<ListNode> nilSupplier) {
		ListNode result = nilSupplier.get();
		for (int i = contexts.size() - 1; i >= 0; i--) {
			Elem element = elementBuilder.apply(contexts.get(i));
			result = chainFactory.apply(element, result);
		}
		return result;
	}
}
