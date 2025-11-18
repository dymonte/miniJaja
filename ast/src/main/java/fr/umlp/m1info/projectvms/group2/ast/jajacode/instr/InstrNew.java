package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.Val;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValAddress;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a new instruction
 */
public class InstrNew extends Instr {
	private final ValIdent valIdent;
	private final VarType type;
	private final VarKind kind;
	private final ValAddress address;

	/**
	 * Instruction for declaring a variable or a method
	 * 
	 * @param lineId   the line number of the instruction
	 * @param valIdent the valIdentifier of the element
	 * @param kind     the kind of the element
	 * @param type     the type of the element
	 * @param address  the initial value of the element
	 */
	public InstrNew(int lineId, ValIdent valIdent, VarKind kind, VarType type, ValAddress address) {
		super(lineId);
		if (valIdent == null)
			throw new NullPointerException("valIdent cannot be null");
		if (kind == null)
			throw new NullPointerException("kind cannot be null");
		if (type == null)
			throw new NullPointerException("type cannot be null");
		if (address == null)
			throw new NullPointerException("val cannot be null");
		this.valIdent = valIdent;
		this.type = type;
		this.kind = kind;
		this.address = address;
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
	public ValAddress getValAddress() {
		return address;
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
		return super.toString() + "new(" + kind + ", " + valIdent + ", " + type + ", " + address + ")";
	}
}
