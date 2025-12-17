package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarKind;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValNbre;
import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

/**
 * Class representing a new instruction
 */
public class InstrNew extends Instr {
	private final ValIdent valIdent;
	private final VarType type;
	private final VarKind kind;
	private final ValNbre depth;

	/**
	 * Instruction for declaring a variable or a method
	 * 
	 * @param lineId   the line number of the instruction
	 * @param valIdent the valIdentifier of the element
	 * @param kind     the kind of the element
	 * @param type     the type of the element
	 * @param depth  the initial value of the element
	 */
	public InstrNew(int lineId, ValIdent valIdent, VarKind kind, VarType type, ValNbre depth) {
		super(lineId);
		if (valIdent == null)
			throw new NullPointerException("valIdent cannot be null");
		if (kind == null)
			throw new NullPointerException("kind cannot be null");
		if (type == null)
			throw new NullPointerException("type cannot be null");
		if (depth == null)
			throw new NullPointerException("depth cannot be null");
		this.valIdent = valIdent;
		this.type = type;
		this.kind = kind;
		this.depth = depth;
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
	public ValNbre getValDepth() {
		return depth;
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
		return super.toString() + "new(" + kind + ", " + valIdent + ", " + type + ", " + depth + ")";
	}
}
