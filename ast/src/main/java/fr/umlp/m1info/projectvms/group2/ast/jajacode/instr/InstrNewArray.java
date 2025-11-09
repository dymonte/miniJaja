package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;

/**
 * Class representing a new array instruction
 */
public class InstrNewArray extends Instr {
	private final ValIdent valIdent;
	private final VarType type;

	/**
	 * Instruction for creating a new array
	 *
	 * @param lineId the line number of the instruction
	 * @param valIdent  the identifier of the array
	 * @param type   the type of values held by the array
	 */
	public InstrNewArray(int lineId, ValIdent valIdent, VarType type) {
		super(lineId);
        if (valIdent == null)
            throw new NullPointerException("valIdent cannot be null");
        if (type == null)
            throw new NullPointerException("type cannot be null");
		this.valIdent = valIdent;
		this.type = type;
	}

	/**
	 * @return the identifier of the array
	 */
	public ValIdent getValIdent() {
		return valIdent;
	}

	/**
	 * @return the type of values held by the array
	 */
	public VarType getType() {
		return type;
	}

	/**
	 * @return the string representation of the instruction
	 */
	@Override
	public String toString() {
		return super.toString() + "newarray(" + valIdent + ", " + type + ")";
	}

}
