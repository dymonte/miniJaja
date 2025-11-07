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

public class JajaCodeBuildVisitor extends JajaCodeBaseVisitor<JajaCodeNode> {

	private final List<Instr> instructions = new ArrayList<>();

	public List<Instr> getInstructions() {
		return instructions;
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
	public JajaCodeNode visitProg(JajaCodeParser.ProgContext ctx) {
		for (JajaCodeParser.InstrContext instr : ctx.instr()) {
			visit(instr);
		}
		return null;
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
	public JajaCodeNode visitInit(JajaCodeParser.InitContext ctx) {
		instructions.add(new InstrInit(VisitorUtils.line(ctx)));
		return null;
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
	public JajaCodeNode visitSwap(JajaCodeParser.SwapContext ctx) {
		instructions.add(new InstrSwap(VisitorUtils.line(ctx)));
		return visitChildren(ctx);
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
	public JajaCodeNode visitNew(JajaCodeParser.NewContext ctx) {
		instructions.add(new InstrNew(
				VisitorUtils.line(ctx),
				(ValIdent) visit(ctx.ident()),
				(VarKind) visit(ctx.kind()),
				(VarType) visit(ctx.type()),
				(ValAddress) visit(ctx.address())));
		return null;
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
	public JajaCodeNode visitNewArray(JajaCodeParser.NewArrayContext ctx) {
		instructions
				.add(new InstrNewArray(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident()), (VarType) visit(ctx.type())));
		return null;
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
	public JajaCodeNode visitInvoke(JajaCodeParser.InvokeContext ctx) {
		instructions.add(new InstrInvoke(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitLength(JajaCodeParser.LengthContext ctx) {
		instructions.add(new InstrLength(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitReturn(JajaCodeParser.ReturnContext ctx) {
		instructions.add(new InstrReturn(VisitorUtils.line(ctx)));
		return null;
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
	public JajaCodeNode visitWrite(JajaCodeParser.WriteContext ctx) {
		instructions.add(new InstrWrite(VisitorUtils.line(ctx)));
		return null;
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
	public JajaCodeNode visitWriteLn(JajaCodeParser.WriteLnContext ctx) {
		instructions.add(new InstrWriteln(VisitorUtils.line(ctx)));
		return null;
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
	public JajaCodeNode visitPush(JajaCodeParser.PushContext ctx) {
		instructions.add(new InstrPush(VisitorUtils.line(ctx), (Val) visit(ctx.value())));
		return null;
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
	public JajaCodeNode visitPop(JajaCodeParser.PopContext ctx) {
		instructions.add(new InstrPop(VisitorUtils.line(ctx)));
		return null;
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
	public JajaCodeNode visitLoad(JajaCodeParser.LoadContext ctx) {
		instructions.add(new InstrLoad(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitALoad(JajaCodeParser.ALoadContext ctx) {
		instructions.add(new InstrALoad(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitStore(JajaCodeParser.StoreContext ctx) {
		instructions.add(new InstrStore(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitAStore(JajaCodeParser.AStoreContext ctx) {
		instructions.add(new InstrAStore(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitIf(JajaCodeParser.IfContext ctx) {
		instructions.add(new InstrIf(VisitorUtils.line(ctx), (ValAddress) visit(ctx.address())));
		return null;
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
	public JajaCodeNode visitGoto(JajaCodeParser.GotoContext ctx) {
		instructions.add(new InstrGoto(VisitorUtils.line(ctx), (ValAddress) visit(ctx.address())));
		return null;
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
	public JajaCodeNode visitInc(JajaCodeParser.IncContext ctx) {
		instructions.add(new InstrInc(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitAInc(JajaCodeParser.AIncContext ctx) {
		instructions.add(new InstrAInc(VisitorUtils.line(ctx), (ValIdent) visit(ctx.ident())));
		return null;
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
	public JajaCodeNode visitOperator(JajaCodeParser.OperatorContext ctx) {
		return visit(ctx.oper());
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
	public JajaCodeNode visitNop(JajaCodeParser.NopContext ctx) {
		instructions.add(new InstrNop(VisitorUtils.line(ctx)));
		return null;
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
	public JajaCodeNode visitJcStop(JajaCodeParser.JcStopContext ctx) {
		instructions.add(new InstrStop(VisitorUtils.line(ctx)));
		return null;
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
	public JajaCodeNode visitUnary(JajaCodeParser.UnaryContext ctx) {
		return visit(ctx.oper1());
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
	public JajaCodeNode visitBinary(JajaCodeParser.BinaryContext ctx) {
		return visit(ctx.oper2());
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
	public JajaCodeNode visitOper1(JajaCodeParser.Oper1Context ctx) {
		instructions.add(new InstrOperUnary(VisitorUtils.line(ctx), JajaCodeUnaryOper.fromSymbol(ctx.getText())));
		return null;
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
	public JajaCodeNode visitOper2(JajaCodeParser.Oper2Context ctx) {
		instructions.add(new InstrOperBinary(VisitorUtils.line(ctx), JajaCodeBinaryOper.fromSymbol(ctx.getText())));
		return null;
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
	public JajaCodeNode visitIdent(JajaCodeParser.IdentContext ctx) {
		return new ValIdent(VisitorUtils.line(ctx), ctx.IDENTIFIER().getText());
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
	public JajaCodeNode visitIntValue(JajaCodeParser.IntValueContext ctx) {
		return new ValNbre(VisitorUtils.line(ctx), Integer.parseInt(ctx.NUMBER().getText()));
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
	public JajaCodeNode visitBoolValue(JajaCodeParser.BoolValueContext ctx) {
		return new ValBool(VisitorUtils.line(ctx), ctx.getText().equals("true"));
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
	public JajaCodeNode visitStringValue(JajaCodeParser.StringValueContext ctx) {
		return new ValString(VisitorUtils.line(ctx), ctx.STRING().getText());
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
	public JajaCodeNode visitVoidValue(JajaCodeParser.VoidValueContext ctx) {
		return new ValNbre(VisitorUtils.line(ctx), 0);
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
	public JajaCodeNode visitAddress(JajaCodeParser.AddressContext ctx) {
		return new ValAddress(VisitorUtils.line(ctx), Integer.parseInt(ctx.NUMBER().getText()));
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
	public JajaCodeNode visitType(JajaCodeParser.TypeContext ctx) {
		return new VarType(VisitorUtils.line(ctx), Type.fromString(ctx.getText()));
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
	public JajaCodeNode visitKind(JajaCodeParser.KindContext ctx) {
		return new VarKind(VisitorUtils.line(ctx), Kind.fromString(ctx.getText()));
	}
}
