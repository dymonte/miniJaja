package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.Val;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a new instruction
 */
public class InstrNew extends Instr {
	private final ValIdent valIdent;
	private final VarType type;
	private final VarKind kind;
	private final Val val;

	/**
	 * Instruction for declaring a variable or a method
	 * 
	 * @param lineId the line number of the instruction
	 * @param valIdent  the valIdentifier of the element
	 * @param kind   the kind of the element
	 * @param type   the type of the element
	 * @param val    the initial value of the element
	 */
	public InstrNew(int lineId, ValIdent valIdent, VarKind kind, VarType type, Val val) {
		super(lineId);
		this.valIdent = valIdent;
		this.type = type;
		this.kind = kind;
		this.val = val;
	}

	/**
	 * @return the valIdentifier of the element
	 */
	public ValIdent getValIdent() {
		return valIdent;
	}

	/**
	 * @return the initial value of the element
	 */
	public Val getValVal() {
		return val;
	}

	/**
	 * @return the kind of the element
	 */
	public VarKind getKind() {
		return kind;
	}

	/**
	 * @return the type of the element
	 */
	public VarType getType() {
		return type;
	}

	/**
	 * @return the string representation of the instruction
	 */
	@Override
	public String toString() {
		return super.toString() + "new(" + kind + ", " + valIdent + ", " + type + ", " + val + ")";
	}
}
