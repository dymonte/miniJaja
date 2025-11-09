package fr.umlp.m1info.projectvms.group2.visitor;

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
	 * Builds the AST for a MiniJaja program
	 *
	 * @param ctx the program context from the parser
	 * @return an ASTClass node representing the class, including its declarations
	 *         and main method
	 */
	@Override
	public ASTClass visitProg(MiniJajaParser.ProgContext ctx) {
		VisitorUtils.info("Starting AST construction for program");
		VisitorUtils
				.debug("Visiting program (class) [" + VisitorUtils.line(ctx) + "," + VisitorUtils.column(ctx) + "]");

		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTDecls decls = (ASTDecls) visit(ctx.decls());
		ASTMainMethod main = (ASTMainMethod) visit(ctx.methmain());
		return VisitorUtils.safeBuild("Prog",
				() -> new ASTClass(ident, decls, main, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits an identifier in the parse tree.
	 *
	 * @param ctx the identifier context
	 * @return an ASTIdent node representing the identifier
	 */
	@Override
	public ASTIdent visitIdent(MiniJajaParser.IdentContext ctx) {
		return VisitorUtils.safeBuild("Ident",
				() -> new ASTIdent(ctx.getText(), VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a sequence of declarations.
	 *
	 * @param ctx the declarations context
	 * @return an ASTDecls node representing a list of declarations
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
	 * Visits a variable declaration.
	 *
	 * @param ctx the variable declaration context
	 * @return an ASTVar node representing the declared variable
	 */
	@Override
	public ASTVar visitVarDecl(MiniJajaParser.VarDeclContext ctx) {
		return VisitorUtils.safeBuild("VarDecl", () -> (ASTVar) visit(ctx.var()));
	}

	/**
	 * Visits a method declaration.
	 *
	 * @param ctx the method declaration context
	 * @return an ASTMethod node representing the method
	 */
	@Override
	public ASTMethod visitMethodDecl(MiniJajaParser.MethodDeclContext ctx) {
		return VisitorUtils.safeBuild("MethodDecl", () -> (ASTMethod) visit(ctx.method()));
	}

	/**
	 * Visits a list of variables.
	 *
	 * @param ctx the variables context
	 * @return an ASTVars node representing a chain of variables
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
	 * Visits a simple variable declaration.
	 *
	 * @param ctx the simple variable declaration context
	 * @return an ASTVarSimple node representing the variable
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
	 * Visits an array variable declaration.
	 *
	 * @param ctx the array variable declaration context
	 * @return an ASTVarArray node representing the variable
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
	 * Visits a final variable declaration.
	 *
	 * @param ctx the final variable declaration context
	 * @return an ASTVarConst node representing the constant
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
	 * Visits an optional expression for a variable initialization.
	 *
	 * @param ctx the optional expression context
	 * @return an ASTVExpr node, or null if no expression is present
	 */
	@Override
	public ASTVExpr visitVexp(MiniJajaParser.VexpContext ctx) {
		if (ctx.exp() == null) {
			return null;
		}
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("VExp",
				() -> new ASTVExpr(expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a method definition.
	 *
	 * @param ctx the method context
	 * @return an ASTMethod node representing the method
	 */
	@Override
	public ASTMethod visitMethod(MiniJajaParser.MethodContext ctx) {
		ASTTypeMeth typemeth = (ASTTypeMeth) visit(ctx.typemeth());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTParams params = (ASTParams) visit(ctx.params());
		ASTVars vars = (ASTVars) visit(ctx.vars());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return VisitorUtils.safeBuild("Method",
				() -> new ASTMethod(typemeth, ident, params, vars, instrs, VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * Visits the main method of a MiniJaja program.
	 *
	 * @param ctx the main method context
	 * @return an ASTMainMethod node representing the main method
	 */
	@Override
	public ASTMainMethod visitMethmain(MiniJajaParser.MethmainContext ctx) {
		ASTVars vars = (ASTVars) visit(ctx.vars());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return VisitorUtils.safeBuild("MainMethod",
				() -> new ASTMainMethod(vars, instrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a list of method parameters.
	 *
	 * @param ctx the parameters context
	 * @return an ASTParams node representing a chain of parameters
	 */
	@Override
	public ASTParams visitParams(MiniJajaParser.ParamsContext ctx) {
		return VisitorUtils.safeBuild("Params",
				() -> VisitorUtils.<MiniJajaParser.ParamContext, ASTParam, ASTParams>build(
						ctx.param(),
						paramCtx -> (ASTParam) visit(paramCtx),
						(head, tail) -> VisitorUtils.safeBuild("ParamsChain",
								() -> new ASTParamsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx))),
						() -> VisitorUtils.safeBuild("ParamsNil",
								() -> new ASTParamsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)))));
	}

	/**
	 * Visits a single method parameter.
	 *
	 * @param ctx the parameter context
	 * @return an ASTParam node representing the parameter
	 */
	@Override
	public ASTParam visitParam(MiniJajaParser.ParamContext ctx) {
		ASTType type = (ASTType) visit(ctx.type());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		return VisitorUtils.safeBuild("Param",
				() -> new ASTParam(type, ident, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a list of instructions.
	 *
	 * @param ctx the instructions context
	 * @return an ASTInstrs node representing a chain of instructions
	 */
	@Override
	public ASTInstrs visitInstrs(MiniJajaParser.InstrsContext ctx) {
		return VisitorUtils.safeBuild("Instrs",
				() -> VisitorUtils.<MiniJajaParser.InstrContext, ASTInstr, ASTInstrs>build(
						ctx.instr(),
						instrCtx -> (ASTInstr) visit(instrCtx),
						(head, tail) -> VisitorUtils.safeBuild("InstrsChain",
								() -> new ASTInstrsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx))),
						() -> VisitorUtils.safeBuild("InstrsNil",
								() -> new ASTInstrsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)))));
	}

	/**
	 * Visits an assignment operation.
	 *
	 * @param ctx the assignment context
	 * @return an ASTAssignmentOp node representing the assignment
	 */
	@Override
	public ASTAssignmentOp visitAssignment(MiniJajaParser.AssignmentContext ctx) {
		ASTLValue lvalue = (ASTLValue) visit(ctx.ident1());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("Assignment",
				() -> new ASTAssignmentOp(lvalue, expr, AssignmentOp.fromSymbol(ctx.op.getText()),
						VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * Visits an increment operation.
	 *
	 * @param ctx the increment context
	 * @return an ASTIncrementOp node representing the increment
	 */
	@Override
	public ASTIncrementOp visitIncrement(MiniJajaParser.IncrementContext ctx) {
		ASTLValue lvalue = (ASTLValue) visit(ctx.ident1());
		return VisitorUtils.safeBuild("Increment",
				() -> new ASTIncrementOp(lvalue, IncrementOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a method call used as an instruction.
	 *
	 * @param ctx the call context
	 * @return an ASTMethodCallInstr node representing the call
	 */
	@Override
	public ASTMethodCallInstr visitCallI(MiniJajaParser.CallIContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExprs exprs = (ASTExprs) visit(ctx.listexp());
		return VisitorUtils.safeBuild("MethodCallInstr",
				() -> new ASTMethodCallInstr(ident, exprs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a return instruction.
	 *
	 * @param ctx the return context
	 * @return an ASTReturn node representing the return statement
	 */
	@Override
	public ASTReturn visitReturn(MiniJajaParser.ReturnContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("Return",
				() -> new ASTReturn(expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a write instruction (without newline).
	 *
	 * @param ctx the write context
	 * @return an ASTWrite node representing the write operation
	 */
	@Override
	public ASTWrite visitWrite(MiniJajaParser.WriteContext ctx) {
		MiniJajaParser.Ident1Context ident1 = ctx.ident1();
		if (ident1 == null) {
			return VisitorUtils.safeBuild("WriteString", () -> new ASTWrite(
					new ASTStringLiteral(ctx.STRING().getText(), VisitorUtils.line(ctx), VisitorUtils.column(ctx)),
					false,
					VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
		}
		ASTLValue lvalue = (ASTLValue) visit(ident1);
		return VisitorUtils.safeBuild("WriteValue",
				() -> new ASTWrite(lvalue, false, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a writeln instruction (with newline).
	 *
	 * @param ctx the writeln context
	 * @return an ASTWrite node representing the writeln operation
	 */
	@Override
	public ASTWrite visitWriteln(MiniJajaParser.WritelnContext ctx) {
		MiniJajaParser.Ident1Context ident1 = ctx.ident1();
		if (ident1 == null) {
			return VisitorUtils.safeBuild("WritelnString", () -> new ASTWrite(
					new ASTStringLiteral(ctx.STRING().getText(), VisitorUtils.line(ctx), VisitorUtils.column(ctx)),
					true,
					VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
		}
		ASTLValue lvalue = (ASTLValue) visit(ident1);
		return VisitorUtils.safeBuild("WritelnValue",
				() -> new ASTWrite(lvalue, true, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits an if statement.
	 *
	 * @param ctx the if context
	 * @return an ASTIf node representing the conditional
	 */
	@Override
	public ASTIf visitIf(MiniJajaParser.IfContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		ASTInstrs ifInstrs = (ASTInstrs) visit(ctx.instrs(0));
		ASTInstrs elseInstrs = (ctx.instrs().size() >= 2 ? (ASTInstrs) visit(ctx.instrs(1))
				: new ASTInstrsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
		return VisitorUtils.safeBuild("If",
				() -> new ASTIf(expr, ifInstrs, elseInstrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a while loop.
	 *
	 * @param ctx the while context
	 * @return an ASTWhile node representing the loop
	 */
	@Override
	public ASTWhile visitWhile(MiniJajaParser.WhileContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return VisitorUtils.safeBuild("While",
				() -> new ASTWhile(expr, instrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a list of expressions.
	 *
	 * @param ctx the list expression context
	 * @return an ASTExprs node representing the chain of expressions
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
	 * Visits a logical NOT expression.
	 *
	 * @param ctx the NOT expression context
	 * @return an ASTUnaryOp node representing the NOT operation
	 */
	@Override
	public ASTUnaryOp visitNotExp(MiniJajaParser.NotExpContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.exp1());
		return VisitorUtils.safeBuild("NotExp",
				() -> new ASTUnaryOp(expr, UnaryOp.fromSymbol("!"), VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a logical expression (AND, OR).
	 *
	 * @param ctx the logical expression context
	 * @return an ASTBinaryOp node representing the logical operation
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
	 * Visits a single exp1 expression.
	 *
	 * @param ctx the single expression context
	 * @return the corresponding ASTExpr node
	 */
	@Override
	public ASTExpr visitSingleExp1(MiniJajaParser.SingleExp1Context ctx) {
		return (ASTExpr) visit(ctx.exp1());
	}

	/**
	 * Visits an exp2-only expression.
	 *
	 * @param ctx the exp2-only context
	 * @return the corresponding ASTExpr node
	 */
	@Override
	public ASTExpr visitExp2Only(MiniJajaParser.Exp2OnlyContext ctx) {
		return (ASTExpr) visit(ctx.exp2());
	}

	/**
	 * Visits a comparison expression.
	 *
	 * @param ctx the comparison expression context
	 * @return an ASTBinaryOp node representing the comparison
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
	 * Visits a negation expression.
	 *
	 * @param ctx the negation expression context
	 * @return an ASTUnaryOp node representing the negation
	 */
	@Override
	public ASTUnaryOp visitNegExp(MiniJajaParser.NegExpContext ctx) {
		ASTExpr expr = (ASTExpr) visit(ctx.term());
		return VisitorUtils.safeBuild("NegExp",
				() -> new ASTUnaryOp(expr, UnaryOp.fromSymbol("-"), VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits an addition or subtraction expression.
	 *
	 * @param ctx the add/subtract expression context
	 * @return an ASTBinaryOp node representing the addition or subtraction
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
	 * Visits a term-only expression.
	 *
	 * @param ctx the term-only context
	 * @return the corresponding ASTExpr node
	 */
	@Override
	public ASTExpr visitTermOnly(MiniJajaParser.TermOnlyContext ctx) {
		return (ASTExpr) visit(ctx.term());
	}

	/**
	 * Visits a multiplication/division expression.
	 *
	 * @param ctx the multiplication/division expression context
	 * @return an ASTBinaryOp node representing the multiplication/division
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
	 * Visits a fact-only expression.
	 *
	 * @param ctx the fact-only context
	 * @return the corresponding ASTExpr node
	 */
	@Override
	public ASTExpr visitFactOnly(MiniJajaParser.FactOnlyContext ctx) {
		return (ASTExpr) visit(ctx.fact());
	}

	/**
	 * Visits a variable reference expression.
	 *
	 * @param ctx the variable reference context
	 * @return the corresponding ASTExpr node
	 */
	@Override
	public ASTExpr visitVarRef(MiniJajaParser.VarRefContext ctx) {
		return (ASTExpr) visit(ctx.ident1());
	}

	/**
	 * Visits a method call expression.
	 *
	 * @param ctx the method call context
	 * @return an ASTMethodCallExpr node representing the call
	 */
	@Override
	public ASTMethodCallExpr visitMethodCall(MiniJajaParser.MethodCallContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExprs exprs = (ASTExprs) visit(ctx.listexp());
		return VisitorUtils.safeBuild("MethodCallExpr",
				() -> new ASTMethodCallExpr(ident, exprs, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a length expression on an array.
	 *
	 * @param ctx the length context
	 * @return an ASTLength node representing the length operation
	 */
	@Override
	public ASTLength visitLength(MiniJajaParser.LengthContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		return VisitorUtils.safeBuild("Length",
				() -> new ASTLength(ident, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a boolean literal.
	 *
	 * @param ctx the boolean context
	 * @return an ASTBoolLiteral node representing the boolean value
	 */
	@Override
	public ASTBoolLiteral visitBool(MiniJajaParser.BoolContext ctx) {
		return VisitorUtils.safeBuild("Bool",
				() -> new ASTBoolLiteral((ctx.getText().equals("true")), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * Visits an integer literal.
	 *
	 * @param ctx the integer context
	 * @return an ASTIntLiteral node representing the integer value
	 */
	@Override
	public ASTIntLiteral visitInt(MiniJajaParser.IntContext ctx) {
		return VisitorUtils.safeBuild("Int",
				() -> new ASTIntLiteral(Integer.parseInt(ctx.NUMBER().getText()), VisitorUtils.line(ctx),
						VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a parenthesized expression.
	 *
	 * @param ctx the parenthesis expression context
	 * @return the corresponding ASTExpr node
	 */
	@Override
	public ASTExpr visitParenExp(MiniJajaParser.ParenExpContext ctx) {
		return (ASTExpr) visit(ctx.exp());
	}

	/**
	 * Visits an identificator reference.
	 *
	 * @param ctx the identificator context
	 * @return an ASTIdent node representing the identifier
	 */
	@Override
	public ASTIdent visitIdentificator(MiniJajaParser.IdentificatorContext ctx) {
		return (ASTIdent) visit(ctx.ident());
	}

	/**
	 * Visits an array access expression.
	 *
	 * @param ctx the array access context
	 * @return an ASTArrayAccess node representing the array access
	 */
	@Override
	public ASTArrayAccess visitArrayAccess(MiniJajaParser.ArrayAccessContext ctx) {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return VisitorUtils.safeBuild("ArrayAccess",
				() -> new ASTArrayAccess(ident, expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a void type node.
	 *
	 * @param ctx the void type context
	 * @return an ASTVoidType node representing void
	 */
	@Override
	public ASTVoidType visitVoidType(MiniJajaParser.VoidTypeContext ctx) {
		return VisitorUtils.safeBuild("VoidType",
				() -> new ASTVoidType(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a method type node.
	 *
	 * @param ctx the method type context
	 * @return the corresponding ASTNode representing the return type
	 */
	@Override
	public ASTNode visitTypeMethod(MiniJajaParser.TypeMethodContext ctx) {
		return visit(ctx.type());
	}

	/**
	 * Visits an integer type node.
	 *
	 * @param ctx the integer type context
	 * @return an ASTIntType node representing int
	 */
	@Override
	public ASTIntType visitIntType(MiniJajaParser.IntTypeContext ctx) {
		return VisitorUtils.safeBuild("IntType",
				() -> new ASTIntType(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a boolean type node.
	 *
	 * @param ctx the boolean type context
	 * @return an ASTBoolType node representing boolean
	 */
	@Override
	public ASTBoolType visitBoolType(MiniJajaParser.BoolTypeContext ctx) {
		return VisitorUtils.safeBuild("BoolType",
				() -> new ASTBoolType(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}
}
