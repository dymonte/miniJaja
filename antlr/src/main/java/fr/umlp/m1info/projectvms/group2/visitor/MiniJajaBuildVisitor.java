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
	 * @throws NullPointerException if arguments of ASTClass is null
	 */
	@Override
	public ASTClass visitProg(MiniJajaParser.ProgContext ctx) throws NullPointerException {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTDecls decls = (ASTDecls) visit(ctx.decls());
		ASTMainMethod main = (ASTMainMethod) visit(ctx.methmain());
		return new ASTClass(ident, decls, main, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits an identifier in the parse tree.
	 *
	 * @param ctx the identifier context
	 * @return an ASTIdent node representing the identifier
	 * @throws NullPointerException if arguments of ASTIdent is null
	 */
	@Override
	public ASTIdent visitIdent(MiniJajaParser.IdentContext ctx) throws NullPointerException {
		return new ASTIdent(ctx.getText(), VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a sequence of declarations.
	 *
	 * @param ctx the declarations context
	 * @return an ASTDecls node representing a list of declarations
	 * @throws NullPointerException if arguments of ASTDecls is null
	 */
	@Override
	public ASTDecls visitDecls(MiniJajaParser.DeclsContext ctx) throws NullPointerException {
		return VisitorUtils.<MiniJajaParser.DeclContext, ASTDecl, ASTDecls>build(
				ctx.decl(),
				declCtx -> (ASTDecl) visit(declCtx),
				(head, tail) -> new ASTDeclsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx)),
				() -> new ASTDeclsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a variable declaration.
	 *
	 * @param ctx the variable declaration context
	 * @return an ASTVar node representing the declared variable
	 * @throws NullPointerException if arguments of ASTVar is null
	 */
	@Override
	public ASTVar visitVarDecl(MiniJajaParser.VarDeclContext ctx) throws NullPointerException {
		return (ASTVar) visit(ctx.var());
	}

	/**
	 * Visits a method declaration.
	 *
	 * @param ctx the method declaration context
	 * @return an ASTMethod node representing the method
	 * @throws NullPointerException if arguments of ASTMethod is null
	 */
	@Override
	public ASTMethod visitMethodDecl(MiniJajaParser.MethodDeclContext ctx) throws NullPointerException {
		return (ASTMethod) visit(ctx.method());
	}

	/**
	 * Visits a list of variables.
	 *
	 * @param ctx the variables context
	 * @return an ASTVars node representing a chain of variables
	 * @throws NullPointerException if arguments of ASTVars is null
	 */
	@Override
	public ASTVars visitVars(MiniJajaParser.VarsContext ctx) {
		return VisitorUtils.<MiniJajaParser.VarContext, ASTVar, ASTVars>build(
				ctx.var(),
				varCtx -> (ASTVar) visit(varCtx),
				(head, tail) -> new ASTVarsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx)),
				() -> new ASTVarsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a simple variable declaration.
	 *
	 * @param ctx the simple variable declaration context
	 * @return an ASTVarSimple node representing the variable
	 * @throws NullPointerException if arguments of ASTVarSimple is null
	 */
	@Override
	public ASTVarSimple visitVarDeclSimple(MiniJajaParser.VarDeclSimpleContext ctx) throws NullPointerException {
		ASTTypeMeth typemeth = (ASTTypeMeth) visit(ctx.typemeth());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTVExpr vexpr = (ASTVExpr) visit(ctx.vexp());
		return new ASTVarSimple(typemeth, ident, vexpr, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits an array variable declaration.
	 *
	 * @param ctx the array variable declaration context
	 * @return an ASTVarArray node representing the variable
	 * @throws NullPointerException if arguments of ASTVarArray is null
	 */
	@Override
	public ASTVarArray visitVarDeclArray(MiniJajaParser.VarDeclArrayContext ctx) throws NullPointerException {
		ASTTypeMeth typemeth = (ASTTypeMeth) visit(ctx.typemeth());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return new ASTVarArray(typemeth, ident, expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a final variable declaration.
	 *
	 * @param ctx the final variable declaration context
	 * @return an ASTVarConst node representing the constant
	 * @throws NullPointerException if arguments of ASTVarConst is null
	 */
	@Override
	public ASTVarConst visitVarDeclFinal(MiniJajaParser.VarDeclFinalContext ctx) throws NullPointerException {
		ASTType type = (ASTType) visit(ctx.type());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTVExpr vexpr = (ASTVExpr) visit(ctx.vexp());
		return new ASTVarConst(type, ident, vexpr, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits an optional expression for a variable initialization.
	 *
	 * @param ctx the optional expression context
	 * @return an ASTVExpr node, or null if no expression is present
	 * @throws NullPointerException if arguments of ASTVExpr is null
	 */
	@Override
	public ASTVExpr visitVexp(MiniJajaParser.VexpContext ctx) throws NullPointerException {
		if (ctx.exp() == null) {
			return null;
		}
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return new ASTVExpr(expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a method definition.
	 *
	 * @param ctx the method context
	 * @return an ASTMethod node representing the method
	 * @throws NullPointerException if arguments of ASTMethod is null
	 */
	@Override
	public ASTMethod visitMethod(MiniJajaParser.MethodContext ctx) throws NullPointerException {
		ASTTypeMeth typemeth = (ASTTypeMeth) visit(ctx.typemeth());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTParams params = (ASTParams) visit(ctx.params());
		ASTVars vars = (ASTVars) visit(ctx.vars());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return new ASTMethod(typemeth, ident, params, vars, instrs, VisitorUtils.line(ctx),
				VisitorUtils.column(ctx));
	}

	/**
	 * Visits the main method of a MiniJaja program.
	 *
	 * @param ctx the main method context
	 * @return an ASTMainMethod node representing the main method
	 * @throws NullPointerException if arguments of ASTMainMethod is null
	 */
	@Override
	public ASTMainMethod visitMethmain(MiniJajaParser.MethmainContext ctx) throws NullPointerException {
		ASTVars vars = (ASTVars) visit(ctx.vars());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return new ASTMainMethod(vars, instrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a list of method parameters.
	 *
	 * @param ctx the parameters context
	 * @return an ASTParams node representing a chain of parameters
	 * @throws NullPointerException if arguments of ASTParams is null
	 */
	@Override
	public ASTParams visitParams(MiniJajaParser.ParamsContext ctx) throws NullPointerException {
		return VisitorUtils.<MiniJajaParser.ParamContext, ASTParam, ASTParams>build(
				ctx.param(),
				paramCtx -> (ASTParam) visit(paramCtx),
				(head, tail) -> new ASTParamsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx)),
				() -> new ASTParamsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a single method parameter.
	 *
	 * @param ctx the parameter context
	 * @return an ASTParam node representing the parameter
	 * @throws NullPointerException if arguments of ASTParam is null
	 */
	@Override
	public ASTParam visitParam(MiniJajaParser.ParamContext ctx) throws NullPointerException {
		ASTType type = (ASTType) visit(ctx.type());
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		return new ASTParam(type, ident, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a list of instructions.
	 *
	 * @param ctx the instructions context
	 * @return an ASTInstrs node representing a chain of instructions
	 * @throws NullPointerException if arguments of ASTInstrs is null
	 */
	@Override
	public ASTInstrs visitInstrs(MiniJajaParser.InstrsContext ctx) throws NullPointerException {
		return VisitorUtils.<MiniJajaParser.InstrContext, ASTInstr, ASTInstrs>build(
				ctx.instr(),
				instrCtx -> (ASTInstr) visit(instrCtx),
				(head, tail) -> new ASTInstrsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx)),
				() -> new ASTInstrsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits an assignment operation.
	 *
	 * @param ctx the assignment context
	 * @return an ASTAssignmentOp node representing the assignment
	 * @throws NullPointerException if arguments of ASTAssignmentOp is null
	 */
	@Override
	public ASTAssignmentOp visitAssignment(MiniJajaParser.AssignmentContext ctx) throws NullPointerException {
		ASTLValue lvalue = (ASTLValue) visit(ctx.ident1());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return new ASTAssignmentOp(lvalue, expr, AssignmentOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
				VisitorUtils.column(ctx));
	}

	/**
	 * Visits an increment operation.
	 *
	 * @param ctx the increment context
	 * @return an ASTIncrementOp node representing the increment
	 * @throws NullPointerException if arguments of ASTIncrementOp is null
	 */
	@Override
	public ASTIncrementOp visitIncrement(MiniJajaParser.IncrementContext ctx) throws NullPointerException {
		ASTLValue lvalue = (ASTLValue) visit(ctx.ident1());
		return new ASTIncrementOp(lvalue, IncrementOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
				VisitorUtils.column(ctx));
	}

	/**
	 * Visits a method call used as an instruction.
	 *
	 * @param ctx the call context
	 * @return an ASTMethodCallInstr node representing the call
	 * @throws NullPointerException if arguments of ASTMethodCallInstr is null
	 */
	@Override
	public ASTMethodCallInstr visitCallI(MiniJajaParser.CallIContext ctx) throws NullPointerException {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExprs exprs = (ASTExprs) visit(ctx.listexp());
		return new ASTMethodCallInstr(ident, exprs, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a return instruction.
	 *
	 * @param ctx the return context
	 * @return an ASTReturn node representing the return statement
	 * @throws NullPointerException if arguments of ASTReturn is null
	 */
	@Override
	public ASTReturn visitReturn(MiniJajaParser.ReturnContext ctx) throws NullPointerException {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return new ASTReturn(expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a write instruction (without newline).
	 *
	 * @param ctx the write context
	 * @return an ASTWrite node representing the write operation
	 * @throws NullPointerException if arguments of ASTWrite is null
	 */
	@Override
	public ASTWrite visitWrite(MiniJajaParser.WriteContext ctx) throws NullPointerException {
		MiniJajaParser.Ident1Context ident1 = ctx.ident1();
		if (ident1 == null) {
			ASTStringLiteral stringLiteral = new ASTStringLiteral(ctx.STRING().getText(), VisitorUtils.line(ctx),
					VisitorUtils.column(ctx));
			return new ASTWrite(stringLiteral, false, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
		}
		ASTLValue lvalue = (ASTLValue) visit(ident1);
		return new ASTWrite(lvalue, false, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a writeln instruction (with newline).
	 *
	 * @param ctx the writeln context
	 * @return an ASTWrite node representing the writeln operation
	 * @throws NullPointerException if arguments of ASTWrite is null
	 */
	@Override
	public ASTWrite visitWriteln(MiniJajaParser.WritelnContext ctx) throws NullPointerException {
		MiniJajaParser.Ident1Context ident1 = ctx.ident1();
		if (ident1 == null) {
			ASTStringLiteral stringLiteral = new ASTStringLiteral(ctx.STRING().getText(), VisitorUtils.line(ctx),
					VisitorUtils.column(ctx));
			return new ASTWrite(stringLiteral, true, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
		}
		ASTLValue lvalue = (ASTLValue) visit(ident1);
		return new ASTWrite(lvalue, true, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits an if statement.
	 *
	 * @param ctx the if context
	 * @return an ASTIf node representing the conditional
	 * @throws NullPointerException if arguments of ASTIf is null
	 */
	@Override
	public ASTIf visitIf(MiniJajaParser.IfContext ctx) throws NullPointerException {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		ASTInstrs ifInstrs = (ASTInstrs) visit(ctx.instrs(0));
		ASTInstrs elseInstrs = (ctx.instrs().size() >= 2 ? (ASTInstrs) visit(ctx.instrs(1))
				: new ASTInstrsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
		return new ASTIf(expr, ifInstrs, elseInstrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a while loop.
	 *
	 * @param ctx the while context
	 * @return an ASTWhile node representing the loop
	 * @throws NullPointerException if arguments of ASTWhile is null
	 */
	@Override
	public ASTWhile visitWhile(MiniJajaParser.WhileContext ctx) throws NullPointerException {
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		ASTInstrs instrs = (ASTInstrs) visit(ctx.instrs());
		return new ASTWhile(expr, instrs, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a list of expressions.
	 *
	 * @param ctx the list expression context
	 * @return an ASTExprs node representing the chain of expressions
	 * @throws NullPointerException if arguments of ASTExprs is null
	 */
	@Override
	public ASTExprs visitListexp(MiniJajaParser.ListexpContext ctx) throws NullPointerException {
		return VisitorUtils.<MiniJajaParser.ExpContext, ASTExpr, ASTExprs>build(
				ctx.exp(),
				exprCtx -> (ASTExpr) visit(exprCtx),
				(head, tail) -> new ASTExprsChain(head, tail, VisitorUtils.line(ctx), VisitorUtils.column(ctx)),
				() -> new ASTExprsNil(VisitorUtils.line(ctx), VisitorUtils.column(ctx)));
	}

	/**
	 * Visits a logical NOT expression.
	 *
	 * @param ctx the NOT expression context
	 * @return an ASTUnaryOp node representing the NOT operation
	 * @throws NullPointerException if arguments of ASTUnaryOp is null
	 */
	@Override
	public ASTUnaryOp visitNotExp(MiniJajaParser.NotExpContext ctx) throws NullPointerException {
		ASTExpr expr = (ASTExpr) visit(ctx.exp1());
		return new ASTUnaryOp(expr, UnaryOp.fromSymbol("!"), VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a logical expression (AND, OR).
	 *
	 * @param ctx the logical expression context
	 * @return an ASTBinaryOp node representing the logical operation
	 * @throws NullPointerException if arguments of ASTBinaryOp is null
	 */
	@Override
	public ASTBinaryOp visitLogicExp(MiniJajaParser.LogicExpContext ctx) throws NullPointerException {
		ASTExpr left = (ASTExpr) visit(ctx.exp());
		ASTExpr right = (ASTExpr) visit(ctx.exp1());
		return new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
				VisitorUtils.column(ctx));
	}

	/**
	 * Visits a single exp1 expression.
	 *
	 * @param ctx the single expression context
	 * @return the corresponding ASTExpr node
	 * @throws NullPointerException if arguments of ASTExpr is null
	 */
	@Override
	public ASTExpr visitSingleExp1(MiniJajaParser.SingleExp1Context ctx) throws NullPointerException {
		return (ASTExpr) visit(ctx.exp1());
	}

	/**
	 * Visits an exp2-only expression.
	 *
	 * @param ctx the exp2-only context
	 * @return the corresponding ASTExpr node
	 * @throws NullPointerException if arguments of ASTExpr is null
	 */
	@Override
	public ASTExpr visitExp2Only(MiniJajaParser.Exp2OnlyContext ctx) throws NullPointerException {
		return (ASTExpr) visit(ctx.exp2());
	}

	/**
	 * Visits a comparison expression.
	 *
	 * @param ctx the comparison expression context
	 * @return an ASTBinaryOp node representing the comparison
	 * @throws NullPointerException if arguments of ASTBinaryOp is null
	 */
	@Override
	public ASTBinaryOp visitCompExp(MiniJajaParser.CompExpContext ctx) throws NullPointerException {
		ASTExpr left = (ASTExpr) visit(ctx.exp1());
		ASTExpr right = (ASTExpr) visit(ctx.exp2());
		return new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
				VisitorUtils.column(ctx));
	}

	/**
	 * Visits a negation expression.
	 *
	 * @param ctx the negation expression context
	 * @return an ASTUnaryOp node representing the negation
	 * @throws NullPointerException if arguments of ASTUnaryOp is null
	 */
	@Override
	public ASTUnaryOp visitNegExp(MiniJajaParser.NegExpContext ctx) throws NullPointerException {
		ASTExpr expr = (ASTExpr) visit(ctx.term());
		return new ASTUnaryOp(expr, UnaryOp.fromSymbol("-"), VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits an addition or subtraction expression.
	 *
	 * @param ctx the add/subtract expression context
	 * @return an ASTBinaryOp node representing the addition or subtraction
	 * @throws NullPointerException if arguments of ASTBinaryOp is null
	 */
	@Override
	public ASTBinaryOp visitAddSubExp(MiniJajaParser.AddSubExpContext ctx) throws NullPointerException {
		ASTExpr left = (ASTExpr) visit(ctx.exp2());
		ASTExpr right = (ASTExpr) visit(ctx.term());
		return new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
				VisitorUtils.column(ctx));
	}

	/**
	 * Visits a term-only expression.
	 *
	 * @param ctx the term-only context
	 * @return the corresponding ASTExpr node
	 * @throws NullPointerException if arguments of ASTExpr is null
	 */
	@Override
	public ASTExpr visitTermOnly(MiniJajaParser.TermOnlyContext ctx) throws NullPointerException {
		return (ASTExpr) visit(ctx.term());
	}

	/**
	 * Visits a multiplication/division expression.
	 *
	 * @param ctx the multiplication/division expression context
	 * @return an ASTBinaryOp node representing the multiplication/division
	 * @throws NullPointerException if arguments of ASTBinaryOp is null
	 */
	@Override
	public ASTBinaryOp visitMulDivExp(MiniJajaParser.MulDivExpContext ctx) throws NullPointerException {
		ASTExpr left = (ASTExpr) visit(ctx.term());
		ASTExpr right = (ASTExpr) visit(ctx.fact());
		return new ASTBinaryOp(left, right, BinaryOp.fromSymbol(ctx.op.getText()), VisitorUtils.line(ctx),
				VisitorUtils.column(ctx));
	}

	/**
	 * Visits a fact-only expression.
	 *
	 * @param ctx the fact-only context
	 * @return the corresponding ASTExpr node
	 * @throws NullPointerException if arguments of ASTExpr is null
	 */
	@Override
	public ASTExpr visitFactOnly(MiniJajaParser.FactOnlyContext ctx) throws NullPointerException {
		return (ASTExpr) visit(ctx.fact());
	}

	/**
	 * Visits a variable reference expression.
	 *
	 * @param ctx the variable reference context
	 * @return the corresponding ASTExpr node
	 * @throws NullPointerException if arguments of ASTExpr is null
	 */
	@Override
	public ASTExpr visitVarRef(MiniJajaParser.VarRefContext ctx) throws NullPointerException {
		return (ASTExpr) visit(ctx.ident1());
	}

	/**
	 * Visits a method call expression.
	 *
	 * @param ctx the method call context
	 * @return an ASTMethodCallExpr node representing the call
	 * @throws NullPointerException if arguments of ASTMethodCallExpr is null
	 */
	@Override
	public ASTMethodCallExpr visitMethodCall(MiniJajaParser.MethodCallContext ctx) throws NullPointerException {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExprs exprs = (ASTExprs) visit(ctx.listexp());
		return new ASTMethodCallExpr(ident, exprs, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a length expression on an array.
	 *
	 * @param ctx the length context
	 * @return an ASTLength node representing the length operation
	 * @throws NullPointerException if arguments of ASTLength is null
	 */
	@Override
	public ASTLength visitLength(MiniJajaParser.LengthContext ctx) throws NullPointerException {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		return new ASTLength(ident, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a boolean literal.
	 *
	 * @param ctx the boolean context
	 * @return an ASTBoolLiteral node representing the boolean value
	 * @throws NullPointerException if arguments of ASTBoolLiteral is null
	 */
	@Override
	public ASTBoolLiteral visitBool(MiniJajaParser.BoolContext ctx) throws NullPointerException {
		Boolean value = ctx.getText().equals("true");
		return new ASTBoolLiteral(value, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits an integer literal.
	 *
	 * @param ctx the integer context
	 * @return an ASTIntLiteral node representing the integer value
	 * @throws NullPointerException if arguments of ASTIntLiteral is null
	 */
	@Override
	public ASTIntLiteral visitInt(MiniJajaParser.IntContext ctx) throws NullPointerException {
		Integer value = Integer.parseInt(ctx.NUMBER().getText());
		return new ASTIntLiteral(value, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a parenthesized expression.
	 *
	 * @param ctx the parenthesis expression context
	 * @return the corresponding ASTExpr node
	 * @throws NullPointerException if arguments of ASTExpr is null
	 */
	@Override
	public ASTExpr visitParenExp(MiniJajaParser.ParenExpContext ctx) throws NullPointerException {
		return (ASTExpr) visit(ctx.exp());
	}

	/**
	 * Visits an identificator reference.
	 *
	 * @param ctx the identificator context
	 * @return an ASTIdent node representing the identifier
	 * @throws NullPointerException if arguments of ASTIdent is null
	 */
	@Override
	public ASTIdent visitIdentificator(MiniJajaParser.IdentificatorContext ctx) throws NullPointerException {
		return (ASTIdent) visit(ctx.ident());
	}

	/**
	 * Visits an array access expression.
	 *
	 * @param ctx the array access context
	 * @return an ASTArrayAccess node representing the array access
	 * @throws NullPointerException if arguments of ASTArrayAccess is null
	 */
	@Override
	public ASTArrayAccess visitArrayAccess(MiniJajaParser.ArrayAccessContext ctx) throws NullPointerException {
		ASTIdent ident = (ASTIdent) visit(ctx.ident());
		ASTExpr expr = (ASTExpr) visit(ctx.exp());
		return new ASTArrayAccess(ident, expr, VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a void type node.
	 *
	 * @param ctx the void type context
	 * @return an ASTVoidType node representing void
	 * @throws NullPointerException if arguments of ASTVoidType is null
	 */
	@Override
	public ASTVoidType visitVoidType(MiniJajaParser.VoidTypeContext ctx) throws NullPointerException {
		return new ASTVoidType(VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a method type node.
	 *
	 * @param ctx the method type context
	 * @return the corresponding ASTNode representing the return type
	 * @throws NullPointerException if arguments of ASTNode is null
	 */
	@Override
	public ASTNode visitTypeMethod(MiniJajaParser.TypeMethodContext ctx) throws NullPointerException {
		return visit(ctx.type());
	}

	/**
	 * Visits an integer type node.
	 *
	 * @param ctx the integer type context
	 * @return an ASTIntType node representing int
	 * @throws NullPointerException if arguments of ASTIntType is null
	 */
	@Override
	public ASTIntType visitIntType(MiniJajaParser.IntTypeContext ctx) throws NullPointerException {
		return new ASTIntType(VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}

	/**
	 * Visits a boolean type node.
	 *
	 * @param ctx the boolean type context
	 * @return an ASTBoolType node representing boolean
	 * @throws NullPointerException if arguments of ASTBoolType is null
	 */
	@Override
	public ASTBoolType visitBoolType(MiniJajaParser.BoolTypeContext ctx) throws NullPointerException {
		return new ASTBoolType(VisitorUtils.line(ctx), VisitorUtils.column(ctx));
	}
}
