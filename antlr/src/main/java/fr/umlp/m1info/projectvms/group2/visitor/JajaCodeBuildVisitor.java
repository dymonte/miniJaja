package fr.umlp.m1info.projectvms.group2.visitor;

import java.util.List;
import java.util.ArrayList;

import fr.umlp.m1info.projectvms.group2.parser.JajaCodeBaseVisitor;
import fr.umlp.m1info.projectvms.group2.parser.JajaCodeParser;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.JajaCodeNode;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.instr.oper.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.*;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.utils.*;

/**
 * The JajaCodeBuildVisitor is responsible for visiting the parse tree of a
 * JajaCode program and constructing its cerresponding Abstract Syntax Tree
 * (AST) representation
 */
public class JajaCodeBuildVisitor extends JajaCodeBaseVisitor<JajaCodeNode> {

	private final List<Instr> instructions = new ArrayList<>();

	/**
	 * Returns the list of collected Instr instructions in the order they were
	 * visited in the parse tree
	 *
	 * @return the list of JajaCode instructions
	 */
	public List<Instr> getInstructions() {
		return instructions;
	}

	/**
	 * Visits the program.
	 *
	 * @param ctx the program context
	 * @return a JajaCodeNode representing the program
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitProg(JajaCodeParser.ProgContext ctx) throws NullPointerException {
		for (JajaCodeParser.InstrContext instr : ctx.instr()) {
			visit(instr);
		}
		return null;
	}

	/**
	 * Visits an initialization instruction.
	 *
	 * @param ctx the init context
	 * @return a JajaCodeNode for the init instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitInit(JajaCodeParser.InitContext ctx) throws NullPointerException {
		instructions.add(new InstrInit(VisitorUtils.line(ctx)));
		return null;
	}

	/**
	 * Visits a swap instruction.
	 *
	 * @param ctx the swap context
	 * @return a JajaCodeNode for the swap instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitSwap(JajaCodeParser.SwapContext ctx) throws NullPointerException {
		instructions.add(new InstrSwap(VisitorUtils.line(ctx)));
		return visitChildren(ctx);
	}

	/**
	 * Visits a "new" object instruction.
	 *
	 * @param ctx the new context
	 * @return a JajaCodeNode for the new instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitNew(JajaCodeParser.NewContext ctx) throws NullPointerException {
		instructions.add(new InstrNew(
				VisitorUtils.line(ctx),
				(ValIdent) visit(ctx.ident()),
				(VarKind) visit(ctx.kind()),
				(VarType) visit(ctx.type()),
				(ValAddress) visit(ctx.address())));
		return null;
	}

	/**
	 * Visits a "new array" instruction.
	 *
	 * @param ctx the new array context
	 * @return a JajaCodeNode for the new array instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitNewArray(JajaCodeParser.NewArrayContext ctx) throws NullPointerException {
		instructions
				.add(new InstrNewArray(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident()), (VarType) visit(ctx.type())));
		return null;
	}

	/**
	 * Visits a method invoke instruction.
	 *
	 * @param ctx the invoke context
	 * @return a JajaCodeNode for the invoke instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitInvoke(JajaCodeParser.InvokeContext ctx) throws NullPointerException {
		instructions.add(new InstrInvoke(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits a length instruction.
	 *
	 * @param ctx the length context
	 * @return a JajaCodeNode for the length instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitLength(JajaCodeParser.LengthContext ctx) throws NullPointerException {
		instructions.add(new InstrLength(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits a return instruction.
	 *
	 * @param ctx the return context
	 * @return a JajaCodeNode for the return instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitReturn(JajaCodeParser.ReturnContext ctx) throws NullPointerException {
		instructions.add(new InstrReturn(VisitorUtils.line(ctx)));
		return null;
	}

	/**
	 * Visits a write instruction.
	 *
	 * @param ctx the write context
	 * @return a JajaCodeNode for the write instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitWrite(JajaCodeParser.WriteContext ctx) throws NullPointerException {
		instructions.add(new InstrWrite(VisitorUtils.line(ctx)));
		return null;
	}

	/**
	 * Visits a writeln instruction.
	 *
	 * @param ctx the writeln context
	 * @return a JajaCodeNode for the writeln instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitWriteLn(JajaCodeParser.WriteLnContext ctx) throws NullPointerException {
		instructions.add(new InstrWriteln(VisitorUtils.line(ctx)));
		return null;
	}

	/**
	 * Visits a push instruction.
	 *
	 * @param ctx the push context
	 * @return a JajaCodeNode for the push instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitPush(JajaCodeParser.PushContext ctx) throws NullPointerException {
		instructions.add(new InstrPush(VisitorUtils.line(ctx), (Val) visit(ctx.value())));
		return null;
	}

	/**
	 * Visits a pop instruction.
	 *
	 * @param ctx the pop context
	 * @return a JajaCodeNode for the pop instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitPop(JajaCodeParser.PopContext ctx) throws NullPointerException {
		instructions.add(new InstrPop(VisitorUtils.line(ctx)));
		return null;
	}

	/**
	 * Visits a load instruction.
	 *
	 * @param ctx the load context
	 * @return a JajaCodeNode for the load instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitLoad(JajaCodeParser.LoadContext ctx) throws NullPointerException {
		instructions.add(new InstrLoad(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits an aload instruction.
	 *
	 * @param ctx the aload context
	 * @return a JajaCodeNode for the aload instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitALoad(JajaCodeParser.ALoadContext ctx) throws NullPointerException {
		instructions.add(new InstrALoad(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits a store instruction.
	 *
	 * @param ctx the store context
	 * @return a JajaCodeNode for the store instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitStore(JajaCodeParser.StoreContext ctx) throws NullPointerException {
		instructions.add(new InstrStore(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits an astore instruction.
	 *
	 * @param ctx the astore context
	 * @return a JajaCodeNode for the astore instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitAStore(JajaCodeParser.AStoreContext ctx) throws NullPointerException {
		instructions.add(new InstrAStore(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits an if instruction.
	 *
	 * @param ctx the if context
	 * @return a JajaCodeNode for the if instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitIf(JajaCodeParser.IfContext ctx) throws NullPointerException {
		instructions.add(new InstrIf(VisitorUtils.line(ctx), (ValAddress) visit(ctx.address())));
		return null;
	}

	/**
	 * Visits a goto instruction.
	 *
	 * @param ctx the goto context
	 * @return a JajaCodeNode for the goto instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitGoto(JajaCodeParser.GotoContext ctx) throws NullPointerException {
		instructions.add(new InstrGoto(VisitorUtils.line(ctx), (ValAddress) visit(ctx.address())));
		return null;
	}

	/**
	 * Visits an increment instruction.
	 *
	 * @param ctx the increment context
	 * @return a JajaCodeNode for the increment instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitInc(JajaCodeParser.IncContext ctx) throws NullPointerException {
		instructions.add(new InstrInc(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits an array increment instruction.
	 *
	 * @param ctx the ainc context
	 * @return a JajaCodeNode for the array increment instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitAInc(JajaCodeParser.AIncContext ctx) throws NullPointerException {
		instructions.add(new InstrAInc(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
	}

	/**
	 * Visits an operator context.
	 *
	 * @param ctx the operator context
	 * @return a JajaCodeNode for the operator
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitOperator(JajaCodeParser.OperatorContext ctx) throws NullPointerException {
		return visit(ctx.oper());
	}

	/**
	 * Visits a nop instruction.
	 *
	 * @param ctx the nop context
	 * @return a JajaCodeNode for the nop instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitNop(JajaCodeParser.NopContext ctx) throws NullPointerException {
		instructions.add(new InstrNop(VisitorUtils.line(ctx)));
		return null;
	}

	/**
	 * Visits a stop instruction.
	 *
	 * @param ctx the stop context
	 * @return a JajaCodeNode for the stop instruction
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitJcStop(JajaCodeParser.JcStopContext ctx) throws NullPointerException {
		instructions.add(new InstrStop(VisitorUtils.line(ctx)));
		return null;
	}

	/**
	 * Visits a unary operator.
	 *
	 * @param ctx the unary context
	 * @return a JajaCodeNode for the unary operator
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitUnary(JajaCodeParser.UnaryContext ctx) throws NullPointerException {
		return visit(ctx.oper1());
	}

	/**
	 * Visits a binary operator.
	 *
	 * @param ctx the binary context
	 * @return a JajaCodeNode for the binary operator
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitBinary(JajaCodeParser.BinaryContext ctx) throws NullPointerException {
		return visit(ctx.oper2());
	}

	/**
	 * Visits a unary operation.
	 *
	 * @param ctx the oper1 context
	 * @return a JajaCodeNode for the unary operation
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitOper1(JajaCodeParser.Oper1Context ctx) throws NullPointerException {
		instructions.add(new InstrOperUnary(VisitorUtils.line(ctx), JajaCodeUnaryOper.fromSymbol(ctx.getText())));
		return null;
	}

	/**
	 * Visits a binary operation.
	 *
	 * @param ctx the oper2 context
	 * @return a JajaCodeNode for the binary operation
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitOper2(JajaCodeParser.Oper2Context ctx) throws NullPointerException {
		instructions.add(new InstrOperBinary(VisitorUtils.line(ctx), JajaCodeBinaryOper.fromSymbol(ctx.getText())));
		return null;
	}

	/**
	 * Visits an identifier.
	 *
	 * @param ctx the ident context
	 * @return a ValIdent node representing the identifier
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitIdent(JajaCodeParser.IdentContext ctx) throws NullPointerException {
		return new ValIdent(VisitorUtils.line(ctx), ctx.IDENTIFIER().getText());
	}

	/**
	 * Visits an integer value.
	 *
	 * @param ctx the int value context
	 * @return a ValNbre node representing the integer
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitIntValue(JajaCodeParser.IntValueContext ctx) throws NullPointerException {
		return new ValNbre(VisitorUtils.line(ctx), Integer.parseInt(ctx.NUMBER().getText()));
	}

	/**
	 * Visits a boolean value.
	 *
	 * @param ctx the boolean value context
	 * @return a ValBool node representing the boolean
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitBoolValue(JajaCodeParser.BoolValueContext ctx) throws NullPointerException {
		return new ValBool(VisitorUtils.line(ctx), ctx.getText().equals("true"));
	}

	/**
	 * Visits a string value.
	 *
	 * @param ctx the string value context
	 * @return a ValString node representing the string
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitStringValue(JajaCodeParser.StringValueContext ctx) throws NullPointerException {
		return new ValString(VisitorUtils.line(ctx), ctx.STRING().getText());
	}

	/**
	 * Visits a void value.
	 *
	 * @param ctx the void value context
	 * @return a ValNbre node representing void (0)
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitVoidValue(JajaCodeParser.VoidValueContext ctx) throws NullPointerException {
		return new ValNbre(VisitorUtils.line(ctx), 0);
	}

	/**
	 * Visits an address value.
	 *
	 * @param ctx the address context
	 * @return a ValAddress node representing the address
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitAddress(JajaCodeParser.AddressContext ctx) throws NullPointerException {
		return new ValAddress(VisitorUtils.line(ctx), Integer.parseInt(ctx.NUMBER().getText()));
	}

	/**
	 * Visits a type.
	 *
	 * @param ctx the type context
	 * @return a VarType node representing the type
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitType(JajaCodeParser.TypeContext ctx) throws NullPointerException {
		return new VarType(VisitorUtils.line(ctx), Type.fromString(ctx.getText()));
	}

	/**
	 * Visits a kind.
	 *
	 * @param ctx the kind context
	 * @return a VarKind node representing the kind
	 * @throws NullPointerException if arguments of node is null
	 */
	@Override
	public JajaCodeNode visitKind(JajaCodeParser.KindContext ctx) throws NullPointerException {
		return new VarKind(VisitorUtils.line(ctx), Kind.fromString(ctx.getText()));
	}
}
