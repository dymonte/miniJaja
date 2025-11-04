package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.value.ValIdent;

import fr.umlp.m1info.projectvms.group2.ast.jajacode.var.VarType;

/**
 * Class representing a new array instruction
 */
public class InstrNewArray extends Instr {
	private final ValIdent ident;
	private final VarType type;

	/**
	 * Instruction for creating a new array
	 * 
	 * @param lineId the line number of the instruction
	 * @param ident  the identifier of the array
	 * @param type   the type of values held by the array
	 */
	public InstrNewArray(int lineId, ValIdent ident, VarType type) {
		super(lineId);
		this.ident = ident;
		this.type = type;
	}

	/**
	 * @return the identifier of the array
	 */
	public ValIdent getIdent() {
		return ident;
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
		return "newarray(" + ident + ", " + type + ")";
	}

}
