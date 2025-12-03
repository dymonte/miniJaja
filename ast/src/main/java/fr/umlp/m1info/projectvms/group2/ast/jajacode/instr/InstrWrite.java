package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing a write instruction
 */
public class InstrWrite extends Instr{

    /**
     * Instruction for writing a value to the standard output
     * @param lineId the line number of the instruction
     */
    public InstrWrite(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "write";
    }
}
