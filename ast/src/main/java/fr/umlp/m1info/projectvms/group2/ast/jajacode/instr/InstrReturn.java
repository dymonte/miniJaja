package fr.umlp.m1info.projectvms.group2.ast.jajacode.instr;

/**
 * Class representing a return instruction
 */
public class InstrReturn extends Instr{

    /**
     * Instruction for returning a value from a method
     * @param lineId the line number of the instruction
     */
    public InstrReturn(int lineId) {
        super(lineId);
    }

    /**
     * @return the string representation of the instruction
     */
    @Override
    public String toString() {
        return super.toString() + "return";
    }
}
