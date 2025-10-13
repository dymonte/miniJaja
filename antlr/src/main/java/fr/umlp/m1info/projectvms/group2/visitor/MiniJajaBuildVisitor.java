package fr.umlp.m1info.projectvms.group2.visitor;

import java.util.function.Supplier;

import fr.umlp.m1info.projectvms.group2.ast.minijaja.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decl.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.decls.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.expr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.exprs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.instrs.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.params.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.type.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.var.*;
import fr.umlp.m1info.projectvms.group2.ast.minijaja.vars.*;

import fr.umlp.m1info.projectvms.group2.parser.MiniJajaBaseVisitor;
import fr.umlp.m1info.projectvms.group2.parser.MiniJajaParser;

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
	public ASTClass visitProg(MiniJajaParser.ProgContext ctx) {
		VisitorUtils.info("Starting AST construction for program");
		VisitorUtils.debug("Visiting program (class) [" + VisitorUtils.line(ctx) + "," + VisitorUtils.column(ctx) + "]");

		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTDecls decls = (ASTDecls) visit(ctx.decls());
		ASTMainMethod main = (ASTMainMethod) visit(ctx.methmain());
		return VisitorUtils.safeBuild("Prog",
				() -> new ASTClass(ident, decls, main, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
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
	public ASTIdent visitIdent(MiniJajaParser.IdentContext ctx) {
		return VisitorUtils.safeBuild("Ident",
				() -> new ASTIdent(ctx.getText(), VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTDecls visitDecls(MiniJajaParser.DeclsContext ctx) {
		return VisitorUtils.safeBuild("Decls", () -> VisitorUtils.<MiniJajaParser.DeclContext, ASTDecl, ASTDecls>build(
				ctx.decl(),
				declCtx -> (ASTDecl) visit(declCtx),
				(head, tail) -> VisitorUtils.safeBuild("DeclsChain",
						() -> new ASTDeclsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx))),
				() -> VisitorUtils.safeBuild("DeclsNil",
						() -> new ASTDeclsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)))));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTVar visitVarDecl(MiniJajaParser.VarDeclContext ctx) {
		return VisitorUtils.safeBuild("VarDecl", () -> (ASTVar) visit(ctx.var()));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTMethod visitMethodDecl(MiniJajaParser.MethodDeclContext ctx) {
		return VisitorUtils.safeBuild("MethodDecl", () -> (ASTMethod) visit(ctx.method()));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTVars visitVars(MiniJajaParser.VarsContext ctx) {
		return VisitorUtils.safeBuild("Vars", () -> VisitorUtils.<MiniJajaParser.VarContext, ASTVar, ASTVars>build(
				ctx.var(),
				varCtx -> (ASTVar) visit(varCtx),
				(head, tail) -> VisitorUtils.safeBuild("VarsChain",
						() -> new ASTVarsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx))),
				() -> VisitorUtils.safeBuild("VarsNil",
						() -> new ASTVarsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)))));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTVarSimple visitVarDeclSimple(MiniJajaParser.VarDeclSimpleContext ctx) {
		ASTTypeMeth typemeth = (ASTTypeMeth) visit(ctx.typemeth());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTVExpr vexpr = (ASTVExpr) visit(ctx.vexp());
		return VisitorUtils.safeBuild("VarDeclSimple",
				() -> new ASTVarSimple(typemeth, ident, vexpr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTVarArray visitVarDeclArray(MiniJajaParser.VarDeclArrayContext ctx) {
		ASTTypeMeth typemeth = (ASTTypeMeth) visit(ctx.typemeth());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("VarDeclArray",
				() -> new ASTVarArray(typemeth, ident, expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTVarConst visitVarDeclFinal(MiniJajaParser.VarDeclFinalContext ctx) {
		ASTType type = (ASTType) visit(ctx.type());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTVExpr vexpr = (ASTVExpr) visit(ctx.vexp());
		return VisitorUtils.safeBuild("VarDeclFinal",
				() -> new ASTVarConst(type, ident, vexpr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTVExpr visitVexp(MiniJajaParser.VexpContext ctx) {
		if (ctx.exp() == null) {
			return null;
		}
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("VExp", () -> new ASTVExpr(expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTMethod visitMethod(MiniJajaParser.MethodContext ctx) {
		ASTTypeMeth typemeth = (ASTTypeMeth) visit(ctx.typemeth());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTParams params = (ASTParams) visit(ctx.params());
		ASTVars vars = (ASTVars) visit(ctx.vars());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return VisitorUtils.safeBuild("Method",
				() -> new ASTMethod(typemeth, ident, params, vars, instrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTMainMethod visitMethmain(MiniJajaParser.MethmainContext ctx) {
		ASTVars vars = (ASTVars) visit(ctx.vars());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return VisitorUtils.safeBuild("MainMethod",
				() -> new ASTMainMethod(vars, instrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTParams visitParams(MiniJajaParser.ParamsContext ctx) {
		return VisitorUtils.safeBuild("Params", () -> VisitorUtils.<MiniJajaParser.ParamContext, ASTParam, ASTParams>build(
				ctx.param(),
				paramCtx -> (ASTParam) visit(paramCtx),
				(head, tail) -> VisitorUtils.safeBuild("ParamsChain",
						() -> new ASTParamsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx))),
				() -> VisitorUtils.safeBuild("ParamsNil",
						() -> new ASTParamsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)))));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTParam visitParam(MiniJajaParser.ParamContext ctx) {
		ASTType type = (ASTType) visit(ctx.type());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		return VisitorUtils.safeBuild("Param",
				() -> new ASTParam(type, ident, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTInstrs visitInstrs(MiniJajaParser.InstrsContext ctx) {
		return VisitorUtils.safeBuild("Instrs", () -> VisitorUtils.<MiniJajaParser.InstrContext, ASTInstr, ASTInstrs>build(
				ctx.instr(),
				instrCtx -> (ASTInstr) visit(instrCtx),
				(head, tail) -> VisitorUtils.safeBuild("InstrsChain",
						() -> new ASTInstrsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx))),
				() -> VisitorUtils.safeBuild("InstrsNil",
						() -> new ASTInstrsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)))));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTAssignmentOp visitAssignment(MiniJajaParser.AssignmentContext ctx) {
		ASTLValue lvalue = (ASTLValue) visit(ctx.ident1());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("Assignment",
				() -> new ASTAssignmentOp(lvalue, expr, AssignmentOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTIncrementOp visitIncrement(MiniJajaParser.IncrementContext ctx) {
		ASTLValue lvalue = (ASTLValue) visit(ctx.ident1());
		return VisitorUtils.safeBuild("Increment",
				() -> new ASTIncrementOp(lvalue, IncrementOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTMethodCallInstr visitCallI(MiniJajaParser.CallIContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExprs exprs = (ASTExprs) visit(ctx.listexp());
		return VisitorUtils.safeBuild("MethodCallInstr",
				() -> new ASTMethodCallInstr(ident, exprs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTReturn visitReturn(MiniJajaParser.ReturnContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("Return",
				() -> new ASTReturn(expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTWrite visitWrite(MiniJajaParser.WriteContext ctx) {
		MiniJajaParser.Ident1Context ident1 = ctx.ident1();
		if (ident1 == null) {
			return VisitorUtils.safeBuild("WriteString", () -> new ASTWrite(
					new ASTStringLiteral(ctx.STRING().getText(), VisitorUtils.line(ctx), VisitorUtils.column(ctx)), false,
					VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
		}
		ASTLValue lvalue = (ASTLValue) visit(ident1);
		return VisitorUtils.safeBuild("WriteValue",
				() -> new ASTWrite(lvalue, false, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTWrite visitWriteln(MiniJajaParser.WritelnContext ctx) {
		MiniJajaParser.Ident1Context ident1 = ctx.ident1();
		if (ident1 == null) {
			return VisitorUtils.safeBuild("WritelnString", () -> new ASTWrite(
					new ASTStringLiteral(ctx.STRING().getText(), VisitorUtils.line(ctx), VisitorUtils.column(ctx)), true,
					VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
		}
		ASTLValue lvalue = (ASTLValue) visit(ident1);
		return VisitorUtils.safeBuild("WritelnValue",
				() -> new ASTWrite(lvalue, true, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTIf visitIf(MiniJajaParser.IfContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		ASTInstrs ifInstrs = (ASTInstrs) visit(ctx.instrs(0));
		ASTInstrs elseInstrs = (ctx.instrs().size() >= 2 ? (ASTInstrs) visit(ctx.instrs(1)) : null);
		return VisitorUtils.safeBuild("If",
				() -> new ASTIf(expr, ifInstrs, elseInstrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTWhile visitWhile(MiniJajaParser.WhileContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return VisitorUtils.safeBuild("While",
				() -> new ASTWhile(expr, instrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTExprs visitListexp(MiniJajaParser.ListexpContext ctx) {
		return VisitorUtils.safeBuild("ListExp", () -> VisitorUtils.<MiniJajaParser.ExpContext, ASTExpr, ASTExprs>build(
				ctx.exp(),
				exprCtx -> (ASTExpr) visit(exprCtx),
				(head, tail) -> VisitorUtils.safeBuild("ExprsChain",
						() -> new ASTExprsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx))),
				() -> VisitorUtils.safeBuild("ExprsNil",
						() -> new ASTExprsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)))));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTUnaryOp visitNotExp(MiniJajaParser.NotExpContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp1());
		return VisitorUtils.safeBuild("NotExp",
				() -> new ASTUnaryOp(expr, UnaryOp.fromSymbol("!"), VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTBinaryOp visitLogicExp(MiniJajaParser.LogicExpContext ctx) {
		ASTExpr left = (ASTExpr) visit(ctx.exp());
		ASTExpr right = (ASTExpr) visit(ctx.exp1());
		return VisitorUtils.safeBuild("LogicExp",
				() -> new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTExpr visitSingleExp1(MiniJajaParser.SingleExp1Context ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp1());
		return expr;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTExpr visitExp2Only(MiniJajaParser.Exp2OnlyContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp2());
		return expr;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTBinaryOp visitCompExp(MiniJajaParser.CompExpContext ctx) {
		ASTExpr left = (ASTExpr) visit(ctx.exp1());
		ASTExpr right = (ASTExpr) visit(ctx.exp2());
		return VisitorUtils.safeBuild("CompExp",
				() -> new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTUnaryOp visitNegExp(MiniJajaParser.NegExpContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.term());
		return VisitorUtils.safeBuild("NegExp",
				() -> new ASTUnaryOp(expr, UnaryOp.fromSymbol("-"), VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTBinaryOp visitAddSubExp(MiniJajaParser.AddSubExpContext ctx) {
		ASTExpr left = (ASTExpr) visit(ctx.exp2());
		ASTExpr right = (ASTExpr) visit(ctx.term());
		return VisitorUtils.safeBuild("AddSubExp",
				() -> new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTExpr visitTermOnly(MiniJajaParser.TermOnlyContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.term());
		return expr;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTBinaryOp visitMulDivExp(MiniJajaParser.MulDivExpContext ctx) {
		ASTExpr left = (ASTExpr) visit(ctx.term());
		ASTExpr right = (ASTExpr) visit(ctx.fact());
		return VisitorUtils.safeBuild("MulDivExp",
				() -> new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTExpr visitFactOnly(MiniJajaParser.FactOnlyContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.fact());
		return expr;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTExpr visitVarRef(MiniJajaParser.VarRefContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.ident1());
		return expr;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTMethodCallExpr visitMethodCall(MiniJajaParser.MethodCallContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExprs exprs = (ASTExprs) visit(ctx.listexp());
		return VisitorUtils.safeBuild("MethodCallExpr",
				() -> new ASTMethodCallExpr(ident, exprs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTLength visitLength(MiniJajaParser.LengthContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		return VisitorUtils.safeBuild("Length",
				() -> new ASTLength(ident, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTBoolLiteral visitBool(MiniJajaParser.BoolContext ctx) {
		return VisitorUtils.safeBuild("Bool",
				() -> new ASTBoolLiteral((ctx.BOOLEAN().getText() == "true" ? true : false), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTIntLiteral visitInt(MiniJajaParser.IntContext ctx) {
		return VisitorUtils.safeBuild("Int",
				() -> new ASTIntLiteral(Integer.parseInt(ctx.NUMBER().getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTExpr visitParenExp(MiniJajaParser.ParenExpContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return expr;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTIdent visitIdentificator(MiniJajaParser.IdentificatorContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		return ident;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTArrayAccess visitArrayAccess(MiniJajaParser.ArrayAccessContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("ArrayAccess",
				() -> new ASTArrayAccess(ident, expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTVoidType visitVoidType(MiniJajaParser.VoidTypeContext ctx) {
		return VisitorUtils.safeBuild("VoidType", () -> new ASTVoidType(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitTypeMeth(MiniJajaParser.TypeMethContext ctx) {
		return visit(ctx.type());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTIntType visitIntType(MiniJajaParser.IntTypeContext ctx) {
		return VisitorUtils.safeBuild("IntType", () -> new ASTIntType(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTBoolType visitBoolType(MiniJajaParser.BoolTypeContext ctx) {
		return VisitorUtils.safeBuild("BoolType", () -> new ASTBoolType(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}
}
