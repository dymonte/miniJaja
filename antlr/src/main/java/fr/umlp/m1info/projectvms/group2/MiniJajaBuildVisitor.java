package fr.umlp.m1info.projectvms.group2.miniJaja.visitor;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.ASTNode;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTClass;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.ASTDecls;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.ASTMainMethod;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.ASTIdent;

import fr.umlp.m1info.projectvms.group2.parser.MiniJajaBaseVisitor;
import fr.umlp.m1info.projectvms.group2.parser.MiniJajaParser.ClassContext;
import fr.umlp.m1info.projectvms.group2.parser.MiniJajaParser.IdentContext;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * ASTVisitor is responsible for traversing the MiniJaja parse tree (produced by
 * ANTLR) and constructing the corresponding Abstract Syntax Tree (AST)
 *
 * The resulting AST provides a structured, high-level representation of the
 * program that is independent of ANTLR's parse tree. It is used in later
 * compilation stages such as semantic analysis, type checking, interpretation,
 * or code generation
 */
public class MiniJajaBuildVisitor extends MiniJajaBaseVisitor<ASTNode> {

	/**
	 * Visits a class declaration in the MiniJaja parse tree and builds its AST
	 * representation
	 *
	 * @param ctx the parse tree context corresponding to a MiniJaja class
	 * @return an ASTClass node containing the class name, its declarations, the
	 *         main method, and the source position
	 */
	@Override
	public ASTClass visitClass(ClassContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTDecls decls = (ASTDecls) visit(ctx.decls());
		ASTMainMethod main = (ASTMainMethod) visit(ctx.methmain());

		return new ASTClass(ident, decls, main, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits an identifier in the MiniJaja parse tree and builds its AST
	 * representation
	 *
	 * @param ctx the parse tree context corresponding to a MiniJaja identifier
	 * @return an ASTIdent node containing the identifier name and its source
	 *         position
	 */
	@Override
	public ASTIdent visitIdent(IdentContext ctx) {
		String name = ctx.getText();

		return new ASTIdent(name, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}
}
