package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing an jdup instruction
 */
public class InstrJdup extends Instr {

	/**
	 * Instruction for jdup
	 *
	 * @param lineId the line number of the instruction
	 */
	public InstrJdup(int lineId) {
		super(lineId);
	}

	@Override
	public String toString() {
		return super.toString() + "jdup";
	}
}
